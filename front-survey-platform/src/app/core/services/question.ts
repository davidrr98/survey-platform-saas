import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/enviroment';
import { ApiResponse } from '../models/api-response.model';
import { Question, QuestionFormData } from '../models/question.model';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  private base = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private url(surveyId: string, questionId?: string): string {
    const base = `${this.base}/api/v1/surveys/${surveyId}/questions`;
    return questionId ? `${base}/${questionId}` : base;
  }

  getAll(surveyId: string): Observable<ApiResponse<Question[]>> {
    return this.http.get<ApiResponse<Question[]>>(this.url(surveyId));
  }

  getById(surveyId: string, questionId: string): Observable<ApiResponse<Question>> {
    return this.http.get<ApiResponse<Question>>(this.url(surveyId, questionId));
  }

  create(surveyId: string, data: QuestionFormData): Observable<ApiResponse<Question>> {
    return this.http.post<ApiResponse<Question>>(this.url(surveyId), data);
  }

  update(surveyId: string, questionId: string, data: QuestionFormData): Observable<ApiResponse<Question>> {
    return this.http.put<ApiResponse<Question>>(this.url(surveyId, questionId), data);
  }

  delete(surveyId: string, questionId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(this.url(surveyId, questionId));
  }
}
