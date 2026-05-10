import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SurveyService } from '../../../core/services/survey';
import { Survey } from '../../../core/models/survey.model';
import { SurveyStatus } from '../../../core/models/enums/survey-status.enum';

@Component({
  selector: 'app-survey-list',
  standalone: false,
  templateUrl: './survey-list.html',
  styleUrl: './survey-list.css',
})
export class SurveyList implements OnInit {

  surveys: Survey[] = [];
  loading = false;
  error = false;

  constructor(
    private surveyService: SurveyService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.getSurveys();
  }

  getSurveys(): void {
    this.loading = true;
    this.error = false;
    
    this.surveyService.getAllSurveys().subscribe({
      next: (response: any) => {
        this.surveys = response?.data || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = true;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  shareSurvey(id: string): void {
    const url = `${window.location.origin}/public/survey/${id}`;
    navigator.clipboard.writeText(url).then(() => {
      this.snackBar.open('Enlace copiado al portapapeles', 'Cerrar', { duration: 3000 });
    });
  }

  goToCreate(): void {
    this.router.navigate(['/surveys/new']);
  }

  goToView(id: string): void {
    this.router.navigate(['/surveys', id]);
  }

  goToEdit(id: string): void {
    this.router.navigate(['/surveys/edit', id]);
  }

  deleteSurvey(id: string): void {
    if (!confirm('¿Eliminar esta encuesta?')) return;
    this.surveyService.deleteSurvey(id).subscribe({
      next: () => {
        this.surveys = this.surveys.filter(s => s.id !== id);
        this.cdr.detectChanges();
      },
      error: () => {
        alert('Error al eliminar');
      }
    });
  }

  getStatusName(status: SurveyStatus): string {
    if (status === SurveyStatus.DRAFT) return 'Borrador';
    if (status === SurveyStatus.ACTIVE) return 'Activa';
    if (status === SurveyStatus.CLOSED) return 'Cerrada';
    return status;
  }

  getStatusClass(status: SurveyStatus): string {
    return 'status-' + status.toLowerCase();
  }
}