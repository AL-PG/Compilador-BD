export type CompileError = {
  line: number
  message: string
}

export type ColumnDef = {
  name: string
  type: string
  primaryKey: boolean
}

export type TableDef = {
  name: string
  columns: ColumnDef[]
}

export type CompilerResult = {
  errors: CompileError[]
  sql: string
  structure: string
  databaseName: string
  tables: TableDef[]
}

export type CreateDatabaseResult = {
  success: boolean
  message: string
}

export type StatusTone = 'idle' | 'ok' | 'warn' | 'busy'