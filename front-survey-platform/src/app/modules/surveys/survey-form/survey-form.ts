import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SurveyService } from '../../../core/services/survey';
import { SurveyStatus } from '../../../core/models/enums/survey-status.enum';

@Component({
  selector: 'app-survey-form',
  standalone: false,
  templateUrl: './survey-form.html',
  styleUrl: './survey-form.css',
})
export class SurveyForm implements OnInit {
  form: FormGroup;
  isEditing = false;
  surveyId: string | null = null;
  loading = false;

  statuses = Object.values(SurveyStatus);
  statusLabels: Record<SurveyStatus, string> = {
    [SurveyStatus.DRAFT]: 'Borrador',
    [SurveyStatus.ACTIVE]: 'Activa',
    [SurveyStatus.CLOSED]: 'Cerrada',
  };

  constructor(
    private fb: FormBuilder,
    private surveyService: SurveyService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      description: [''],
      status: [SurveyStatus.DRAFT, Validators.required],
    });
  }

  ngOnInit(): void {
    this.surveyId = this.route.snapshot.paramMap.get('id');
    if (this.surveyId) {
      this.isEditing = true;
      this.loadSurvey();
    }
  }

  private loadSurvey(): void {
    this.loading = true;
    this.surveyService.getSurveyById(this.surveyId!).subscribe({
      next: (response) => {
        this.form.patchValue(response.data);
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

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    const request = this.isEditing
      ? this.surveyService.updateSurvey(this.surveyId!, this.form.value)
      : this.surveyService.createSurvey(this.form.value);

    request.subscribe({
      next: () => {
        this.loading = false;
        this.snackBar.open(
          this.isEditing ? 'Encuesta actualizada' : 'Encuesta creada',
          'Cerrar',
          { duration: 3000 }
        );
        this.router.navigate(['/surveys']);
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al guardar', 'Cerrar', { duration: 3000 });
        this.cdr.detectChanges();
      },
    });
  }

  cancel(): void {
    this.router.navigate(['/surveys']);
  }
}
