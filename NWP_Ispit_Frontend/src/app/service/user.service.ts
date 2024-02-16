import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/users';
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) { }

  getToken(): HttpHeaders {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`).set('Content-Type', 'application/json');

    return headers;
  }

  createUser(user: User): Observable<User> {
    const headers = this.getToken();
    console.log(user)
    return this.http.post<User>(this.apiUrl + "/create", user, {headers});
  }


  getUsers(): Observable<User[]> {
    const headers = this.getToken();
    return this.http.get<User[]>(this.apiUrl, { headers });
  }

  getUserById(userId: number): Observable<User> {
    const headers = this.getToken();
    const url = `${this.apiUrl}/${userId}`;

    return this.http.get<User>(url, {headers});
  }

  updateUser(userId: number, updatedUser: User): Observable<User> {
    const headers = this.getToken();
    const url = `${this.apiUrl}/${userId}`;

    return this.http.put<User>(url, updatedUser, {headers});
  }

  deleteUser(userId: number): Observable<void> {
    const headers = this.getToken();
    const url = `${this.apiUrl}/${userId}`;

    return this.http.delete<void>(url, {headers});
  }
}
