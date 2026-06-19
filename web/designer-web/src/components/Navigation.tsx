import { useEffect, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { Menu, X } from 'lucide-react'
import { useSiteConfig } from '@/hooks/useSiteConfig'
import { getLenis } from '@/hooks/useLenis'
import { cn } from '@/lib/utils'

function useHeroInNav() {
  const [overHero, setOverHero] = useState(true)

  useEffect(() => {
    const handleScroll = () => {
      const hero = document.getElementById('hero')
      const nav = document.getElementById('main-nav')

      if (!hero || !nav) {
        setOverHero(false)
        return
      }

      const navBottom = nav.getBoundingClientRect().bottom
      const heroRect = hero.getBoundingClientRect()
      setOverHero(heroRect.top <= navBottom && heroRect.bottom >= navBottom)
    }

    handleScroll()
    window.addEventListener('scroll', handleScroll, { passive: true })
    window.addEventListener('resize', handleScroll)

    return () => {
      window.removeEventListener('scroll', handleScroll)
      window.removeEventListener('resize', handleScroll)
    }
  }, [])

  return overHero
}

export function Navigation() {
  const { config } = useSiteConfig()
  const [mobileOpen, setMobileOpen] = useState(false)
  const overHero = useHeroInNav()
  const location = useLocation()

  const textColor = overHero ? 'text-parchment' : 'text-charcoal'
  const borderColor = overHero ? 'border-parchment/30' : 'border-charcoal/10'

  const handleAnchor = (event: React.MouseEvent<HTMLAnchorElement>, target: string) => {
    event.preventDefault()
    setMobileOpen(false)

    if (location.pathname !== '/') {
      window.location.href = `/#${target}`
      return
    }

    const lenis = getLenis()
    if (lenis) {
      lenis.scrollTo(`#${target}`)
    } else {
      document.getElementById(target)?.scrollIntoView({ behavior: 'smooth' })
    }
  }

  return (
    <nav
      id="main-nav"
      className="fixed top-0 left-0 z-50 w-full py-5 transition-all duration-500 lg:py-6"
    >
      <div
        className={cn(
          'liquid-glass mx-auto flex max-w-[1200px] items-center justify-between px-6 py-3.5 lg:px-10',
          borderColor
        )}
      >
        <Link
          to="/"
          className={cn(
            'font-serif text-xl font-medium uppercase tracking-[2px] transition-colors duration-500',
            textColor
          )}
        >
          {config?.brandName || 'Studio'}
        </Link>

        <div className="hidden items-center gap-9 md:flex">
          <Link
            to="/"
            className={cn(
              'text-[11px] font-semibold uppercase tracking-[1.3px] opacity-85 transition-colors duration-500 hover:opacity-100',
              textColor
            )}
          >
            Home
          </Link>
          <a
            href="#works"
            onClick={(event) => handleAnchor(event, 'works')}
            className={cn(
              'text-[11px] font-semibold uppercase tracking-[1.3px] opacity-85 transition-colors duration-500 hover:opacity-100',
              textColor
            )}
          >
            Works
          </a>
          <a
            href="#footer"
            onClick={(event) => handleAnchor(event, 'footer')}
            className={cn(
              'text-[11px] font-semibold uppercase tracking-[1.3px] opacity-85 transition-colors duration-500 hover:opacity-100',
              textColor
            )}
          >
            Contact
          </a>
        </div>

        <button
          onClick={() => setMobileOpen(true)}
          className={cn('md:hidden', textColor)}
          aria-label="Open menu"
        >
          <Menu className="h-6 w-6" />
        </button>
      </div>

      {mobileOpen && (
        <div className="fixed inset-0 z-[60] flex flex-col items-center justify-center bg-parchment/95 backdrop-blur-md md:hidden">
          <button
            onClick={() => setMobileOpen(false)}
            className="absolute right-6 top-6 text-charcoal"
            aria-label="Close menu"
          >
            <X className="h-8 w-8" />
          </button>

          <nav className="flex flex-col items-center gap-8">
            <Link
              to="/"
              onClick={() => setMobileOpen(false)}
              className="font-serif text-2xl text-charcoal"
            >
              Home
            </Link>
            <a
              href="#works"
              onClick={(event) => handleAnchor(event, 'works')}
              className="font-serif text-2xl text-charcoal"
            >
              Works
            </a>
            <a
              href="#footer"
              onClick={(event) => handleAnchor(event, 'footer')}
              className="font-serif text-2xl text-charcoal"
            >
              Contact
            </a>
          </nav>
        </div>
      )}
    </nav>
  )
}
