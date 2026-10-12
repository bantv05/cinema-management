"use client";

import { ChevronLeft, ChevronRight } from "lucide-react";

interface PaginationProps {
  /** Trang hien tai, bat dau tu 0 (giong backend). */
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export default function Pagination({ page, totalPages, onPageChange }: PaginationProps) {
  if (totalPages <= 1) return null;

  return (
    <nav aria-label="Phân trang" className="flex items-center justify-center gap-1">
      <button
        type="button"
        aria-label="Trang trước"
        disabled={page === 0}
        onClick={() => onPageChange(page - 1)}
        className="rounded-lg border border-gray-300 p-2 hover:bg-gray-50 disabled:opacity-40"
      >
        <ChevronLeft className="h-4 w-4" />
      </button>
      {Array.from({ length: totalPages }, (_, i) => (
        <button
          key={i}
          type="button"
          aria-current={i === page ? "page" : undefined}
          onClick={() => onPageChange(i)}
          className={`min-w-9 rounded-lg border px-3 py-1.5 text-sm ${
            i === page ? "border-indigo-600 bg-indigo-600 text-white" : "border-gray-300 hover:bg-gray-50"
          }`}
        >
          {i + 1}
        </button>
      ))}
      <button
        type="button"
        aria-label="Trang sau"
        disabled={page >= totalPages - 1}
        onClick={() => onPageChange(page + 1)}
        className="rounded-lg border border-gray-300 p-2 hover:bg-gray-50 disabled:opacity-40"
      >
        <ChevronRight className="h-4 w-4" />
      </button>
    </nav>
  );
}
