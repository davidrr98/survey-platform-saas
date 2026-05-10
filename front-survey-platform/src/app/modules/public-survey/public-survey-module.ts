import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { PublicSurveyRoutingModule } from './public-survey-routing-module';
import { PublicSurveyList } from './public-survey-list/public-survey-list';
import { SurveyResponse } from './survey-response/survey-response';
import { ErrorRouting } from './error-routing/error-routing';

@NgModule({
  declarations: [PublicSurveyList, SurveyResponse, ErrorRouting],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PublicSurveyRoutingModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatCheckboxModule,
  ],
})
export class PublicSurveyModule {}
