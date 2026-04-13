import type { ColumnDef, CompilerResult, TableDef } from '../types/compiler'

export const SAMPLE_PROGRAM = `CREAR BD tienda_virtual;

CREAR TABLA usuarios (
  id INT PK AUTOINC,
  nombre VARCHAR(120) NN,
  correo VARCHAR(180) UNICO
);

CREAR TABLA pedidos (
  id INT PK AUTOINC,
  usuario_id INT NN,
  total DECIMAL(10,2) NN,
  fecha DATETIME
);`

function normalizeConstraints(raw: string) {
  return raw
    .replace(/\bPK\b/gi, 'PRIMARY KEY')
    .replace(/\bUNICO\b/gi, 'UNIQUE')
    .replace(/\bAUTOINC\b/gi, 'AUTO_INCREMENT')
    .replace(/\bNN\b/gi, 'NOT NULL')
    .replace(/\s+/g, ' ')
    .trim()
}

export function compileHighLevelProgram(program: string): CompilerResult {
  const lines = program.split('\n')
  const errors: CompilerResult['errors'] = []
  const sql: string[] = []
  const tables: TableDef[] = []
  let dbName = ''

  const pushError = (line: number, message: string) => {
    errors.push({ line, message })
  }

  let index = 0
  while (index < lines.length) {
    const line = lines[index].trim()
    const lineNumber = index + 1

    if (!line || line.startsWith('//') || line.startsWith('#')) {
      index += 1
      continue
    }

    const createDbMatch = line.match(/^CREAR\s+BD\s+([A-Za-z_][A-Za-z0-9_]*)\s*;?$/i)
    if (createDbMatch) {
      if (!line.endsWith(';')) {
        pushError(lineNumber, 'Falta el punto y coma al final de CREAR BD.')
      }

      dbName = createDbMatch[1]
      sql.push(`CREATE DATABASE IF NOT EXISTS ${dbName};`)
      sql.push(`USE ${dbName};`)
      index += 1
      continue
    }

    const useDbMatch = line.match(/^USAR\s+BD\s+([A-Za-z_][A-Za-z0-9_]*)\s*;?$/i)
    if (useDbMatch) {
      if (!line.endsWith(';')) {
        pushError(lineNumber, 'Falta el punto y coma al final de USAR BD.')
      }

      const selectedDb = useDbMatch[1]
      if (!dbName) {
        dbName = selectedDb
      }
      sql.push(`USE ${selectedDb};`)
      index += 1
      continue
    }

    const createTableMatch = line.match(
      /^CREAR\s+TABLA\s+([A-Za-z_][A-Za-z0-9_]*)\s*\($/i,
    )
    if (createTableMatch) {
      const tableName = createTableMatch[1]
      const columns: ColumnDef[] = []
      let closed = false

      index += 1
      while (index < lines.length) {
        const tableLine = lines[index].trim()
        const tableLineNumber = index + 1

        if (!tableLine || tableLine.startsWith('//') || tableLine.startsWith('#')) {
          index += 1
          continue
        }

        if (/^\)\s*;?\s*$/.test(tableLine)) {
          if (!tableLine.endsWith(';')) {
            pushError(tableLineNumber, 'La tabla debe cerrarse con ");".')
          }

          closed = true
          index += 1
          break
        }

        const normalizedLine = tableLine.endsWith(',')
          ? tableLine.slice(0, -1).trim()
          : tableLine

        const columnMatch = normalizedLine.match(
          /^([A-Za-z_][A-Za-z0-9_]*)\s+([A-Za-z]+(?:\(\d+(?:,\d+)?\))?)(.*)$/i,
        )

        if (!columnMatch) {
          pushError(
            tableLineNumber,
            'Definición de columna invalida. Formato esperado: nombre TIPO [restricciones].',
          )
          index += 1
          continue
        }

        const [, columnName, columnType, rawConstraints] = columnMatch
        const constraints = normalizeConstraints(rawConstraints)

        columns.push({
          name: columnName,
          type: columnType.toUpperCase(),
          constraints,
        })

        index += 1
      }

      if (!closed) {
        pushError(lineNumber, `La tabla ${tableName} no tiene cierre ");".`)
        break
      }

      if (columns.length === 0) {
        pushError(lineNumber, `La tabla ${tableName} no contiene columnas.`)
      }

      tables.push({ name: tableName, columns })

      const sqlColumns = columns.map((column) => {
        const constraints = column.constraints ? ` ${column.constraints}` : ''
        return `  ${column.name} ${column.type}${constraints}`
      })

      sql.push(`CREATE TABLE IF NOT EXISTS ${tableName} (\n${sqlColumns.join(',\n')}\n);`)
      continue
    }

    pushError(
      lineNumber,
      'Instruccion no reconocida. Usa CREAR BD, USAR BD o CREAR TABLA.',
    )
    index += 1
  }

  const structure: string[] = []
  structure.push(`Base de datos: ${dbName || 'No definida'}`)
  structure.push(`Cantidad de tablas: ${tables.length}`)

  if (tables.length === 0) {
    structure.push('No se encontraron tablas en el programa.')
  } else {
    for (const table of tables) {
      structure.push('')
      structure.push(`Tabla: ${table.name}`)

      if (table.columns.length === 0) {
        structure.push('  - Sin columnas definidas')
        continue
      }

      for (const column of table.columns) {
        const extra = column.constraints ? ` (${column.constraints})` : ''
        structure.push(`  - ${column.name}: ${column.type}${extra}`)
      }
    }
  }

  return {
    errors,
    sql: sql.join('\n\n'),
    structure: structure.join('\n'),
  }
}