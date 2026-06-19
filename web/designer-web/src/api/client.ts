import type { Category, SiteConfig, Work } from '@/types'

const API_BASE = '/api'

export interface ApiResponse<T> {
  success?: boolean
  code?: number
  ec?: number
  message?: string
  em?: string
  data: T
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    credentials: 'include',
    headers: {
      ...(options.headers || {}),
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
    },
  })

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status} ${response.statusText}`)
  }

  const result: ApiResponse<T> = await response.json()
  const isSuccess =
    result.success !== undefined
      ? result.success
      : (result.ec === 0 || result.code === 0)

  if (!isSuccess) {
    throw new Error(result.message || result.em || 'API request failed')
  }

  return result.data
}

export const api = {
  getSiteConfig: () => request<SiteConfig>('/public/site-config'),
  getCategories: () => request<Category[]>('/public/categories'),
  getWorks: (category?: string) =>
    request<Work[]>(
      `/public/works${category ? `?category=${encodeURIComponent(category)}` : ''}`
    ),
  getWork: (slug: string) => request<Work>(`/public/works/${encodeURIComponent(slug)}`),
}
