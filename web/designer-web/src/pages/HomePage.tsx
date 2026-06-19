import { Navigation } from '@/components/Navigation'
import { Hero } from '@/components/Hero'
import { CategoryFilter } from '@/components/CategoryFilter'
import { WorksGrid } from '@/components/WorksGrid'
import { Footer } from '@/components/Footer'
import { Loading } from '@/components/Loading'
import { ErrorMessage } from '@/components/ErrorMessage'
import { useSiteConfig } from '@/hooks/useSiteConfig'
import { useCategories } from '@/hooks/useCategories'
import { useWorks } from '@/hooks/useWorks'

export function HomePage() {
  const { config, loading: configLoading, error: configError } = useSiteConfig()
  const { categories, loading: categoriesLoading, error: categoriesError } = useCategories()
  const { works, loading: worksLoading, error: worksError } = useWorks()

  if (configLoading) return <Loading />
  if (configError || !config) {
    return <ErrorMessage message={configError || 'Failed to load site config'} />
  }

  return (
    <>
      <Navigation />
      <Hero />

      <main id="works" className="relative z-10 bg-parchment px-6 py-20 lg:px-10">
        <div className="mx-auto max-w-[1200px]">
          <p className="mb-3 text-center text-[11px] font-semibold uppercase tracking-[2px] text-taupe">
            Portfolio
          </p>
          <h2 className="mb-4 text-center font-serif text-3xl text-charcoal md:text-4xl">
            Selected Works
          </h2>

          {categoriesLoading ? (
            <Loading />
          ) : categoriesError ? (
            <ErrorMessage message={categoriesError} />
          ) : (
            <CategoryFilter categories={categories} />
          )}

          {worksLoading ? (
            <Loading />
          ) : worksError ? (
            <ErrorMessage message={worksError} />
          ) : (
            <WorksGrid works={works} />
          )}
        </div>
      </main>

      <Footer />
    </>
  )
}
