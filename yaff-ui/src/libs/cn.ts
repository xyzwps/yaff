import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export type { ClassValue } from "clsx";

export default function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}
