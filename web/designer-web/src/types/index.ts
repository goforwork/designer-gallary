export interface SocialLink {
  name: string
  url: string
}

export interface HeroConfig {
  eyebrow: string
  titleLine: string
  titleEmphasis: string
  subtitleLine1: string
  subtitleLine2: string
  backgroundMedia: string
  ctaText: string
  ctaTarget: string
}

export interface SiteConfig {
  siteTitle: string
  siteDescription: string
  brandName: string
  contactEmail: string
  contactPhone: string
  socialLinks: SocialLink[]
  footerText: string
  heroConfig: HeroConfig
}

export interface Category {
  id: string
  name: string
  slug: string
  description: string
  sortOrder: number
  isVisible: boolean
}

export interface Work {
  id: string
  title: string
  slug: string
  categoryId: string | null
  coverImage: string | null
  images: string[]
  description: string
  tags: string[]
  sortOrder: number
  isVisible: boolean
  createdAt: string
  updatedAt: string
}
