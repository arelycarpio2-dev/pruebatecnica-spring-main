import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeService, EmployeeRequest } from '../../services/employee';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.html',
  styleUrls: ['./employee-form.css'],
  standalone: false,
})
export class EmployeeForm implements OnInit {
  employee: EmployeeRequest = {
    nombre: '',
    apellidos: '',
    fechaNacimiento: '',
    genero: '',
    numeroIdentificacion: '',
  };
  isEdit = false;
  editId: number | null = null;
  loading = false;
  error = '';
  generos = ['Masculino', 'Femenino', 'Otro'];

  constructor(
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = Number(id);
      this.loadEmployee(this.editId);
    }
  }

  loadEmployee(id: number): void {
    this.loading = true;
    this.employeeService.findById(id).subscribe({
      next: (emp) => {
        this.employee = {
          nombre: emp.nombre,
          apellidos: emp.apellidos,
          fechaNacimiento: emp.fechaNacimiento,
          genero: emp.genero,
          numeroIdentificacion: emp.numeroIdentificacion,
        };
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar empleado';
        this.loading = false;
      },
    });
  }

  onSubmit(): void {
    if (!this.employee.nombre || !this.employee.apellidos || !this.employee.fechaNacimiento || !this.employee.genero || !this.employee.numeroIdentificacion) {
      this.error = 'Todos los campos son obligatorios';
      return;
    }

    this.loading = true;
    this.error = '';

    const request = { ...this.employee };
    const operation = this.isEdit && this.editId
      ? this.employeeService.update(this.editId, request)
      : this.employeeService.create(request);

    operation.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/empleados']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.mensaje || 'Error al guardar empleado';
      },
    });
  }

  onCancel(): void {
    this.router.navigate(['/empleados']);
  }
}
