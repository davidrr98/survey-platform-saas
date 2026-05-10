export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  timestamp: string;
}

export interface PaginatedResponse<T> {
  success: boolean;
  data: T[];
  message: string;
  timestamp: string;
  totalElements?: number;
  totalPages?: number;
}