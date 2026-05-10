import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from './material.module';
import { ConfirmDialog } from './components/confirm-dialog/confirm-dialog';

@NgModule({
  declarations: [
    ConfirmDialog
  ],
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule
  ],
  exports: [
    CommonModule,
    RouterModule,
    ConfirmDialog
  ]
})
export class SharedModule {}
