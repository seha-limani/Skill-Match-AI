import { ButtonHTMLAttributes } from "react";
import { cn } from "../utils";

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: "primary" | "secondary" | "ghost";
};

export function Button({ className, variant = "primary", ...props }: Props) {
  const styles = {
    primary: "bg-slate-950 text-white shadow-[0_12px_30px_rgba(15,23,42,0.18)] hover:-translate-y-0.5 hover:bg-slate-800",
    secondary: "bg-white text-slate-900 border border-slate-200 shadow-sm hover:-translate-y-0.5 hover:border-sky-300 hover:shadow-md",
    ghost: "text-slate-700 hover:bg-slate-100",
  };

  return (
    <button
      className={cn(
        "inline-flex h-11 items-center justify-center gap-2 rounded-full px-5 text-sm font-semibold transition duration-200 disabled:cursor-not-allowed disabled:opacity-50",
        styles[variant],
        className,
      )}
      {...props}
    />
  );
}
