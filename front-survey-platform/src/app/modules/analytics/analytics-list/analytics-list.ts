import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SurveyService } from '../../../core/services/survey';
import { Survey } from '../../../core/models/survey.model';
import { SurveyStatus } from '../../../core/models/enums/survey-status.enum';

@Component({
  selector: 'app-analytics-list',
  standalone: false,
  templateUrl: './analytics-list.html',
  styleUrl: './analytics-list.css',
})
export class AnalyticsList implements OnInit {
  active: Survey[] = [];
  closed: Survey[] = [];
  loading = true;
  error = false;

  constructor(
    private surveyService: SurveyService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.surveyService.getAllSurveys().subscribe({
      next: (res: any) => {
        const all: Survey[] = res?.data ?? [];
        this.active = all.filter(s => s.status === SurveyStatus.ACTIVE);
        this.closed = all.filter(s => s.status === SurveyStatus.CLOSED);
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

  openAnalytics(surveyId: string): void {
    this.router.navigate(['/analytics', surveyId]);
  }
}
