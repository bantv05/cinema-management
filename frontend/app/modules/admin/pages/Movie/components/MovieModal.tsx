"use client";

import { getErrorMessage } from "@/lib/apiClient";
import type { Category } from "@/services/categoryApi";
import { MOVIE_STATUS_LABEL, type Movie, type MovieRequest, type MovieStatus } from "@/services/movieApi";
import CheckBoxIcon from "@mui/icons-material/CheckBox";
import CheckBoxOutlineBlankIcon from "@mui/icons-material/CheckBoxOutlineBlank";
import Autocomplete from "@mui/material/Autocomplete";
import Checkbox from "@mui/material/Checkbox";
import TextField from "@mui/material/TextField";
import { Loader2, X } from "lucide-react";
import React, { useState } from "react";

export type ModalMode = "add" | "edit" | "view";

/** Parent chi render khi mo va dat key theo mode + movie de form duoc khoi tao lai. */
interface MovieModalProps {
  mode: ModalMode;
  movie?: Movie;
  categories: Category[];
  onClose: () => void;
  onSubmit: (body: MovieRequest) => Promise<void>;
}

const AGE_RATINGS = ["P", "K", "T13", "T16", "T18"];

interface FormState {
  title: string;
  description: string;
  durationMinutes: string;
  releaseDate: string;
  endDate: string;
  posterUrl: string;
  trailerUrl: string;
  director: string;
  castList: string;
  language: string;
  subtitle: string;
  ageRating: string;
  status: MovieStatus;
  categories: Category[];
}

const emptyForm: FormState = {
  title: "",
  description: "",
  durationMinutes: "",
  releaseDate: "",
  endDate: "",
  posterUrl: "",
  trailerUrl: "",
  director: "",
  castList: "",
  language: "",
  subtitle: "",
  ageRating: "",
  status: "COMING_SOON",
  categories: [],
};

function toForm(movie: Movie): FormState {
  return {
    title: movie.title,
    description: movie.description ?? "",
    durationMinutes: String(movie.durationMinutes),
    releaseDate: movie.releaseDate,
    endDate: movie.endDate ?? "",
    posterUrl: movie.posterUrl ?? "",
    trailerUrl: movie.trailerUrl ?? "",
    director: movie.director ?? "",
    castList: movie.castList ?? "",
    language: movie.language ?? "",
    subtitle: movie.subtitle ?? "",
    ageRating: movie.ageRating ?? "",
    status: movie.status,
    categories: movie.categories,
  };
}

/** Chuoi rong -> null de backend luu NULL thay vi "". */
const orNull = (value: string) => (value.trim() === "" ? null : value.trim());


function validate(form: FormState): Record<string, string> {
  const errors: Record<string, string> = {};
  if (!form.title.trim()) errors.title = "Tên phim không được để trống";
  const duration = Number(form.durationMinutes);
  if (!Number.isInteger(duration) || duration <= 0) errors.durationMinutes = "Thời lượng phải là số nguyên > 0";
  if (!form.releaseDate) errors.releaseDate = "Ngày khởi chiếu không được để trống";
  if (form.endDate && form.releaseDate && form.endDate < form.releaseDate)
    errors.endDate = "Ngày ngừng chiếu phải sau hoặc bằng ngày khởi chiếu";
  if (form.categories.length === 0) errors.categories = "Phim phải có ít nhất 1 thể loại";
  return errors;
}

const inputClass =
  "w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 disabled:bg-gray-50";

function Field({ label, error, required, children }: {
  label: string;
  error?: string;
  required?: boolean;
  children: React.ReactNode;
}) {
  return (
    <label className="block">
      <span className="mb-1 block text-sm font-medium text-gray-700">
        {label} {required && <span className="text-red-500">*</span>}
      </span>
      {children}
      {error && <span className="mt-1 block text-xs text-red-600">{error}</span>}
    </label>
  );
}

