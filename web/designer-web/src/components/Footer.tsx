import { Mail, Phone, ExternalLink } from 'lucide-react'
import { useSiteConfig } from '@/hooks/useSiteConfig'

export function Footer() {
  const { config } = useSiteConfig()

  if (!config) return null

  return (
    <footer
      id="footer"
      className="relative z-10 border-t border-charcoal/10 bg-parchment-dark"
    >
      <div className="mx-auto max-w-[1200px] px-6 py-20 lg:px-10">
        <div className="grid gap-12 md:grid-cols-2 lg:grid-cols-4">
          <div>
            <p className="font-serif text-lg font-medium uppercase tracking-[1px] text-charcoal">
              {config.brandName}
            </p>
            <p className="mt-4 max-w-xs text-sm leading-relaxed text-warm-gray">
              {config.siteDescription}
            </p>
          </div>

          <div>
            <p className="mb-4 text-[11px] font-semibold uppercase tracking-[2px] text-taupe">
              Contact
            </p>
            {config.contactEmail && (
              <a
                href={`mailto:${config.contactEmail}`}
                className="mb-2 flex items-center gap-2 text-sm text-warm-gray transition-colors hover:text-charcoal"
              >
                <Mail className="h-4 w-4" />
                {config.contactEmail}
              </a>
            )}
            {config.contactPhone && (
              <a
                href={`tel:${config.contactPhone}`}
                className="flex items-center gap-2 text-sm text-warm-gray transition-colors hover:text-charcoal"
              >
                <Phone className="h-4 w-4" />
                {config.contactPhone}
              </a>
            )}
          </div>

          <div>
            <p className="mb-4 text-[11px] font-semibold uppercase tracking-[2px] text-taupe">
              Social
            </p>
            <div className="flex flex-col gap-2">
              {config.socialLinks.map((link) => (
                <a
                  key={link.name + link.url}
                  href={link.url}
                  target="_blank"
                  rel="noreferrer"
                  className="flex items-center gap-2 text-sm text-warm-gray transition-colors hover:text-charcoal"
                >
                  <ExternalLink className="h-4 w-4" />
                  {link.name}
                </a>
              ))}
            </div>
          </div>

          <div>
            <p className="mb-4 text-[11px] font-semibold uppercase tracking-[2px] text-taupe">
              Studio
            </p>
            <p className="text-sm leading-relaxed text-warm-gray">{config.footerText}</p>
          </div>
        </div>
      </div>

      <div className="border-t border-charcoal/10 py-6 text-center">
        <p className="text-[11px] text-warm-gray">
          © {new Date().getFullYear()} {config.brandName}. {config.footerText}
        </p>
      </div>
    </footer>
  )
}
