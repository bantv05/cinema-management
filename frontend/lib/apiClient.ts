import axios, { AxiosError } from "axios";

/** Cau truc ApiResponse chung cua backend (common/dto/ApiResponse.java). */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

/** Cau truc PageResponse cua backend (page bat dau tu 0). */
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export const apiClient = axios.create({
  baseURL: "/api/v1",
  headers: { "Content-Type": "application/json" },
});

/** Lay data tu ApiResponse de cac service chi tra ve du lieu nghiep vu. */
export async function unwrap<T>(request: Promise<{ data: ApiResponse<T> }>): Promise<T> {
  const res = await request;
  return res.data.data;
}

/**
 * Chuyen loi tu backend thanh thong bao hien thi.
 * Loi validation (400) co data la map field -> message, ghep lai thanh 1 chuoi.
 */
export function getErrorMessage(error: unknown, fallback = "Đã xảy ra lỗi"): string {
  if (error instanceof AxiosError) {
    const body = error.response?.data as Partial<ApiResponse<unknown>> | undefined;
    if (body?.data && typeof body.data === "object") {
      const fieldErrors = Object.values(body.data as Record<string, string>);
      if (fieldErrors.length > 0) return fieldErrors.join(", ");
    }
    if (body?.message) return body.message;
    if (!error.response) return "Không kết nối được tới server";
  }
  return fallback;
}
