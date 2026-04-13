type OutputBlockProps = {
  title: string
  value: string
  ariaLabel: string
  isError?: boolean
}

export function OutputBlock({
  title,
  value,
  ariaLabel,
  isError = false,
}: OutputBlockProps) {
  const toneClass = isError
    ? 'border-red-200 bg-red-50/50 text-red-900 focus:border-red-300 focus:ring-red-400/10'
    : 'border-neutral-200 bg-neutral-50/50 text-neutral-800 focus:border-neutral-400 focus:ring-neutral-400/10 focus:bg-white'

  return (
    <div className="flex flex-1 flex-col gap-2">
      <h3 className="text-sm font-medium tracking-tight text-neutral-700">{title}</h3>
      <textarea
        className={`min-h-35 w-full flex-1 resize-y rounded-xl border p-4 font-mono text-[13px] leading-relaxed transition-colors focus:outline-none focus:ring-4 ${toneClass}`}
        value={value}
        readOnly
        aria-label={ariaLabel}
      />
    </div>
  )
}