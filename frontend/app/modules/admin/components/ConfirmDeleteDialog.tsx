"use client";

import { Loader2, TriangleAlert } from "lucide-react";

interface ConfirmDeleteDialogProps {
  open: boolean;
  title?: string;
  message: string;
  confirmText?: string;
  loading?: boolean;
  onConfirm: () => void;
  onClose: () => void;
}

export default function ConfirmDeleteDialog({
  open,
  title = "Xác nhận",
  message,
  confirmText = "Xóa",
  loading = false,
  onConfirm,
  onClose,
}: ConfirmDeleteDialogProps) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4" onClick={onClose}>
      <div
        role="alertdialog"
        aria-modal="true"
        aria-labelledby="confirm-title"
        className="w-full max-w-md rounded-xl bg-white p-6 shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-start gap-3">
          <TriangleAlert className="mt-0.5 h-6 w-6 shrink-0 text-red-500" aria-hidden />
          <div>
            <h2 id="confirm-title" className="text-lg font-semibold text-gray-900">{title}</h2>
            <p className="mt-1 text-sm text-gray-600">{message}</p>
          </div>
        </div>
        <div className="mt-6 flex justify-end gap-2">
          <button
            type="button"
            onClick={onClose}
            disabled={loading}
            className="rounded-lg border border-gray-300 px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 disabled:opacity-50"
          >
            Hủy
          </button>
          <button
            type="button"
            onClick={onConfirm}
            disabled={loading}
            className="flex items-center gap-2 rounded-lg bg-red-600 px-4 py-2 text-sm text-white hover:bg-red-700 disabled:opacity-50"
          >
            {loading && <Loader2 className="h-4 w-4 animate-spin" aria-hidden />}
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
}
