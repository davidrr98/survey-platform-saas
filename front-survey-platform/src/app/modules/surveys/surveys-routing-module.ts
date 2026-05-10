import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SurveyList } from './survey-list/survey-list';
import { SurveyForm } from './survey-form/survey-form';
import { SurveyDetail } from './survey-detail/survey-detail';
import { QuestionForm } from './question-form/question-form';

const routes: Routes = [
  { path: '', component: SurveyList },
  { path: 'new', component: SurveyForm },
  { path: 'edit/:id', component: SurveyForm },
  // Rutas de preguntas ANTES de ':id' para que no colisionen
  { path: ':surveyId/questions/new', component: QuestionForm },
  { path: ':surveyId/questions/edit/:questionId', component: QuestionForm },
  { path: ':id', component: SurveyDetail },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SurveysRoutingModule { }
