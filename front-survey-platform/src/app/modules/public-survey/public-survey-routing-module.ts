import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PublicSurveyList } from './public-survey-list/public-survey-list';
import { SurveyResponse } from './survey-response/survey-response';
import { ErrorRouting } from './error-routing/error-routing';

const routes: Routes = [
  { path: 'surveys', component: PublicSurveyList },
  { path: 'survey/:id', component: SurveyResponse },
  { path: '**', component: ErrorRouting },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicSurveyRoutingModule { }
