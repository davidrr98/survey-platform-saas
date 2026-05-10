import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnalyticsList } from './analytics-list/analytics-list';
import { AnalyticsDashboard } from './analytics-dashboard/analytics-dashboard';

const routes: Routes = [
  { path: '', component: AnalyticsList },
  { path: ':surveyId', component: AnalyticsDashboard },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnalyticsRoutingModule { }
