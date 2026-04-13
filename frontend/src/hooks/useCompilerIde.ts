import { useMemo, useState } from 'react'
import { SAMPLE_PROGRAM, compileHighLevelProgram } from '../lib/compiler'
import { downloadTextFile } from '../lib/download'
import type { CompileError, StatusTone } from '../types/compiler'

export function useCompilerIde() {
  const [sourceCode, setSourceCode] = useState(SAMPLE_PROGRAM)
  const [compileErrors, setCompileErrors] = useState<CompileError[]>([])
  const [sqlCode, setSqlCode] = useState('')
  const [structureCode, setStructureCode] = useState('')
  const [statusText, setStatusText] = useState(
    'Escribe tu lenguaje de alto nivel y ejecuta Compilar.',
  )
  const [statusTone, setStatusTone] = useState<StatusTone>('idle')
  const [isConnecting, setIsConnecting] = useState(false)

  const errorsOutput = useMemo(() => {
    if (compileErrors.length === 0) {
      return 'Sin errores de compilacion.'
    }

    return compileErrors.map((error) => `L${error.line}: ${error.message}`).join('\n')
  }, [compileErrors])

  const hasCompiledOutput =
    compileErrors.length === 0 && sqlCode.length > 0 && structureCode.length > 0

  const handleCompile = () => {
    const result = compileHighLevelProgram(sourceCode)
    setCompileErrors(result.errors)

    if (result.errors.length > 0) {
      setSqlCode('')
      setStructureCode('')
      setStatusTone('warn')
      setStatusText(`Compilacion finalizada con ${result.errors.length} error(es).`)
      return
    }

    setSqlCode(result.sql)
    setStructureCode(result.structure)
    setStatusTone('ok')
    setStatusText('Compilacion exitosa. Puedes descargar los archivos de salida.')
  }

  const handleDownloadSql = () => {
    if (!hasCompiledOutput) {
      setStatusTone('warn')
      setStatusText('No hay SQL para descargar. Compila primero sin errores.')
      return
    }

    downloadTextFile(sqlCode, 'codigo_sql_generado.txt')
    setStatusTone('ok')
    setStatusText('Archivo generado: codigo_sql_generado.txt')
  }

  const handleDownloadStructure = () => {
    if (!hasCompiledOutput) {
      setStatusTone('warn')
      setStatusText('No hay estructura para descargar. Compila primero sin errores.')
      return
    }

    downloadTextFile(structureCode, 'estructura_bd.txt')
    setStatusTone('ok')
    setStatusText('Archivo generado: estructura_bd.txt')
  }

  const handleConnectAndCreate = () => {
    if (!hasCompiledOutput) {
      setStatusTone('warn')
      setStatusText('Corrige errores y compila antes de crear la base de datos.')
      return
    }

    setIsConnecting(true)
    setStatusTone('busy')
    setStatusText('Conectando al gestor y creando la base de datos...')

    window.setTimeout(() => {
      setIsConnecting(false)
      setStatusTone('ok')
      setStatusText(
        'Conexion simulada completada. Integra aqui el endpoint real para ejecutar el SQL.',
      )
    }, 1400)
  }

  return {
    sourceCode,
    setSourceCode,
    sqlCode,
    structureCode,
    statusText,
    statusTone,
    isConnecting,
    errorsOutput,
    hasCompiledOutput,
    handleCompile,
    handleDownloadSql,
    handleDownloadStructure,
    handleConnectAndCreate,
  }
}