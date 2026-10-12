"use client";

import { getErrorMessage, type PageResponse } from "@/lib/apiClient";
import { categoryApiService, type Category } from "@/services/categoryApi";
import {
  MOVIE_STATUS_LABEL,
  movieApiService,
  type Movie,
  type MovieRequest,
  type MovieStatus,
} from "@/services/movieApi";
import { Ban, Edit, Eye, Film, Loader2, Plus, Search } from "lucide-react";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import ConfirmDeleteDialog from "../../components/ConfirmDeleteDialog";
import Pagination from "../../components/Pagination";
import MovieModal, { type ModalMode } from "./components/MovieModal";

const PAGE_SIZE = 10; // backend gioi han size toi da 10

const STATUS_COLOR: Record<MovieStatus, string> = {
  COMING_SOON: "bg-blue-100 text-blue-800",
  NOW_SHOWING: "bg-green-100 text-green-800",
  ENDED: "bg-gray-200 text-gray-700",
};

const formatDate = (value: string | null) => (value ? new Date(value).toLocaleDateString("vi-VN") : "—");

const formatDuration = (minutes: number) => `${Math.floor(minutes / 60)}h ${minutes % 60}p`;

export default function MovieManagement() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [page, setPage] = useState(0);
  const [keywordInput, setKeywordInput] = useState("");
  const [keyword, setKeyword] = useState("");
  const [status, setStatus] = useState<MovieStatus | "">("");

  // Luu kem bo loc da tai; loading = bo loc hien tai chua co ket qua (tranh setState dong bo trong effect)
  const [reloadCount, setReloadCount] = useState(0);
  const queryKey = `${status}|${keyword}|${page}|${reloadCount}`;
  const [result, setResult] = useState<{ key: string; data: PageResponse<Movie> | null }>({ key: "", data: null });
  const loading = result.key !== queryKey;
  const data = result.data;

  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<ModalMode>("add");
  const [selectedMovie, setSelectedMovie] = useState<Movie | undefined>();
  const [movieToEnd, setMovieToEnd] = useState<Movie | null>(null);
  const [ending, setEnding] = useState(false);

  useEffect(() => {
    let ignore = false;
    movieApiService.getMovies({ status: status || undefined, keyword, page, size: PAGE_SIZE })
      .then((res) => { if (!ignore) setResult({ key: queryKey, data: res }); })
      .catch((error) => {
        if (ignore) return;
        toast.error(getErrorMessage(error, "Không thể tải danh sách phim"));
        setResult((prev) => ({ key: queryKey, data: prev.data }));
      });
    return () => { ignore = true; };
  }, [status, keyword, page, queryKey]);

  const fetchMovies = () => setReloadCount((n) => n + 1);

  useEffect(() => {
    categoryApiService.getAll()
      .then(setCategories)
      .catch((error) => toast.error(getErrorMessage(error, "Không thể tải thể loại")));
  }, []);

  // Debounce o tim kiem de khong goi API moi lan go phim
  useEffect(() => {
    const timer = setTimeout(() => {
      setKeyword(keywordInput.trim());
      setPage(0);
    }, 400);
    return () => clearTimeout(timer);
  }, [keywordInput]);

  const openModal = (mode: ModalMode, movie?: Movie) => {
    setModalMode(mode);
    setSelectedMovie(movie);
    setModalOpen(true);
  };

  // Loi duoc nem tiep de MovieModal hien thi ngay trong form
  const handleSubmit = async (body: MovieRequest) => {
    if (modalMode === "edit" && selectedMovie) {
      await movieApiService.update(selectedMovie.id, body);
      toast.success("Cập nhật phim thành công");
    } else {
      await movieApiService.create(body);
      toast.success("Thêm phim thành công");
    }
    setModalOpen(false);
    await fetchMovies();
  };

  const handleEndMovie = async () => {
    if (!movieToEnd) return;
    setEnding(true);
    try {
      await movieApiService.remove(movieToEnd.id);
      toast.success("Đã ngừng chiếu phim");
      setMovieToEnd(null);
      await fetchMovies();
    } catch (error) {
      toast.error(getErrorMessage(error, "Không thể ngừng chiếu phim"));
    } finally {
      setEnding(false);
    }
  };

  const movies = data?.content ?? [];

  return (
    <div className="space-y-6 p-6">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <Film className="h-7 w-7 text-indigo-600" aria-hidden />
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Quản lý phim</h1>
            <p className="text-sm text-gray-500">Tổng: {data?.totalElements ?? 0} phim</p>
          </div>
        </div>
        <button type="button" onClick={() => openModal("add")}
          className="flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700">
          <Plus className="h-4 w-4" aria-hidden /> Thêm phim
        </button>
      </div>

      <div className="flex flex-wrap gap-3 rounded-xl bg-white p-4 shadow-sm">
        <div className="relative min-w-60 flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" aria-hidden />
          <input
            aria-label="Tìm theo tên phim"
            placeholder="Tìm theo tên phim..."
            value={keywordInput}
            onChange={(e) => setKeywordInput(e.target.value)}
            className="w-full rounded-lg border border-gray-300 py-2 pl-9 pr-3 text-sm focus:border-indigo-500 focus:outline-none"
          />
        </div>
        <select
          aria-label="Lọc theo trạng thái"
          value={status}
          onChange={(e) => { setStatus(e.target.value as MovieStatus | ""); setPage(0); }}
          className="rounded-lg border border-gray-300 px-3 py-2 text-sm"
        >
          <option value="">Tất cả trạng thái</option>
          {Object.entries(MOVIE_STATUS_LABEL).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
      </div>

      <div className="overflow-x-auto rounded-xl bg-white shadow-sm">
        <table className="min-w-full divide-y divide-gray-200 text-sm">
          <thead className="bg-gray-50 text-left text-xs font-semibold uppercase text-gray-500">
            <tr>
              <th className="px-4 py-3">Phim</th>
              <th className="px-4 py-3">Thể loại</th>
              <th className="px-4 py-3">Thời lượng</th>
              <th className="px-4 py-3">Lịch chiếu</th>
              <th className="px-4 py-3">Đánh giá</th>
              <th className="px-4 py-3">Trạng thái</th>
              <th className="px-4 py-3 text-right">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr><td colSpan={7} className="py-12 text-center">
                <Loader2 className="mx-auto h-6 w-6 animate-spin text-indigo-600" aria-label="Đang tải" />
              </td></tr>
            ) : movies.length === 0 ? (
              <tr><td colSpan={7} className="py-12 text-center text-gray-500">Không có phim nào</td></tr>
            ) : movies.map((movie) => (
              <tr key={movie.id} className="hover:bg-gray-50">
                <td className="px-4 py-3">
                  <div className="flex items-center gap-3">
                    <div className="h-16 w-11 shrink-0 overflow-hidden rounded bg-gray-200">
                      {movie.posterUrl && (
                        // eslint-disable-next-line @next/next/no-img-element
                        <img src={movie.posterUrl} alt={movie.title} className="h-full w-full object-cover" />
                      )}
                    </div>
                    <div className="min-w-0">
                      <p className="max-w-56 truncate font-medium text-gray-900">{movie.title}</p>
                      <p className="text-xs text-gray-500">
                        {movie.director ?? "—"}{movie.ageRating && ` · ${movie.ageRating}`}
                      </p>
                    </div>
                  </div>
                </td>
                <td className="px-4 py-3">
                  <div className="flex max-w-48 flex-wrap gap-1">
                    {movie.categories.map((c) => (
                      <span key={c.id} className="rounded bg-indigo-50 px-2 py-0.5 text-xs text-indigo-700">{c.name}</span>
                    ))}
                  </div>
                </td>
                <td className="whitespace-nowrap px-4 py-3 text-gray-600">{formatDuration(movie.durationMinutes)}</td>
                <td className="whitespace-nowrap px-4 py-3 text-gray-600">
                  <p>{formatDate(movie.releaseDate)}</p>
                  <p className="text-xs text-gray-400">đến {formatDate(movie.endDate)}</p>
                </td>
                <td className="px-4 py-3 text-gray-600">⭐ {movie.averageRating}/5</td>
                <td className="px-4 py-3">
                  <span className={`whitespace-nowrap rounded-full px-2.5 py-1 text-xs font-medium ${STATUS_COLOR[movie.status]}`}>
                    {MOVIE_STATUS_LABEL[movie.status]}
                  </span>
                </td>
                <td className="px-4 py-3">
                  <div className="flex justify-end gap-1">
                    <button type="button" title="Xem" aria-label={`Xem ${movie.title}`} onClick={() => openModal("view", movie)}
                      className="rounded p-1.5 text-gray-600 hover:bg-gray-100"><Eye className="h-4 w-4" /></button>
                    <button type="button" title="Sửa" aria-label={`Sửa ${movie.title}`} onClick={() => openModal("edit", movie)}
                      className="rounded p-1.5 text-blue-600 hover:bg-blue-50"><Edit className="h-4 w-4" /></button>
                    <button type="button" title="Ngừng chiếu" aria-label={`Ngừng chiếu ${movie.title}`}
                      disabled={movie.status === "ENDED"} onClick={() => setMovieToEnd(movie)}
                      className="rounded p-1.5 text-red-600 hover:bg-red-50 disabled:opacity-30"><Ban className="h-4 w-4" /></button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {data && <Pagination page={data.page} totalPages={data.totalPages} onPageChange={setPage} />}

      {modalOpen && (
        <MovieModal
          key={`${modalMode}-${selectedMovie?.id ?? "new"}`}
          mode={modalMode}
          movie={selectedMovie}
          categories={categories}
          onClose={() => setModalOpen(false)}
          onSubmit={handleSubmit}
        />
      )}

      <ConfirmDeleteDialog
        open={movieToEnd !== null}
        title="Ngừng chiếu phim"
        message={`Chuyển phim "${movieToEnd?.title}" sang trạng thái Ngừng chiếu? Dữ liệu suất chiếu cũ vẫn được giữ lại.`}
        confirmText="Ngừng chiếu"
        loading={ending}
        onConfirm={handleEndMovie}
        onClose={() => setMovieToEnd(null)}
      />
    </div>
  );
}
