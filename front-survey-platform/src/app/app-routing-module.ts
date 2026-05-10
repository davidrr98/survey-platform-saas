import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayout } from './layouts/main-layout/main-layout';
import { ErrorRouting } from './layouts/error-routing/error-routing';

const routes: Routes = [
  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: '',
        redirectTo: 'surveys',
        pathMatch: 'full'
      },
      {
        path: 'surveys',
        loadChildren: () => import('./modules/surveys/surveys-module').then(m => m.SurveysModule)
      },
      {
        path: 'analytics',
        loadChildren: () => import('./modules/analytics/analytics.module').then(m => m.AnalyticsModule)
      },
      {
        path: 'public',
        loadChildren: () => import('./modules/public-survey/public-survey-module').then(m => m.PublicSurveyModule)
      },
      {
        path: '**',
        component: ErrorRouting
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
