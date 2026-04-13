import type { CompilerResult } from '../types/compiler'

type CreateDatabaseResponse = {
  success: boolean
  message: string
}

const rawApiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim() ?? ''
const apiBaseUrl = rawApiBaseUrl.endsWith('/')
  ? rawApiBaseUrl.slice(0, -1)
  : rawApiBaseUrl

async function requestJson<T>(path: string, options: RequestInit): Promise<T> {
  const response = await fetch(`${apiBaseUrl}${path}`, {
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
