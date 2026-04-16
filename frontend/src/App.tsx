import { EditorPanel } from './components/ide/EditorPanel'
import { IdeHeader } from './components/ide/IdeHeader'
import { ResultsPanel } from './components/ide/ResultsPanel'
import { useCompilerIde } from './hooks/useCompilerIde'

function App() {
  const ide = useCompilerIde()

  return (
    <div className="min-h-screen bg-neutral-50 font-sans text-neutral-900 selection:bg-neutral-200">
      <div className="mx-auto flex w-full max-w-350 flex-col gap-6 px-4 py-8 sm:px-6 lg:px-8">
        <IdeHeader />

        <main className="grid items-start gap-6 lg:grid-cols-[minmax(400px,1.2fr)_minmax(320px,0.8fr)] max-lg:grid-cols-1">
          <EditorPanel
            sourceCode={ide.sourceCode}
            hasCompiledOutput={ide.hasCompiledOutput}
            isCompiling={ide.isCompiling}
            onSourceCodeChange={ide.setSourceCode}
            onCompile={ide.handleCompile}
            onDownloadSql={ide.handleDownloadSql}
            onDownloadStructure={ide.handleDownloadStructure}
          />

          <ResultsPanel
            statusTone={ide.statusTone}
            statusText={ide.statusText}
            compileErrors={ide.compileErrors}
            sqlCode={ide.sqlCode}
            structureCode={ide.structureCode}
          />
        </main>
      </div>
    </div>
  )
}

export default App
