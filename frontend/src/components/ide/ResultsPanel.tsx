import type { CompileError, StatusTone } from '../../types/compiler'
import { OutputBlock } from './OutputBlock'

type ResultsPanelProps = {
  compileErrors: CompileError[]
  statusTone: StatusTone
  statusText: string
  sqlCode: string
  structureCode: string
}

export function ResultsPanel({
  compileErrors,
  sqlCode,
  structureCode,
}: ResultsPanelProps) {
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
          title="Estructura de Datos (JSON)"
          value={
            structureCode || 'Compila para obtener la descripcion de la estructura...'
          }
          ariaLabel="Estructura de la base de datos"
        />
      </div>
    </section>
  )
}