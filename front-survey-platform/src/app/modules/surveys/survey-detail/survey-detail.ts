import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SurveyService } from '../../../core/services/survey';
import { QuestionService } from '../../../core/services/question';
import { Survey } from '../../../core/models/survey.model';
import { Question } from '../../../core/models/question.model';
import { QuestionType } from '../../../core/models/enums/question-type.enum';
import { SurveyStatus } from '../../../core/models/enums/survey-status.enum';

@Component({
  selector: 'app-survey-detail',
  standalone: false,
  templateUrl: './survey-detail.html',
  styleUrl: './survey-detail.css',
})
export class SurveyDetail implements OnInit {
  survey: Survey | null = null;
  questions: Question[] = [];
  loading = false;
  loadingQuestions = false;
  surveyId: string | null = null;

  statusLabels: Record<SurveyStatus, string> = {
    [SurveyStatus.DRAFT]: 'Borrador',
    [SurveyStatus.ACTIVE]: 'Activa',
    [SurveyStatus.CLOSED]: 'Cerrada',
  };

  typeLabels: Record<QuestionType, string> = {
    [QuestionType.MULTIPLE_CHOICE]: 'Múltiple',
    [QuestionType.SINGLE_CHOICE]: 'Única',
    [QuestionType.TEXT]: 'Texto',
    [QuestionType.RATING]: 'Calificación',
    [QuestionType.NUMERIC]: 'Numérico',
    [QuestionType.YES_NO]: 'Sí / No',
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private surveyService: SurveyService,
    private questionService: QuestionService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.surveyId = this.route.snapshot.paramMap.get('id');
    if (this.surveyId) {
      this.loadSurvey();
      this.loadQuestions();
    } else {
      this.router.navigate(['/surveys']);
    }
  }

  loadSurvey(): void {
    this.loading = true;
    this.surveyService.getSurveyById(this.surveyId!).subscribe({
      next: (response) => {
        this.survey = response.data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
        this.router.navigate(['/surveys']);
      },
    });
  }

  loadQuestions(): void {
    this.loadingQuestions = true;
    this.questionService.getAll(this.surveyId!).subscribe({
      next: (response) => {
        this.questions = response.data ?? [];
        this.loadingQuestions = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.questions = [];
        this.loadingQuestions = false;
        this.cdr.detectChanges();
      },
    });
  }

  addQuestion(): void {
    this.router.navigate(
      ['/surveys', this.surveyId, 'questions', 'new'],
      { queryParams: { order: this.questions.length + 1 } }
    );
  }

  editQuestion(questionId: string): void {
    this.router.navigate(['/surveys', this.surveyId, 'questions', 'edit', questionId]);
  }

  deleteQuestion(questionId: string): void {
    if (!confirm('¿Eliminar esta pregunta?')) return;
    this.questionService.delete(this.surveyId!, questionId).subscribe({
      next: () => {
        this.questions = this.questions.filter(q => q.id !== questionId);
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Error al eliminar la pregunta', 'Cerrar', { duration: 3000 });
        this.cdr.detectChanges();
      },
    });
  }

  edit(): void {
    this.router.navigate(['/surveys/edit', this.surveyId]);
  }

  delete(): void {
    if (!confirm(`¿Eliminar "${this.survey?.title}"? Esta acción no se puede deshacer.`)) return;
    this.surveyService.deleteSurvey(this.surveyId!).subscribe({
      next: () => {
        this.snackBar.open('Encuesta eliminada', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/surveys']);
      },
      error: () => {
        this.snackBar.open('Error al eliminar', 'Cerrar', { duration: 3000 });
        this.cdr.detectChanges();
      },
    });
  }

  getStatusLabel(status: SurveyStatus): string {
    return this.statusLabels[status] ?? status;
  }

  getTypeLabel(type: QuestionType): string {
    return this.typeLabels[type] ?? type;
  }

  formatDate(date: string | undefined): string {
    if (!date) return 'N/A';
    return new Date(date).toLocaleDateString('es-CO', {
      year: 'numeric', month: 'long', day: 'numeric',
    });
  }
}
