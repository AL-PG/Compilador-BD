import type { StatusTone } from '../../types/compiler'
import { OutputBlock } from './OutputBlock'
import { StatusBadge } from './StatusBadge'

type ResultsPanelProps = {
  statusTone: StatusTone
  statusText: string
  errorsOutput: string
  sqlCode: string
  structureCode: string
}

export function ResultsPanel({
  statusTone,
  statusText,
  errorsOutput,
  sqlCode,
  structureCode,
}: ResultsPanelProps) {
  return (
    <section className="flex h-full flex-col gap-5 rounded-2xl border border-neutral-200 bg-white p-5 shadow-sm sm:p-6 lg:p-8">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 className="text-lg font-semibold tracking-tight text-neutral-900">Resultados del Proceso</h2>
        <StatusBadge tone={statusTone} text={statusText} />
      </div>

      <div className="flex flex-1 flex-col gap-5">
        <OutputBlock
          title="Errores de Compilacion"
          value={errorsOutput}
          ariaLabel="Errores de Compilacion"
          isError
        />
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