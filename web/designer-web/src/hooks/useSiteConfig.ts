import { useEffect, useState } from 'react'
import { api } from '@/api/client'
import type { SiteConfig } from '@/types'

let configCache: SiteConfig | null = null

export function useSiteConfig() {
  const [config, setConfig] = useState<SiteConfig | null>(configCache)
  const [loading, setLoading] = useState(!configCache)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (configCache) return

    api
      .getSiteConfig()
      .then((data) => {
        configCache = data
        setConfig(data)
      })
      .catch((err) => setError(err instanceof Error ? err.message : 'Failed to load config'))
      .finally(() => setLoading(false))
  }, [])

  return { config, loading, error }
}
