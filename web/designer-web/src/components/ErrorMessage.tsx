export function ErrorMessage({ message }: { message: string }) {
  return (
    <div className="flex min-h-[40vh] items-center justify-center px-6">
      <p className="text-center text-warm-gray">{message}</p>
    </div>
  )
}
