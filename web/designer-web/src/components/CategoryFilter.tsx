import { Link } from 'react-router-dom'
import { cn } from '@/lib/utils'
import type { Category } from '@/types'

interface CategoryFilterProps {
  categories: Category[]
  activeSlug?: string
}

export function CategoryFilter({ categories, activeSlug }: CategoryFilterProps) {
  return (
    <div className="flex flex-wrap items-center justify-center gap-4 py-10 md:py-14">
      <Link
        to="/"
        className={cn(
          'text-[11px] font-semibold uppercase tracking-[1.5px] transition-colors',
          activeSlug ? 'text-warm-gray hover:text-charcoal' : 'text-charcoal'
        )}
      >
        All
      </Link>

      {categories
        .filter((category) => category.isVisible)
        .sort((a, b) => a.sortOrder - b.sortOrder)
        .map((category) => (
          <Link
            key={category.id}
            to={`/category/${category.slug}`}
            className={cn(
              'text-[11px] font-semibold uppercase tracking-[1.5px] transition-colors',
              activeSlug === category.slug
                ? 'text-charcoal'
                : 'text-warm-gray hover:text-charcoal'
            )}
          >
            {category.name}
          </Link>
        ))}
    </div>
  )
}
