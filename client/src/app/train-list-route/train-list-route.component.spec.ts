import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainListRouteComponent } from './train-list-route.component';

describe('TrainListRouteComponent', () => {
  let component: TrainListRouteComponent;
  let fixture: ComponentFixture<TrainListRouteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrainListRouteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrainListRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
