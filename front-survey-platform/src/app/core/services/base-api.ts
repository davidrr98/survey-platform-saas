import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/enviroment';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export abstract class BaseApiService {
  protected abstract basePath: string;
  protected apiUrl = environment.apiUrl;

  constructor(protected http: HttpClient) {}

  protected get<T>(path: string = '', params?: HttpParams): Observable<ApiResponse<T>> {
    const url = path ? `${this.apiUrl}/${this.basePath}/${path}` : `${this.apiUrl}/${this.basePath}`;    
    return this.http.get<ApiResponse<T>>(url, { params });
  }

  protected getById<T>(id: string): Observable<ApiResponse<T>> {
    const url = `${this.apiUrl}/${this.basePath}/${id}`;
    return this.http.get<ApiResponse<T>>(url);
  }

  protected create<T>(data: any, path: string = ''): Observable<ApiResponse<T>> {
    const url = path ? `${this.apiUrl}/${this.basePath}/${path}` : `${this.apiUrl}/${this.basePath}`;
    return this.http.post<ApiResponse<T>>(url, data);
  }

  protected update<T>(id: string, data: any): Observable<ApiResponse<T>> {
    const url = `${this.apiUrl}/${this.basePath}/${id}`;
    return this.http.put<ApiResponse<T>>(url, data);
  }

  protected delete(id: string): Observable<ApiResponse<void>> {
    const url = `${this.apiUrl}/${this.basePath}/${id}`;
    return this.http.delete<ApiResponse<void>>(url);
  }
}