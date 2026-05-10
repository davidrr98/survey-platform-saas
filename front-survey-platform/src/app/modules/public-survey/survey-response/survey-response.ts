import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Survey } from '../../../core/models/survey.model';
import { Question } from '../../../core/models/question.model';
import { QuestionType } from '../../../core/models/enums/question-type.enum';
import { SurveyStatus } from '../../../core/models/enums/survey-status.enum';
import { ApiResponse } from '../../../core/models/api-response.model';
import { environment } from '../../../../env/enviroment';

@Component({
  selector: 'app-survey-response',
  standalone: false,
  templateUrl: './survey-response.html',
  styleUrl: './survey-response.css',
})
export class SurveyResponse implements OnInit {
  survey: Survey | null = null;
  questions: Question[] = [];
  form!: FormGroup;
  surveyId: string | null = null;

  loading = true;
  submitting = false;
  submitted = false;
  error = false;

  readonly QuestionType = QuestionType;
  readonly ratings = [1, 2, 3, 4, 5];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.surveyId =
      this.route.snapshot.paramMap.get('id') ??
      this.route.parent?.snapshot.paramMap.get('id') ??
      null;

    if (!this.surveyId) {
      this.error = true;
      this.loading = false;
      return;
    }
    this.loadData();
  }

  private loadData(): void {
    this.http
      .get<ApiResponse<any>>(`${environment.apiUrl}/api/v1/public/surveys/${this.surveyId}`)
      .subscribe({
        next: (res) => {
          const data = res.data;
          this.survey = data;
          this.questions = (data.questions ?? [])
            .sort((a: Question, b: Question) => a.questionOrder - b.questionOrder);
          this.buildForm();
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

  private buildForm(): void {
    const controls: Record<string, any> = {};
    for (const q of this.questions) {
      if (q.type === QuestionType.MULTIPLE_CHOICE && q.options?.length) {
        controls[q.id!] = this.fb.array(q.options.map(() => this.fb.control(false)));
      } else {
        controls[q.id!] = this.fb.control('');
      }
    }
    this.form = this.fb.group(controls);
  }

  optionsArray(questionId: string): FormArray {
    return this.form.get(questionId) as FormArray;
  }

  getRating(questionId: string): number {
    return this.form.get(questionId)?.value ?? 0;
  }

  setRating(questionId: string, value: number): void {
    this.form.get(questionId)?.setValue(value);
  }

  isClosed(): boolean {
    return this.survey?.status === SurveyStatus.CLOSED;
  }

  isDraft(): boolean {
    return this.survey?.status === SurveyStatus.DRAFT;
  }

  submit(): void {
    if (this.submitting) return;
    this.submitting = true;

    const answers: { questionId: string; textValue?: string; optionId?: string }[] = [];

    for (const q of this.questions) {
      const qId = q.id!;

      if (q.type === QuestionType.MULTIPLE_CHOICE && q.options?.length) {
        const checks: boolean[] = this.optionsArray(qId).value;
        q.options.forEach((opt, i) => {
          if (checks[i]) answers.push({ questionId: qId, optionId: opt.id! });
        });

      } else if (q.type === QuestionType.SINGLE_CHOICE) {
        const optionId = this.form.get(qId)?.value;
        if (optionId) answers.push({ questionId: qId, optionId });

      } else {
        // TEXT, NUMERIC, RATING, YES_NO → textValue como string
        const val = this.form.get(qId)?.value;
        if (val !== null && val !== '') {
          answers.push({ questionId: qId, textValue: String(val) });
        }
      }
    }

    this.http
      .post(`${environment.apiUrl}/api/v1/public/surveys/${this.surveyId}/respond`, { answers })
      .subscribe({
        next: () => {
          this.submitted = true;
          this.submitting = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.submitting = false;
          this.cdr.detectChanges();
          alert('Error al enviar las respuestas. Intenta de nuevo.');
        },
      });
  }
}
