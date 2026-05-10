import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { QuestionService } from '../../../core/services/question';
import { QuestionType } from '../../../core/models/enums/question-type.enum';
import { QuestionFormData } from '../../../core/models/question.model';

@Component({
  selector: 'app-question-form',
  standalone: false,
  templateUrl: './question-form.html',
  styleUrl: './question-form.css',
})
export class QuestionForm implements OnInit {
  form: FormGroup;
  isEditing = false;
  surveyId: string | null = null;
  questionId: string | null = null;
  loading = false;

  types = Object.values(QuestionType);
  typeLabels: Record<QuestionType, string> = {
    [QuestionType.MULTIPLE_CHOICE]: 'Selección múltiple',
    [QuestionType.SINGLE_CHOICE]: 'Selección única',
    [QuestionType.TEXT]: 'Texto abierto',
    [QuestionType.RATING]: 'Calificación',
    [QuestionType.NUMERIC]: 'Numérico',
    [QuestionType.YES_NO]: 'Sí / No',
  };

  constructor(
    private fb: FormBuilder,
    private questionService: QuestionService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      text: ['', [Validators.required, Validators.minLength(5)]],
      type: [QuestionType.TEXT, Validators.required],
      required: [false],
      questionOrder: [1, Validators.required],
      options: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    this.surveyId = this.route.snapshot.paramMap.get('surveyId');
    this.questionId = this.route.snapshot.paramMap.get('questionId');

    const order = this.route.snapshot.queryParamMap.get('order');
    if (order) this.form.patchValue({ questionOrder: +order });

    if (this.questionId) {
      this.isEditing = true;
      this.loadQuestion();
    }

    this.form.get('type')!.valueChanges.subscribe(type => {
      if (!this.needsOptions(type)) {
        this.options.clear();
      } else if (this.options.length === 0) {
        this.addOption();
        this.addOption();
      }
    });
  }

  get options(): FormArray {
    return this.form.get('options') as FormArray;
  }

  needsOptions(type?: QuestionType): boolean {
    const t = type ?? this.form.get('type')?.value;
    return t === QuestionType.MULTIPLE_CHOICE || t === QuestionType.SINGLE_CHOICE;
  }

  addOption(): void {
    this.options.push(this.fb.control('', Validators.required));
  }

  removeOption(i: number): void {
    if (this.options.length > 2) this.options.removeAt(i);
  }

  private loadQuestion(): void {
    this.loading = true;
    this.questionService.getById(this.surveyId!, this.questionId!).subscribe({
      next: (response) => {
        const q = response.data;
        this.form.patchValue({ text: q.text, type: q.type, required: q.required, questionOrder: q.questionOrder });
        if (q.options?.length) {
          this.options.clear();
          q.options.sort((a, b) => a.optionOrder - b.optionOrder)
            .forEach(o => this.options.push(this.fb.control(o.text, Validators.required)));
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
        this.goBack();
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    const { text, type, required, questionOrder } = this.form.value;
    const data: QuestionFormData = {
      text, type, required, questionOrder,
      options: this.needsOptions()
        ? this.options.value.map((t: string, i: number) => ({ text: t, optionOrder: i + 1 }))
        : undefined,
    };

    const req = this.isEditing
      ? this.questionService.update(this.surveyId!, this.questionId!, data)
      : this.questionService.create(this.surveyId!, data);

    req.subscribe({
      next: () => {
        this.loading = false;
        this.snackBar.open(this.isEditing ? 'Pregunta actualizada' : 'Pregunta creada', 'Cerrar', { duration: 3000 });
        this.goBack();
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Error al guardar la pregunta', 'Cerrar', { duration: 3000 });
        this.cdr.detectChanges();
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/surveys', this.surveyId]);
  }
}
