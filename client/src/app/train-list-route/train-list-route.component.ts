import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-train-list-route',
  templateUrl: './train-list-route.component.html',
  styleUrls: ['./train-list-route.component.scss']
})
export class TrainListRouteComponent implements OnInit {
  trains$: Observable<any>;
  constructor(private httpClient: HttpClient) {
    this.trains$ = this.httpClient.get<any>('http://localhost:8080/api/train');
  }

  ngOnInit(): void {
  }

}
