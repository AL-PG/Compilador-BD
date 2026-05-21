import type { CompileError, StatusTone, TableDef } from '../../types/compiler'
import { OutputBlock } from './OutputBlock'
import { generateCrudHtml } from '../../lib/crud-generator'

const rawApiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim() ?? ''
const apiBaseUrl = rawApiBaseUrl.endsWith('/')
  ? rawApiBaseUrl.slice(0, -1)
  : rawApiBaseUrl

function resolveApiUrl(): string {
  if (!apiBaseUrl) {
    return window.location.origin
  }
  if (apiBaseUrl.toLowerCase().endsWith('/api')) {
    return apiBaseUrl.slice(0, -4)
  }
  return apiBaseUrl
}

type ResultsPanelProps = {
  compileErrors: CompileError[]
  statusTone: StatusTone
  statusText: string
  sqlCode: string
  structureCode: string
  databaseName: string
  tables: TableDef[]
}

export function ResultsPanel({
  compileErrors,
  sqlCode,
  structureCode,
  databaseName,
  tables,
}: ResultsPanelProps) {
  const handlePreviewCrud = (table: TableDef) => {
    const html = generateCrudHtml(databaseName, table, resolveApiUrl())
    const blob = new Blob([html], { type: 'text/html;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    window.open(url, '_blank')
  }

  return (
    <section className="flex h-full flex-col gap-5 rounded-2xl border border-neutral-200 bg-white p-5 shadow-sm sm:p-6 lg:p-8">
      <div className="flex flex-1 flex-col gap-5">
        <div className="rounded-xl border border-red-200 bg-red-50/40 p-4">
          <h3 className="mb-2 text-sm font-semibold text-red-900">Errores de Compilacion</h3>

          {compileErrors.length === 0 ? (
            <p className="text-sm text-red-800">Sin errores de compilacion.</p>
          ) : (
            <ul className="space-y-2" aria-label="Errores de compilacion con linea exacta">
              {compileErrors.map((error, index) => {
                const match = error.message.match(/^line\s+\d+:(\d+)\s+(.*)$/i)
                const column = match?.[1]
                const cleanMessage = match?.[2] ?? error.message

                return (
                  <li
                    key={`${error.line}-${index}`}
                    className="rounded-lg border border-red-200 bg-white px-3 py-2"
                  >
                    <p className="text-xs font-semibold uppercase tracking-wide text-red-700">
                      {column ? `Linea ${error.line}, columna ${column}` : `Linea ${error.line}`}
                    </p>
                    <p className="mt-1 font-mono text-[13px] text-red-900">{cleanMessage}</p>
                  </li>
                )
              })}
            </ul>
          )}
        </div>

        <OutputBlock
          title="Codigo SQL Generado"
          value={sqlCode || 'Compila para generar el SQL equivalente...'}
          ariaLabel="Codigo SQL Generado"
        />
        <OutputBlock
          title="Estructura de Datos"
          value={structureCode || 'Compila para obtener la descripcion de la estructura...'}
          ariaLabel="Estructura de la base de datos"
        />

        {tables.length > 0 && (
          <div className="rounded-xl border border-neutral-200 bg-neutral-50/60 p-4">
            <h3 className="mb-3 text-sm font-semibold text-neutral-900">Aplicaciones CRUD Generadas</h3>
            <div className="space-y-3">
              {tables.map((table) => (
                <div key={table.name} className="rounded-lg border border-neutral-200 bg-white p-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm font-semibold text-neutral-900">{table.name}</p>
                      <p className="text-xs text-neutral-600">
                        {table.columns.map((c) => `${c.name}${c.primaryKey ? ' (PK)' : ''}`).join(', ')}
                      </p>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handlePreviewCrud(table)}
                        className="rounded-lg bg-neutral-900 px-3 py-1.5 text-xs font-medium text-white hover:bg-neutral-800"
                      >
                        Probar CRUD
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </section>
  )
}
