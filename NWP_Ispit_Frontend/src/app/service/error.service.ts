import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OperationError } from '../error-list/error-list.component';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  private apiUrl = '/api/errors';

  constructor(private http: HttpClient) { }

  getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)

    return headers;
  }

  getErrors(): Observable<OperationError[]> {
    const headers = this.getHeaders();

    return this.http.get<OperationError[]>(this.apiUrl, {headers})
  }
}
