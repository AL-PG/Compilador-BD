export type CompileError = {
  line: number
  message: string
}

export type ColumnDef = {
  name: string
  type: string
  constraints: string
}

export type TableDef = {
  name: string
  columns: ColumnDef[]
}

export type CompilerResult = {
  errors: CompileError[]
  sql: string
  structure: string
}

export type StatusTone = 'idle' | 'ok' | 'warn' | 'busy'