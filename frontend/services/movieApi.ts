import { apiClient, unwrap, type ApiResponse, type PageResponse } from "@/lib/apiClient";
import type { Category } from "./categoryApi";

export type MovieStatus = "COMING_SOON" | "NOW_SHOWING" | "ENDED";

export const MOVIE_STATUS_LABEL: Record<MovieStatus, string> = {
  COMING_SOON: "Sắp chiếu",
  NOW_SHOWING: "Đang chiếu",
  ENDED: "Ngừng chiếu",
};

/** Khop voi MovieResponse.java */
export interface Movie {
  id: number;
  title: string;
  description: string | null;
  durationMinutes: number;
  releaseDate: string; // yyyy-MM-dd
  endDate: string | null;
  posterUrl: string | null;
  trailerUrl: string | null;
  director: string | null;
  castList: string | null;
  language: string | null;
  subtitle: string | null;
  ageRating: string | null;
  averageRating: number;
  status: MovieStatus;
  categories: Category[];
}

/** Khop voi MovieRequest.java */
export interface MovieRequest {
  title: string;
  description?: string | null;
  durationMinutes: number;
  releaseDate: string;
  endDate?: string | null;
  posterUrl?: string | null;
  trailerUrl?: string | null;
  director?: string | null;
  castList?: string | null;
  language?: string | null;
  subtitle?: string | null;
  ageRating?: string | null;
  status?: MovieStatus | null;
  categoryIds: number[];
}

export interface MovieQuery {
  status?: MovieStatus;
  keyword?: string;
  page?: number; // bat dau tu 0
  size?: number; // backend gioi han toi da 10
}

export const movieApiService = {
  getMovies: (query: MovieQuery = {}) =>
    unwrap(
      apiClient.get<ApiResponse<PageResponse<Movie>>>("/movies", {
        params: {
          status: query.status || undefined,
          keyword: query.keyword?.trim() || undefined,
          page: query.page ?? 0,
          size: query.size ?? 10,
        },
      }),
    ),

  getById: (id: number) => unwrap(apiClient.get<ApiResponse<Movie>>(`/movies/${id}`)),

  create: (body: MovieRequest) => unwrap(apiClient.post<ApiResponse<Movie>>("/movies", body)),

  update: (id: number, body: MovieRequest) => unwrap(apiClient.put<ApiResponse<Movie>>(`/movies/${id}`, body)),

  /** Backend xoa mem: chuyen status sang ENDED. */
  remove: (id: number) => unwrap(apiClient.delete<ApiResponse<null>>(`/movies/${id}`)),
};
