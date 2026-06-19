import { Link } from 'react-router-dom'
import { MediaImage } from './MediaImage'
import type { Work } from '@/types'

interface WorksGridProps {
  works: Work[]
  emptyText?: string
}

export function WorksGrid({ works, emptyText = 'No works found.' }: WorksGridProps) {
  if (works.length === 0) {
    return (
      <div className="flex min-h-[30vh] items-center justify-center">
        <p className="text-center text-warm-gray">{emptyText}</p>
      </div>
    )
  }

  return (
    <div className="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
      {works
        .filter((work) => work.isVisible)
        .sort((a, b) => a.sortOrder - b.sortOrder)
        .map((work) => (
          <Link
            key={work.id}
            to={`/work/${work.slug}`}
            className="group block"
          >
            <div className="aspect-[4/5] overflow-hidden bg-parchment-dark">
              <MediaImage
                src={work.coverImage}
                alt={work.title}
                containerClassName="h-full w-full"
                className="group-hover:scale-105"
              />
            </div>
            <div className="mt-4">
              <h3 className="font-serif text-xl text-charcoal transition-colors group-hover:text-taupe">
                {work.title}
              </h3>
              {work.tags.length > 0 && (
                <p className="mt-1 text-[11px] uppercase tracking-wider text-warm-gray">
                  {work.tags.slice(0, 3).join(' / ')}
                </p>
              )}
            </div>
          </Link>
        ))}
    </div>
  )
}
