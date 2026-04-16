import { useMemo, useState } from 'react'
import { SAMPLE_PROGRAM } from '../lib/compiler'
import { downloadTextFile } from '../lib/download'
import { compileProgramWithBackend } from '../lib/api'
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
  const [isCompiling, setIsCompiling] = useState(false)

  const errorsOutput = useMemo(() => {
    if (compileErrors.length === 0) {
      return 'Sin errores de compilacion.'
    }

    return compileErrors.map((error) => `L${error.line}: ${error.message}`).join('\n')
  }, [compileErrors])

  const hasCompiledOutput =
    compileErrors.length === 0 && sqlCode.length > 0 && structureCode.length > 0

  const handleCompile = async () => {
    setIsCompiling(true)
    setStatusTone('busy')
    setStatusText('Compilando con el backend Spring Boot...')

    try {
      const result = await compileProgramWithBackend(sourceCode)
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
      setStatusText('Compilacion exitosa desde backend. Puedes descargar los archivos.')
    } catch (error) {
      const detail =
        error instanceof Error ? error.message : 'No se pudo contactar con el backend.'
      setCompileErrors([{ line: 1, message: detail }])
      setSqlCode('')
      setStructureCode('')
      setStatusTone('warn')
      setStatusText('No se pudo compilar. Verifica que Spring Boot este ejecutandose.')
    } finally {
      setIsCompiling(false)
    }
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

  return {
    sourceCode,
    setSourceCode,
    compileErrors,
    sqlCode,
    structureCode,
    statusText,
    statusTone,
    isCompiling,
    errorsOutput,
    hasCompiledOutput,
    handleCompile,
    handleDownloadSql,
    handleDownloadStructure,
  }
}