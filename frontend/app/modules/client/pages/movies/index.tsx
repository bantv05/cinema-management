"use client";

import { getErrorMessage, type PageResponse } from "@/lib/apiClient";
import { movieApiService, type Movie } from "@/services/movieApi";
import { Clock, Loader2, Play, Star, X } from "lucide-react";
import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import Pagination from "../../../admin/components/Pagination";

type Tab = "NOW_SHOWING" | "COMING_SOON";

const TABS: { key: Tab; label: string }[] = [
  { key: "NOW_SHOWING", label: "Phim đang chiếu" },
  { key: "COMING_SOON", label: "Phim sắp chiếu" },
];

/** Lay id video tu cac dang link YouTube pho bien (watch?v=, youtu.be/, embed/). */
function getYoutubeId(url: string): string | null {
  const match = url.match(/(?:youtu\.be\/|v=|embed\/|shorts\/)([\w-]{11})/);
  return match ? match[1] : null;
}

export default function MoviesPage() {
  const [tab, setTab] = useState<Tab>("NOW_SHOWING");
  const [page, setPage] = useState(0);
  // Ket qua gan kem tab/page da tai; loading = tab/page hien tai chua co ket qua
  const queryKey = `${tab}|${page}`;
  const [result, setResult] = useState<{ key: string; data: PageResponse<Movie> | null; error: string }>(
    { key: "", data: null, error: "" },
  );
  const loading = result.key !== queryKey;
  const { data, error } = result;
  const [trailerId, setTrailerId] = useState<string | null>(null);

  useEffect(() => {
    let ignore = false;
    movieApiService.getMovies({ status: tab, page, size: 10 })
      .then((res) => { if (!ignore) setResult({ key: queryKey, data: res, error: "" }); })
      .catch((err) => {
        if (!ignore) setResult({ key: queryKey, data: null, error: getErrorMessage(err, "Không thể tải danh sách phim") });
      });
    return () => { ignore = true; };
  }, [tab, page, queryKey]);

  const movies = data?.content ?? [];

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <div role="tablist" className="mb-8 flex justify-center gap-2">
        {TABS.map((t) => (
          <button
            key={t.key}
            role="tab"
            type="button"
            aria-selected={tab === t.key}
            onClick={() => { setTab(t.key); setPage(0); }}
            className={`rounded-full px-5 py-2 text-sm font-medium transition ${
              tab === t.key ? "bg-indigo-600 text-white" : "bg-gray-100 text-gray-700 hover:bg-gray-200"
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      {loading ? (
        <Loader2 className="mx-auto h-8 w-8 animate-spin text-indigo-600" aria-label="Đang tải" />
      ) : error ? (
        <p role="alert" className="text-center text-red-600">{error}</p>
      ) : movies.length === 0 ? (
        <p className="text-center text-gray-500">Chưa có phim nào</p>
      ) : (
        <ul className="grid grid-cols-2 gap-6 sm:grid-cols-3 lg:grid-cols-5">
          {movies.map((movie) => {
            const youtubeId = movie.trailerUrl ? getYoutubeId(movie.trailerUrl) : null;
            return (
              <li key={movie.id} className="group">
                <div className="relative aspect-[2/3] overflow-hidden rounded-xl bg-gray-200">
                  {movie.posterUrl && (
                    // eslint-disable-next-line @next/next/no-img-element
                    <img src={movie.posterUrl} alt={movie.title}
                      className="h-full w-full object-cover transition group-hover:scale-105" />
                  )}
                  {movie.ageRating && (
                    <span className="absolute left-2 top-2 rounded bg-orange-500 px-1.5 py-0.5 text-xs font-bold text-white">
                      {movie.ageRating}
                    </span>
                  )}
                  {youtubeId && (
                    <button
                      type="button"
                      aria-label={`Xem trailer ${movie.title}`}
                      onClick={() => setTrailerId(youtubeId)}
                      className="absolute inset-0 flex items-center justify-center bg-black/40 opacity-0 transition group-hover:opacity-100 focus:opacity-100"
                    >
                      <Play className="h-12 w-12 text-white" />
                    </button>
                  )}
                </div>
                <h3 className="mt-2 line-clamp-2 font-semibold text-gray-900">{movie.title}</h3>
                <p className="text-xs text-gray-500">{movie.categories.map((c) => c.name).join(", ")}</p>
                <div className="mt-1 flex items-center gap-3 text-xs text-gray-600">
                  <span className="flex items-center gap-1"><Clock className="h-3 w-3" aria-hidden />{movie.durationMinutes} phút</span>
                  <span className="flex items-center gap-1"><Star className="h-3 w-3 text-yellow-500" aria-hidden />{movie.averageRating}</span>
                </div>
              </li>
            );
          })}
        </ul>
      )}

      {data && <div className="mt-8"><Pagination page={data.page} totalPages={data.totalPages} onPageChange={setPage} /></div>}

      {trailerId && createPortal(
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 p-4" onClick={() => setTrailerId(null)}>
          <div className="relative aspect-video w-full max-w-4xl" onClick={(e) => e.stopPropagation()}>
            <iframe
              className="h-full w-full rounded-xl"
              src={`https://www.youtube.com/embed/${trailerId}?autoplay=1`}
              title="Trailer"
              allow="autoplay; encrypted-media"
              allowFullScreen
            />
            <button type="button" aria-label="Đóng trailer" onClick={() => setTrailerId(null)}
              className="absolute -right-3 -top-3 rounded-full bg-white p-1.5 shadow">
              <X className="h-5 w-5" />
            </button>
          </div>
        </div>,
        document.body,
      )}
    </div>
  );
}
