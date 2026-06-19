import { useState } from 'react'
import { ImageIcon } from 'lucide-react'
import { cn, getMediaUrl } from '@/lib/utils'

interface MediaImageProps {
  src: string | null | undefined
  alt: string
  className?: string
  containerClassName?: string
}

export function MediaImage({ src, alt, className, containerClassName }: MediaImageProps) {
  const [error, setError] = useState(false)
  const url = getMediaUrl(src)

  if (!url || error) {
    return (
      <div
        className={cn(
          'flex items-center justify-center bg-parchment-dark',
          containerClassName
        )}
      >
        <ImageIcon className="h-8 w-8 text-taupe/50" />
      </div>
    )
  }

  return (
    <div className={cn('overflow-hidden', containerClassName)}>
      <img
        src={url}
        alt={alt}
        loading="lazy"
        className={cn(
          'h-full w-full object-cover transition-transform duration-700',
          className
        )}
        onError={() => setError(true)}
      />
    </div>
  )
}
