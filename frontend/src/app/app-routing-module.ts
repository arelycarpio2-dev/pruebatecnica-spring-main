import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Employees } from './components/employees/employees';
import { EmployeeForm } from './components/employee-form/employee-form';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'empleados', component: Employees },
  { path: 'empleados/nuevo', component: EmployeeForm },
  { path: 'empleados/editar/:id', component: EmployeeForm },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
