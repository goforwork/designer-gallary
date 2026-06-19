import { Loader2 } from 'lucide-react'

export function Loading() {
  return (
    <div className="flex min-h-[40vh] items-center justify-center">
      <Loader2 className="h-8 w-8 animate-spin text-taupe" />
    </div>
  )
}
