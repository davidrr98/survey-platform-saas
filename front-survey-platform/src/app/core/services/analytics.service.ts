import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../env/enviroment';
import { ApiResponse } from '../models/api-response.model';
import { SurveyResults } from '../models/analytics.model';

export type SseEvent =
  | { type: 'data'; payload: SurveyResults }
  | { type: 'connected' }
  | { type: 'disconnected' };

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  private base = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getResults(surveyId: string): Observable<SurveyResults> {
    return this.http
      .get<ApiResponse<SurveyResults>>(`${this.base}/api/v1/surveys/${surveyId}/analytics`)
      .pipe(map(res => res.data));
  }

  streamResults(surveyId: string): Observable<SseEvent> {
    return new Observable(observer => {
      const source = new EventSource(
        `${this.base}/api/v1/surveys/${surveyId}/analytics/stream`
      );

      source.onopen = () => {
        observer.next({ type: 'connected' });
      };

      source.addEventListener('survey-update', (event: MessageEvent) => {
        try {
          observer.next({ type: 'data', payload: JSON.parse(event.data) });
        } catch {
          // ignorar eventos mal formados
        }
      });

      // No cerramos aquí — EventSource hace auto-reconexión por diseño.
      // Solo notificamos el estado para que el componente actualice el indicador.
      source.onerror = () => {
        observer.next({ type: 'disconnected' });
      };

      // El cierre real ocurre solo al desuscribirse (ngOnDestroy)
      return () => source.close();
    });
  }
}
