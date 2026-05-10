import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RouterModule } from '@angular/router';

import { AnalyticsRoutingModule } from './analytics-routing.module';
import { AnalyticsList } from './analytics-list/analytics-list';
import { AnalyticsDashboard } from './analytics-dashboard/analytics-dashboard';

@NgModule({
  declarations: [AnalyticsList, AnalyticsDashboard],
  imports: [
    CommonModule,
    RouterModule,
    AnalyticsRoutingModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
  ],
})
export class AnalyticsModule { }
