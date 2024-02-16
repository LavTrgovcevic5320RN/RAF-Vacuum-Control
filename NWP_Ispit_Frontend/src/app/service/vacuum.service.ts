import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vacuum } from '../vacuum-list/vacuum-list.component';

export interface OperationResponse {
  msg: string;
}

@Injectable({
  providedIn: 'root',
})
export class VacuumServiceService {
  private apiUrl = '/api/vacuums';

  constructor(private http: HttpClient) {}

  getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)

    return headers;
  }

  createVacuum(name: string): Observable<Vacuum> {
    const headers = this.getHeaders();

    return this.http.post<Vacuum>(
      this.apiUrl + '/add',
      {
        name,
      },
      { headers }
    );
  }

  startVacuum(id: string): Observable<OperationResponse> {
    const headers = this.getHeaders();

    return this.http.post<OperationResponse>(
      this.apiUrl + `/start/${id}`,null,
      { headers }
    );
  }

  stopVacuum(id: string): Observable<OperationResponse> {
    const headers = this.getHeaders();

    return this.http.post<OperationResponse>(
      this.apiUrl + `/stop/${id}`,null,
      { headers }
    );
  }

  discVacuum(id: string): Observable<OperationResponse> {
    const headers = this.getHeaders();

    return this.http.post<OperationResponse>(
      this.apiUrl + `/discharge/${id}`,null,
      { headers }
    );
  }

  scheduleOperation(id: string, operation: string, schedTime: Date): Observable<OperationResponse> {
    const headers = this.getHeaders();

    return this.http.post<OperationResponse>(
      `/api/vacuum-schedule`,{
        vacuumId: id,
        action: operation,
        schedTime
      },
      { headers }
    );
  }

  getVacuums(
    name: string,
    status: string,
    dateFrom: string,
    dateTo: string
  ): Observable<Vacuum[]> {
    const headers = this.getHeaders();

    const dateFormat = new Intl.DateTimeFormat('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      timeZone: 'UTC',
    });

    let params: any = {};

    if (name != '') {
      params.name = name;
    }

    if (status != '') {
      params.status = status;
    }

    if (dateFrom != '') {
      params.dateFrom = dateFormat.format(Date.parse(dateFrom));

      if (dateTo != '') {
        params.dateTo = dateFormat.format(Date.parse(dateTo));
      } else {
        alert("Date to can't be null");
        // @ts-ignore
        return null;
      }
    }

    return this.http.get<Vacuum[]>(this.apiUrl + '/search', {
      headers,
      params,
    });
  }
}
