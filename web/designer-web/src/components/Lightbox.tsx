import { useEffect, useState } from 'react'
import { X, ChevronLeft, ChevronRight } from 'lucide-react'
import { getMediaUrl } from '@/lib/utils'

interface LightboxProps {
  images: string[]
  initialIndex: number
  onClose: () => void
}

export function Lightbox({ images, initialIndex, onClose }: LightboxProps) {
  const [index, setIndex] = useState(initialIndex)

  const goPrevious = () => {
    setIndex((current) => (current - 1 + images.length) % images.length)
  }

  const goNext = () => {
    setIndex((current) => (current + 1) % images.length)
  }

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onClose()
      if (event.key === 'ArrowLeft') goPrevious()
      if (event.key === 'ArrowRight') goNext()
    }

    document.body.style.overflow = 'hidden'
    window.addEventListener('keydown', handleKeyDown)

    return () => {
      document.body.style.overflow = ''
      window.removeEventListener('keydown', handleKeyDown)
    }
  }, [images.length, onClose])

  return (
    <div
      className="fixed inset-0 z-[100] flex items-center justify-center bg-charcoal/95 backdrop-blur-sm"
      onClick={onClose}
    >
      <button
        onClick={onClose}
        className="absolute right-6 top-6 rounded-full p-2 text-parchment transition-colors hover:text-taupe"
        aria-label="Close lightbox"
      >
        <X className="h-8 w-8" />
      </button>

      {images.length > 1 && (
        <button
          onClick={(event) => {
            event.stopPropagation()
            goPrevious()
          }}
          className="absolute left-6 top-1/2 -translate-y-1/2 rounded-full p-2 text-parchment transition-colors hover:text-taupe"
          aria-label="Previous image"
        >
          <ChevronLeft className="h-10 w-10" />
        </button>
      )}

      <img
        src={getMediaUrl(images[index])}
        alt={`Gallery image ${index + 1}`}
        className="max-h-[90vh] max-w-[90vw] object-contain"
        onClick={(event) => event.stopPropagation()}
      />

      {images.length > 1 && (
        <button
          onClick={(event) => {
            event.stopPropagation()
            goNext()
          }}
          className="absolute right-6 top-1/2 -translate-y-1/2 rounded-full p-2 text-parchment transition-colors hover:text-taupe"
          aria-label="Next image"
        >
          <ChevronRight className="h-10 w-10" />
        </button>
      )}

      <div className="absolute bottom-6 left-1/2 -translate-x-1/2 text-[11px] uppercase tracking-wider text-parchment/80">
        {index + 1} / {images.length}
      </div>
    </div>
  )
}
