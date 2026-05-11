import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { SurveyResults, QuestionResult } from '../../../core/models/analytics.model';

@Component({
  selector: 'app-analytics-dashboard',
  standalone: false,
  templateUrl: './analytics-dashboard.html',
  styleUrl: './analytics-dashboard.css',
})
export class AnalyticsDashboard implements OnInit, OnDestroy {
  results: SurveyResults | null = null;
  surveyId: string | null = null;
  loading = true;
  error = false;
  connected = false;

  private streamSub?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private analyticsService: AnalyticsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.surveyId = this.route.snapshot.paramMap.get('surveyId');
    if (!this.surveyId) {
      this.error = true;
      this.loading = false;
      return;
    }
    this.loadSnapshot();
  }

  private loadSnapshot(): void {
    this.analyticsService.getResults(this.surveyId!).subscribe({
      next: (data) => {
        this.results = data;
        this.loading = false;
        this.cdr.detectChanges();
        this.subscribeToStream();
      },
      error: () => {
        this.error = true;
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  private subscribeToStream(): void {
    this.streamSub = this.analyticsService.streamResults(this.surveyId!).subscribe({
      next: (event) => {
        if (event.type === 'connected') {
          this.connected = true;
        } else if (event.type === 'disconnected') {
          this.connected = false;
        } else if (event.type === 'data') {
          this.results = event.payload;
          this.connected = true;
        }
        this.cdr.detectChanges();
      },
    });
  }

  hasOptions(q: QuestionResult): boolean {
    return q.optionResults?.length > 0;
  }

  isOpenType(q: QuestionResult): boolean {
    return q.type === 'TEXT';
  }

  isNumeric(q: QuestionResult): boolean {
    return q.type === 'NUMERIC';
  }

  isRating(q: QuestionResult): boolean {
    return q.type === 'RATING';
  }

  goBack(): void {
    this.router.navigate(['/analytics']);
  }

  ngOnDestroy(): void {
    this.streamSub?.unsubscribe();
  }
}
