import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BaseApiService } from './base-api';
import { Survey, SurveyFormData } from '../models/survey.model';

@Injectable({
  providedIn: 'root'
})
export class SurveyService extends BaseApiService {
  protected override basePath = 'api/v1/surveys';

  constructor(http: HttpClient) {
    super(http);
  }

  getAllSurveys(): Observable<any> {    
    return this.get<Survey[]>();
  }

  getSurveyById(id: string): Observable<any> {
    return this.getById<Survey>(id);
  }

  createSurvey(survey: SurveyFormData): Observable<any> {
    return this.create<Survey>(survey);
  }

  updateSurvey(id: string, survey: SurveyFormData): Observable<any> {
    return this.update<Survey>(id, survey);
  }

  deleteSurvey(id: string): Observable<any> {
    return this.delete(id);
  }

  updateStatus(id: string, status: string): Observable<any> {
    return this.update<Survey>(id, { status });
  }
}