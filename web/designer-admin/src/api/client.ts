import type { ApiResponse, Category, SiteConfig, Work } from '@/types'

const API_BASE = '/api'

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const url = `${API_BASE}${path}`
  const body = options.body
  const needsJsonHeader = body !== undefined && !(body instanceof FormData)

  const response = await fetch(url, {
    ...options,
    credentials: 'include',
    headers: {
      ...(options.headers || {}),
      ...(needsJsonHeader ? { 'Content-Type': 'application/json' } : {}),
    },
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  const json = (await response.json()) as ApiResponse<T>
  const success =
    json.success !== undefined
      ? json.success
      : json.ec === 0 || json.code === 0

  if (!success) {
    throw new Error(json.message || json.em || 'Request failed')
  }

  return json.data
}

export const api = {
  login: (username: string, password: string) =>
    request<{ username: string; role: string }>('/admin/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    }),

  logout: () => request<string>('/admin/auth/logout', { method: 'POST' }),

  me: () => request<{ username: string; role: string } | null>('/admin/auth/me'),

  listWorks: () => request<Work[]>('/admin/works'),

  getWork: (id: string) => request<Work>(`/admin/works/${id}`),

  createWork: (work: Partial<Work>) =>
    request<Work>('/admin/works', {
      method: 'POST',
      body: JSON.stringify(work),
    }),

  updateWork: (id: string, work: Partial<Work>) =>
    request<Work>(`/admin/works/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(work),
    }),

  deleteWork: (id: string) =>
    request<string>(`/admin/works/${id}`, { method: 'DELETE' }),

  listCategories: () => request<Category[]>('/admin/categories'),

  getCategory: (id: string) => request<Category>(`/admin/categories/${id}`),

  createCategory: (category: Partial<Category>) =>
    request<Category>('/admin/categories', {
      method: 'POST',
      body: JSON.stringify(category),
    }),

  updateCategory: (id: string, category: Partial<Category>) =>
    request<Category>(`/admin/categories/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(category),
    }),

  deleteCategory: (id: string) =>
    request<string>(`/admin/categories/${id}`, { method: 'DELETE' }),

  getSiteConfig: () => request<SiteConfig>('/admin/site-config'),

  updateSiteConfig: (config: Partial<SiteConfig>) =>
    request<SiteConfig>('/admin/site-config', {
      method: 'PATCH',
      body: JSON.stringify(config),
    }),

  uploadImage: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request<string>('/admin/upload', {
      method: 'POST',
      body: formData,
    })
  },
}
