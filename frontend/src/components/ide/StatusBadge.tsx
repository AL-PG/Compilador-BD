import type { StatusTone } from '../../types/compiler'

type StatusBadgeProps = {
  tone: StatusTone
  text: string
}

const toneMap: Record<StatusTone, string> = {
  idle: 'border-neutral-200 bg-neutral-100 text-neutral-600',
  ok: 'border-emerald-200 bg-emerald-50 text-emerald-700',
  warn: 'border-red-200 bg-red-50 text-red-700',
  busy: 'border-blue-200 bg-blue-50 text-blue-700',
}

export function StatusBadge({ tone, text }: StatusBadgeProps) {
  return (
    <span
      className={`inline-flex items-center justify-center whitespace-nowrap rounded-full border px-3 py-1 text-xs font-medium tracking-wide transition-colors ${toneMap[tone]}`}
    >
      {text}
    </span>
  )
}