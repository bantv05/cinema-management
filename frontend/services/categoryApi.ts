import { apiClient, unwrap, type ApiResponse } from "@/lib/apiClient";

export interface Category {
  id: number;
  name: string;
}

export interface CategoryRequest {
  name: string;
}

export const categoryApiService = {
  getAll: () => unwrap(apiClient.get<ApiResponse<Category[]>>("/categories")),

  getById: (id: number) => unwrap(apiClient.get<ApiResponse<Category>>(`/categories/${id}`)),

  create: (body: CategoryRequest) => unwrap(apiClient.post<ApiResponse<Category>>("/categories", body)),

  update: (id: number, body: CategoryRequest) =>
    unwrap(apiClient.put<ApiResponse<Category>>(`/categories/${id}`, body)),

  remove: (id: number) => unwrap(apiClient.delete<ApiResponse<null>>(`/categories/${id}`)),
};
