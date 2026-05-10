import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorRouting } from './error-routing';

describe('ErrorRouting', () => {
  let component: ErrorRouting;
  let fixture: ComponentFixture<ErrorRouting>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ErrorRouting],
    }).compileComponents();

    fixture = TestBed.createComponent(ErrorRouting);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
