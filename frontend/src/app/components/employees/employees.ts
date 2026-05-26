import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { EmployeeService, EmployeeResponse } from '../../services/employee';

@Component({
  selector: 'app-employees',
  templateUrl: './employees.html',
  styleUrls: ['./employees.css'],
  standalone: false,
})
export class Employees implements OnInit {
  employees: EmployeeResponse[] = [];
  loading = false;
  error = '';

  constructor(
    private employeeService: EmployeeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.employeeService.findAll().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar empleados';
        this.loading = false;
      },
    });
  }

  onDelete(id: number): void {
    if (confirm('¿Está seguro de eliminar este empleado?')) {
      this.employeeService.delete(id).subscribe({
        next: () => this.loadEmployees(),
        error: () => (this.error = 'Error al eliminar empleado'),
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
