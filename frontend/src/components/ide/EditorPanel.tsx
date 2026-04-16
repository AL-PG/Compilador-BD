type EditorPanelProps = {
  sourceCode: string
  hasCompiledOutput: boolean
  isCompiling: boolean
  onSourceCodeChange: (value: string) => void
  onCompile: () => void
  onDownloadSql: () => void
  onDownloadStructure: () => void
}

export function EditorPanel({
  sourceCode,
  hasCompiledOutput,
  isCompiling,
  onSourceCodeChange,
  onCompile,
  onDownloadSql,
  onDownloadStructure,
}: EditorPanelProps) {
  const buttonBase =
    'rounded-xl px-4 py-2.5 text-sm font-medium transition-all outline-none focus-visible:ring-2 focus-visible:ring-neutral-400 focus-visible:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50'
  const primaryButton = `${buttonBase} bg-neutral-900 text-white hover:bg-neutral-800 disabled:hover:bg-neutral-900`
  const neutralButton = `${buttonBase} border border-neutral-200 bg-white text-neutral-700 hover:bg-neutral-50 hover:border-neutral-300 disabled:hover:bg-white disabled:hover:border-neutral-200`

  return (
    <section className="flex h-full flex-col rounded-2xl border border-neutral-200 bg-white p-5 shadow-sm sm:p-6 lg:p-8">
      <div className="mb-4 flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
        <h2 className="text-lg font-semibold tracking-tight text-neutral-900">Editor de Instrucciones</h2>
        <span className="inline-flex items-center rounded-full bg-neutral-100 px-2.5 py-1 text-xs font-medium text-neutral-600">
          Entrada Principal
        </span>
      </div>

      <textarea
        id="source-program"
        className="min-h-120 w-full flex-1 resize-y rounded-xl border border-neutral-200 bg-neutral-50/50 p-4 font-mono text-sm leading-relaxed text-neutral-800 transition-colors focus:border-neutral-400 focus:bg-white focus:outline-none focus:ring-4 focus:ring-neutral-400/10 max-[1024px]:min-h-95"
        value={sourceCode}
        onChange={(event) => onSourceCodeChange(event.target.value)}
        spellCheck={false}
        aria-label="Instrucciones del lenguaje de alto nivel"
        placeholder="Escribe tus instrucciones aqui..."
      />

      <div className="mt-5 grid grid-cols-1 gap-3 sm:grid-cols-2">
        <button
          className={primaryButton}
          onClick={onCompile}
          disabled={isCompiling}
        >
          {isCompiling ? 'Compilando...' : 'Compilar Codigo'}
        </button>
        <button className={neutralButton} onClick={onDownloadSql} disabled={!hasCompiledOutput}>
          Descargar SQL
        </button>
        <button
          className={neutralButton}
          onClick={onDownloadStructure}
          disabled={!hasCompiledOutput}
        >
          Descargar JSON
        </button>
      </div>
    </section>
  )
}