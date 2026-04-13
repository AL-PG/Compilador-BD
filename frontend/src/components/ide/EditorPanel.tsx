type EditorPanelProps = {
  sourceCode: string
  hasCompiledOutput: boolean
  isConnecting: boolean
  onSourceCodeChange: (value: string) => void
  onCompile: () => void
  onDownloadSql: () => void
  onDownloadStructure: () => void
  onConnectAndCreate: () => void
}

export function EditorPanel({
  sourceCode,
  hasCompiledOutput,
  isConnecting,
  onSourceCodeChange,
  onCompile,
  onDownloadSql,
  onDownloadStructure,
  onConnectAndCreate,
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
        className="min-h-[480px] w-full flex-1 resize-y rounded-xl border border-neutral-200 bg-neutral-50/50 p-4 font-mono text-sm leading-relaxed text-neutral-800 transition-colors focus:border-neutral-400 focus:bg-white focus:outline-none focus:ring-4 focus:ring-neutral-400/10 max-[1024px]:min-h-[380px]"
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
        >
          Compilar Codigo
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
        <button
          className={`${buttonBase} flex items-center justify-center gap-2 bg-emerald-600 text-white hover:bg-emerald-700 disabled:hover:bg-emerald-600`}
          onClick={onConnectAndCreate}
          disabled={isConnecting}
        >
          {isConnecting ? (
            <>
              <svg className="h-4 w-4 animate-spin" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <circle className="opacity-25" cx="12" cy="12" r="10" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Conectando...
            </>
          ) : (
            'Conectar y Crear BD'
          )}
        </button>
      </div>
    </section>
  )
}