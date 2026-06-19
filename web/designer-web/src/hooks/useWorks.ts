import { useEffect, useState } from 'react'
import { api } from '@/api/client'
import type { Work } from '@/types'

export function useWorks(categorySlug?: string) {
  const [works, setWorks] = useState<Work[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    setLoading(true)
    api
      .getWorks(categorySlug)
      .then(setWorks)
      .catch((err) => setError(err instanceof Error ? err.message : 'Failed to load works'))
      .finally(() => setLoading(false))
  }, [categorySlug])

  return { works, loading, error }
}
