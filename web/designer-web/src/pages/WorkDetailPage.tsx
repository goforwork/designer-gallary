import { useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { Navigation } from '@/components/Navigation'
import { WorksGrid } from '@/components/WorksGrid'
import { Footer } from '@/components/Footer'
import { Loading } from '@/components/Loading'
import { ErrorMessage } from '@/components/ErrorMessage'
import { MediaImage } from '@/components/MediaImage'
import { Lightbox } from '@/components/Lightbox'
import { useSiteConfig } from '@/hooks/useSiteConfig'
import { useWork } from '@/hooks/useWork'
import { useWorks } from '@/hooks/useWorks'

export function WorkDetailPage() {
  const slug = useParams().slug ?? ''
  const { config } = useSiteConfig()
  const { work, loading: workLoading, error: workError } = useWork(slug)
  const { works: allWorks } = useWorks()
  const [lightboxIndex, setLightboxIndex] = useState<number>(-1)

  if (!slug) return <ErrorMessage message="Missing work slug" />
  if (workLoading) return <Loading />
  if (workError || !work) return <ErrorMessage message={workError || 'Work not found'} />
  if (!config) return <Loading />

  const images = [work.coverImage, ...work.images].filter(
    (path): path is string => Boolean(path)
  )

  const relatedWorks = allWorks
    .filter(
      (item) =>
        item.categoryId === work.categoryId && item.id !== work.id && item.isVisible
    )
    .slice(0, 3)

  return (
    <>
      <Navigation />

      <main className="relative z-10 min-h-screen bg-parchment px-6 pb-20 pt-32 lg:px-10">
        <div className="mx-auto max-w-[1200px]">
          <Link
            to="/"
            className="text-[11px] font-semibold uppercase tracking-[1.5px] text-warm-gray transition-colors hover:text-charcoal"
          >
            ← Back to works
          </Link>

          <h1 className="mt-8 font-serif text-4xl text-charcoal md:text-5xl lg:text-6xl">
            {work.title}
          </h1>

          {work.tags.length > 0 && (
            <p className="mt-4 text-[11px] uppercase tracking-wider text-warm-gray">
              {work.tags.join(' / ')}
            </p>
          )}

          {work.description && (
            <p className="mt-8 max-w-2xl whitespace-pre-line leading-relaxed text-warm-gray">
              {work.description}
            </p>
          )}

          <div className="mt-12 grid grid-cols-1 gap-6 md:grid-cols-2">
            {images.map((image, index) => (
              <button
                key={`${image}-${index}`}
                onClick={() => setLightboxIndex(index)}
                className="group aspect-[4/3] overflow-hidden bg-parchment-dark text-left"
              >
                <MediaImage
                  src={image}
                  alt={`${work.title} ${index + 1}`}
                  containerClassName="h-full w-full"
                  className="group-hover:scale-105"
                />
              </button>
            ))}
          </div>

          {relatedWorks.length > 0 && (
            <section className="mt-24">
              <h2 className="mb-8 font-serif text-2xl text-charcoal">Related Works</h2>
              <WorksGrid works={relatedWorks} />
            </section>
          )}
        </div>
      </main>

      <Footer />

      {lightboxIndex >= 0 && images.length > 0 && (
        <Lightbox
          images={images}
          initialIndex={lightboxIndex}
          onClose={() => setLightboxIndex(-1)}
        />
      )}
    </>
  )
}
