import type { CompilerResult } from '../types/compiler'

type CreateDatabaseResponse = {
  success: boolean
  message: string
}

const rawApiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim() ?? ''
const apiBaseUrl = rawApiBaseUrl.endsWith('/')
  ? rawApiBaseUrl.slice(0, -1)
  : rawApiBaseUrl

const isProd = import.meta.env.PROD

function resolveApiUrl(path: string): string {
  if (!path.startsWith('/')) {
    path = `/${path}`
  }

  if (!apiBaseUrl) {
    return path
  }

  const baseEndsWithApi = apiBaseUrl.toLowerCase().endsWith('/api')
  const pathStartsWithApi = path.startsWith('/api/')

  if (baseEndsWithApi && pathStartsWithApi) {
    return `${apiBaseUrl}${path.slice(4)}`
  }

  return `${apiBaseUrl}${path}`
}

async function requestJson<T>(path: string, options: RequestInit): Promise<T> {
  if (!apiBaseUrl && isProd) {
    throw new Error(
      'Falta configurar VITE_API_BASE_URL en el frontend (Vercel). Debe apuntar al backend de Railway.',
    )
  }

  const response = await fetch(resolveApiUrl(path), {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
    ...options,
  })

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`)
  }

  return (await response.json()) as T
}

export async function compileProgramWithBackend(program: string): Promise<CompilerResult> {
  return requestJson<CompilerResult>('/api/compiler/compile', {
    method: 'POST',
    body: JSON.stringify({ program }),
  })
}

export async function createDatabaseFromSql(sql: string): Promise<CreateDatabaseResponse> {
  return requestJson<CreateDatabaseResponse>('/api/database/create', {
    method: 'POST',
    body: JSON.stringify({ sql }),
  })
}