export default function MovieModal({ mode, movie, categories, onClose, onSubmit }: MovieModalProps) {
  const [form, setForm] = useState<FormState>(() => (movie && mode !== "add" ? toForm(movie) : emptyForm));
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitError, setSubmitError] = useState("");
  const [saving, setSaving] = useState(false);
  const readOnly = mode === "view";

  const set = <K extends keyof FormState>(key: K, value: FormState[K]) =>
    setForm((prev) => ({ ...prev, [key]: value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const found = validate(form);
    setErrors(found);
    if (Object.keys(found).length > 0) return;

    setSaving(true);
    setSubmitError("");
    try {
      await onSubmit({
        title: form.title.trim(),
        description: orNull(form.description),
        durationMinutes: Number(form.durationMinutes),
        releaseDate: form.releaseDate,
        endDate: form.endDate || null,
        posterUrl: orNull(form.posterUrl),
        trailerUrl: orNull(form.trailerUrl),
        director: orNull(form.director),
        castList: orNull(form.castList),
        language: orNull(form.language),
        subtitle: orNull(form.subtitle),
        ageRating: form.ageRating || null,
        status: form.status,
        categoryIds: form.categories.map((c) => c.id),
      });
    } catch (error) {
      setSubmitError(getErrorMessage(error, "Không thể lưu phim"));
    } finally {
      setSaving(false);
    }
  };

  const title = mode === "add" ? "Thêm phim" : mode === "edit" ? "Sửa phim" : "Chi tiết phim";

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/50 p-4">
      <div role="dialog" aria-modal="true" aria-labelledby="movie-modal-title"
        className="flex max-h-[90vh] w-full max-w-3xl flex-col rounded-xl bg-white shadow-xl">
        <div className="flex items-center justify-between border-b px-6 py-4">
          <h2 id="movie-modal-title" className="text-lg font-semibold text-gray-900">{title}</h2>
          <button type="button" onClick={onClose} aria-label="Đóng" className="rounded p-1 hover:bg-gray-100">
            <X className="h-5 w-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto px-6 py-4" noValidate>
          <fieldset disabled={readOnly || saving} className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div className="md:col-span-2">
              <Field label="Tên phim" required error={errors.title}>
                <input className={inputClass} value={form.title} maxLength={255}
                  onChange={(e) => set("title", e.target.value)} />
              </Field>
            </div>

            <Field label="Thời lượng (phút)" required error={errors.durationMinutes}>
              <input type="number" min={1} className={inputClass} value={form.durationMinutes}
                onChange={(e) => set("durationMinutes", e.target.value)} />
            </Field>

            <Field label="Trạng thái">
              <select className={inputClass} value={form.status}
                onChange={(e) => set("status", e.target.value as MovieStatus)}>
                {Object.entries(MOVIE_STATUS_LABEL).map(([key, label]) => (
                  <option key={key} value={key}>{label}</option>
                ))}
              </select>
            </Field>

            <Field label="Ngày khởi chiếu" required error={errors.releaseDate}>
              <input type="date" className={inputClass} value={form.releaseDate}
                onChange={(e) => set("releaseDate", e.target.value)} />
            </Field>

            <Field label="Ngày ngừng chiếu" error={errors.endDate}>
              <input type="date" className={inputClass} value={form.endDate} min={form.releaseDate || undefined}
                onChange={(e) => set("endDate", e.target.value)} />
            </Field>

            <div className="md:col-span-2">
              <Field label="Thể loại" required error={errors.categories}>
                <Autocomplete
                  multiple
                  size="small"
                  disableCloseOnSelect
                  disabled={readOnly || saving}
                  options={categories}
                  value={form.categories}
                  getOptionLabel={(option) => option.name}
                  isOptionEqualToValue={(a, b) => a.id === b.id}
                  onChange={(_, value) => set("categories", value)}
                  renderOption={(props, option, { selected }) => {
                    const { key, ...rest } = props;
                    return (
                      <li key={key} {...rest}>
                        <Checkbox
                          icon={<CheckBoxOutlineBlankIcon fontSize="small" />}
                          checkedIcon={<CheckBoxIcon fontSize="small" />}
                          checked={selected}
                          sx={{ mr: 1 }}
                        />
                        {option.name}
                      </li>
                    );
                  }}
                  renderInput={(params) => <TextField {...params} placeholder="Chọn thể loại" />}
                />
              </Field>
            </div>

            <Field label="Đạo diễn">
              <input className={inputClass} value={form.director} maxLength={255}
                onChange={(e) => set("director", e.target.value)} />
            </Field>

            <Field label="Phân loại độ tuổi">
              <select className={inputClass} value={form.ageRating} onChange={(e) => set("ageRating", e.target.value)}>
                <option value="">-- Chọn --</option>
                {AGE_RATINGS.map((age) => <option key={age} value={age}>{age}</option>)}
              </select>
            </Field>

            <Field label="Ngôn ngữ">
              <input className={inputClass} value={form.language} maxLength={255}
                onChange={(e) => set("language", e.target.value)} />
            </Field>

            <Field label="Phụ đề">
              <input className={inputClass} value={form.subtitle} maxLength={255}
                onChange={(e) => set("subtitle", e.target.value)} />
            </Field>

            <div className="md:col-span-2">
              <Field label="Diễn viên">
                <input className={inputClass} value={form.castList} placeholder="Ngăn cách bằng dấu phẩy"
                  onChange={(e) => set("castList", e.target.value)} />
              </Field>
            </div>

            <Field label="Poster URL">
              <input type="url" className={inputClass} value={form.posterUrl} maxLength={255}
                onChange={(e) => set("posterUrl", e.target.value)} />
            </Field>

            <Field label="Trailer URL (YouTube)">
              <input type="url" className={inputClass} value={form.trailerUrl} maxLength={255}
                onChange={(e) => set("trailerUrl", e.target.value)} />
            </Field>

            {form.posterUrl && (
              <div className="md:col-span-2">
                {/* eslint-disable-next-line @next/next/no-img-element */}
                <img src={form.posterUrl} alt="Xem trước poster" className="h-48 rounded-lg object-cover" />
              </div>
            )}

            <div className="md:col-span-2">
              <Field label="Mô tả">
                <textarea rows={4} className={inputClass} value={form.description}
                  onChange={(e) => set("description", e.target.value)} />
              </Field>
            </div>

            {mode !== "add" && movie && (
              <p className="text-sm text-gray-600 md:col-span-2">
                Điểm đánh giá trung bình: <strong>{movie.averageRating}</strong>/5 (tính tự động từ review)
              </p>
            )}
          </fieldset>

          <div className="mt-6 flex justify-end gap-2 border-t pt-4">
            <button type="button" onClick={onClose}
              className="rounded-lg border border-gray-300 px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">
              {readOnly ? "Đóng" : "Hủy"}
            </button>
            {!readOnly && (
              <button type="submit" disabled={saving}
                className="flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2 text-sm text-white hover:bg-indigo-700 disabled:opacity-50">
                {saving && <Loader2 className="h-4 w-4 animate-spin" aria-hidden />}
                {mode === "add" ? "Thêm phim" : "Lưu thay đổi"}
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}
