import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

import { SurveysRoutingModule } from './surveys-routing-module';
import { SharedModule } from '../../shared/shared.module';

import { SurveyList } from './survey-list/survey-list';
import { SurveyForm } from './survey-form/survey-form';
import { SurveyDetail } from './survey-detail/survey-detail';
import { QuestionForm } from './question-form/question-form';

@NgModule({
  declarations: [
    SurveyList,
    SurveyForm,
    SurveyDetail,
    QuestionForm,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SurveysRoutingModule,
    SharedModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatSlideToggleModule,
  ],
})
export class SurveysModule { }
