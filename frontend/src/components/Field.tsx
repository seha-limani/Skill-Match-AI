import { InputHTMLAttributes, TextareaHTMLAttributes } from "react";

type InputProps = InputHTMLAttributes<HTMLInputElement> & { label: string };
type TextAreaProps = TextareaHTMLAttributes<HTMLTextAreaElement> & { label: string };

export function Field({ label, ...props }: InputProps) {
  return (
    <label className="grid gap-1.5 text-sm font-medium text-ink">
      {label}
      <input
        className="h-10 rounded-md border border-ink/15 bg-white px-3 text-sm outline-none transition focus:border-ocean focus:ring-2 focus:ring-ocean/15"
        {...props}
      />
    </label>
  );
}

export function TextArea({ label, ...props }: TextAreaProps) {
  return (
    <label className="grid gap-1.5 text-sm font-medium text-ink">
      {label}
      <textarea
        className="min-h-28 rounded-md border border-ink/15 bg-white px-3 py-2 text-sm outline-none transition focus:border-ocean focus:ring-2 focus:ring-ocean/15"
        {...props}
      />
    </label>
  );
}
