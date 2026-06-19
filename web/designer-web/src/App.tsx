import { useEffect } from 'react'
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom'
import { useLenis } from '@/hooks/useLenis'
import { HomePage } from '@/pages/HomePage'
import { CategoryPage } from '@/pages/CategoryPage'
import { WorkDetailPage } from '@/pages/WorkDetailPage'
import { api } from '@/api/client'

function ScrollToTop() {
  const { pathname } = useLocation()

  useEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])

  return null
}

function AppContent() {
  useLenis()

  return (
    <>
      <ScrollToTop />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/category/:slug" element={<CategoryPage />} />
        <Route path="/work/:slug" element={<WorkDetailPage />} />
      </Routes>
    </>
  )
}

export default function App() {
  useEffect(() => {
    api
      .getSiteConfig()
      .then((config) => {
        if (config.siteTitle) {
          document.title = config.siteTitle
        }

        let metaDescription = document.querySelector<HTMLMetaElement>('meta[name="description"]')
        if (!metaDescription) {
          metaDescription = document.createElement('meta')
          metaDescription.name = 'description'
          document.head.appendChild(metaDescription)
        }
        metaDescription.content = config.siteDescription || ''
      })
      .catch(() => {
        // Silently ignore; the page will show its own error state.
      })
  }, [])

  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  )
}
