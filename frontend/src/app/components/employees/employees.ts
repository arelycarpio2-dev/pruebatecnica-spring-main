import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, switchMap, catchError, of } from 'rxjs';
import { AuthService } from '../../services/auth';
import { EmployeeService, EmployeeResponse } from '../../services/employee';

@Component({
  selector: 'app-employees',
  templateUrl: './employees.html',
  styleUrls: ['./employees.css'],
  standalone: false,
})
export class Employees implements OnInit {
  employees$!: Observable<EmployeeResponse[]>;

  constructor(
    private employeeService: EmployeeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.employees$ = this.employeeService.onRefresh.pipe(
      switchMap(() => this.employeeService.findAll().pipe(
        catchError(() => of([]))
      ))
    );
  }

  onDelete(id: number): void {
    if (confirm('¿Está seguro de eliminar este empleado?')) {
      this.employeeService.delete(id).subscribe({
        next: () => this.employeeService.notifyRefresh(),
        error: () => {},
      });
    }
  }

  onEdit(id: number): void {
    this.router.navigate(['/empleados/editar', id]);
  }

  onLogout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.authService.clearSession();
        this.router.navigate(['/login']);
      },
    });
  }

  getUsername(): string {
    return this.authService.getUsername() || '';
  }
}