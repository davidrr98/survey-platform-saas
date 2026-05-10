import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Survey } from '../../../core/models/survey.model';
import { ApiResponse } from '../../../core/models/api-response.model';
import { environment } from '../../../../env/enviroment';

@Component({
  selector: 'app-public-survey-list',
  standalone: false,
  templateUrl: './public-survey-list.html',
  styleUrl: './public-survey-list.css',
})
export class PublicSurveyList implements OnInit {
  surveys: Survey[] = [];
  loading = true;
  error = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.http
      .get<ApiResponse<Survey[]>>(`${environment.apiUrl}/api/v1/public/surveys`)
      .subscribe({
        next: (res) => {
          this.surveys = res.data ?? [];
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = true;
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  open(id: string): void {
    this.router.navigate(['/public/survey', id]);
  }
}
