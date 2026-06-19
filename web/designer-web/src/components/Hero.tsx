import { useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import gsap from 'gsap'
import { useSiteConfig } from '@/hooks/useSiteConfig'
import { getLenis } from '@/hooks/useLenis'
import { getMediaUrl } from '@/lib/utils'

export function Hero() {
  const { config } = useSiteConfig()
  const navigate = useNavigate()
  const containerRef = useRef<HTMLDivElement>(null)
  const titleRef = useRef<HTMLHeadingElement>(null)
  const subtitleRef = useRef<HTMLParagraphElement>(null)
  const ctaRef = useRef<HTMLAnchorElement>(null)

  const hero = config?.heroConfig

  useEffect(() => {
    if (!hero?.titleLine) return

    const tl = gsap.timeline({ delay: 0.3 })

    tl.fromTo(
      titleRef.current,
      { opacity: 0, y: 30, filter: 'blur(8px)' },
      { opacity: 1, y: 0, filter: 'blur(0px)', duration: 1.2, ease: 'power2.out' }
    )
      .fromTo(
        subtitleRef.current,
        { opacity: 0, y: 20 },
        { opacity: 0.85, y: 0, duration: 1.0, ease: 'power2.out' },
        '-=0.6'
      )

    if (ctaRef.current) {
      tl.fromTo(
        ctaRef.current,
        { opacity: 0, y: 10 },
        { opacity: 1, y: 0, duration: 0.8, ease: 'power2.out' },
        '-=0.4'
      )
    }

    return () => {
      tl.kill()
    }
  }, [hero?.titleLine])

  const handleCta = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault()
    if (!hero?.ctaTarget) return

    if (hero.ctaTarget.startsWith('#')) {
      const lenis = getLenis()
      if (lenis) {
        lenis.scrollTo(hero.ctaTarget)
      } else {
        document.querySelector(hero.ctaTarget)?.scrollIntoView({ behavior: 'smooth' })
      }
    } else {
      navigate(hero.ctaTarget)
    }
  }

  const isVideo = /\.(mp4|webm|ogg)(\?.*)?$/i.test(hero?.backgroundMedia || '')

  return (
    <section
      id="hero"
      ref={containerRef}
      className="relative flex h-screen min-h-[600px] items-end justify-center overflow-hidden pb-[12vh]"
    >
      {hero?.backgroundMedia ? (
        isVideo ? (
          <video
            autoPlay
            muted
            loop
            playsInline
            className="absolute inset-0 h-full w-full object-cover"
          >
            <source src={getMediaUrl(hero.backgroundMedia)} type="video/mp4" />
          </video>
        ) : (
          <img
            src={getMediaUrl(hero.backgroundMedia)}
            alt=""
            className="absolute inset-0 h-full w-full object-cover"
          />
        )
      ) : (
        <div className="absolute inset-0 bg-charcoal" />
      )}

      <div className="absolute inset-0 bg-gradient-to-b from-black/25 via-black/50 to-black/70" />

      <div className="liquid-glass relative z-10 w-[90%] max-w-[600px] px-8 py-10 text-center md:px-10 md:py-12">
        {hero?.eyebrow && (
          <p className="mb-5 text-[11px] font-semibold uppercase tracking-[3px] text-taupe">
            {hero.eyebrow}
          </p>
        )}

        {hero?.titleLine && (
          <h1
            ref={titleRef}
            className="mb-5 font-serif text-[clamp(36px,5vw,62px)] font-normal leading-[1.15] text-parchment opacity-0"
          >
            {hero.titleLine}
            {hero.titleEmphasis && (
              <>
                <br />
                <em className="italic">{hero.titleEmphasis}</em>
              </>
            )}
          </h1>
        )}

        {(hero?.subtitleLine1 || hero?.subtitleLine2) && (
          <p
            ref={subtitleRef}
            className="mx-auto mb-8 max-w-[420px] text-[13px] leading-[1.7] text-parchment opacity-0"
          >
            {hero.subtitleLine1}
            {hero.subtitleLine1 && hero.subtitleLine2 && <br />}
            {hero.subtitleLine2}
          </p>
        )}

        {hero?.ctaText && (
          <a
            ref={ctaRef}
            href={hero.ctaTarget || '#'}
            onClick={handleCta}
            className="inline-block border-b border-parchment/40 pb-1 text-[11px] font-semibold uppercase tracking-[2px] text-parchment opacity-0 transition-colors duration-500 hover:border-taupe"
          >
            {hero.ctaText}
          </a>
        )}
      </div>
    </section>
  )
}
