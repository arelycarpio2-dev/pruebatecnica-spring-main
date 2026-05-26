import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

export interface EmployeeRequest {
  nombre: string;
  apellidos: string;
  fechaNacimiento: string;
  genero: string;
  numeroIdentificacion: string;
}

export interface EmployeeResponse {
  id: number;
  nombre: string;
  apellidos: string;
  fechaNacimiento: string;
  genero: string;
  numeroIdentificacion: string;
}

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private apiUrl = 'http://localhost:8080/api/empleados';
  private refreshSubject = new BehaviorSubject<number>(0);

  constructor(private http: HttpClient) {}

  get onRefresh(): Observable<number> {
    return this.refreshSubject.asObservable();
  }

  notifyRefresh(): void {
    this.refreshSubject.next(this.refreshSubject.value + 1);
  }

  findAll(): Observable<EmployeeResponse[]> {
    return this.http.get<EmployeeResponse[]>(`${this.apiUrl}?_=${Date.now()}`);
  }

  findById(id: number): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.apiUrl}/${id}`);
  }

  create(employee: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.post<EmployeeResponse>(this.apiUrl, employee);
  }

  update(id: number, employee: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.put<EmployeeResponse>(`${this.apiUrl}/${id}`, employee);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
