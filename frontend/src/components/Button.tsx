import { ButtonHTMLAttributes } from "react";
import { cn } from "../utils";

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: "primary" | "secondary" | "ghost";
};

export function Button({ className, variant = "primary", ...props }: Props) {
  const styles = {
    primary: "bg-ink text-white hover:bg-ocean",
    secondary: "bg-white text-ink border border-ink/10 hover:border-ocean/50",
    ghost: "text-ink hover:bg-ink/5",
  };

  return (
    <button
      className={cn(
        "inline-flex h-10 items-center justify-center gap-2 rounded-md px-4 text-sm font-semibold transition disabled:cursor-not-allowed disabled:opacity-50",
        styles[variant],
        className,
      )}
      {...props}
    />
  );
}
