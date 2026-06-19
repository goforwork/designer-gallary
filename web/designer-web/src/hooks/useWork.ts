import { useEffect, useState } from 'react'
import { api } from '@/api/client'
import type { Work } from '@/types'

export function useWork(slug: string) {
  const [work, setWork] = useState<Work | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!slug) {
      setWork(null)
      setLoading(false)
      return
    }

    setLoading(true)
    api
      .getWork(slug)
      .then(setWork)
      .catch((err) => setError(err instanceof Error ? err.message : 'Failed to load work'))
      .finally(() => setLoading(false))
  }, [slug])

  return { work, loading, error }
}
