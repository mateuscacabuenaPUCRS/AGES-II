import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperimentalClassComponent } from './trial-class.component';

describe('ExperimentalClassComponent', () => {
  let component: ExperimentalClassComponent;
  let fixture: ComponentFixture<ExperimentalClassComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExperimentalClassComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ExperimentalClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
