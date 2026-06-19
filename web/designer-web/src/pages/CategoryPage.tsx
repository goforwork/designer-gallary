import { useParams } from 'react-router-dom'
import { Navigation } from '@/components/Navigation'
import { CategoryFilter } from '@/components/CategoryFilter'
import { WorksGrid } from '@/components/WorksGrid'
import { Footer } from '@/components/Footer'
import { Loading } from '@/components/Loading'
import { ErrorMessage } from '@/components/ErrorMessage'
import { useSiteConfig } from '@/hooks/useSiteConfig'
import { useCategories } from '@/hooks/useCategories'
import { useWorks } from '@/hooks/useWorks'

export function CategoryPage() {
  const slug = useParams().slug
  const { config } = useSiteConfig()
  const { categories, loading: categoriesLoading, error: categoriesError } = useCategories()
  const { works, loading: worksLoading, error: worksError } = useWorks(slug)

  const category = categories.find((item) => item.slug === slug)

  if (!config) return <Loading />
  if (!slug) return <ErrorMessage message="Category not found" />

  return (
    <>
      <Navigation />

      <main className="relative z-10 min-h-screen bg-parchment px-6 pb-20 pt-36 lg:px-10">
        <div className="mx-auto max-w-[1200px]">
          <div className="mb-12 text-center">
            <p className="mb-3 text-[11px] font-semibold uppercase tracking-[2px] text-taupe">
              Category
            </p>
            <h1 className="font-serif text-4xl text-charcoal md:text-5xl">
              {category?.name || 'Category'}
            </h1>
            {category?.description && (
              <p className="mx-auto mt-4 max-w-xl text-warm-gray">{category.description}</p>
            )}
          </div>

          {categoriesLoading ? (
            <Loading />
          ) : categoriesError ? (
            <ErrorMessage message={categoriesError} />
          ) : (
            <CategoryFilter categories={categories} activeSlug={slug} />
          )}

          {worksLoading ? (
            <Loading />
          ) : worksError ? (
            <ErrorMessage message={worksError} />
          ) : (
            <WorksGrid works={works} emptyText="No works in this category yet." />
          )}
        </div>
      </main>

      <Footer />
    </>
  )
}
