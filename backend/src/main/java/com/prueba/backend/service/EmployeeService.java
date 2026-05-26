package com.prueba.backend.service;

import com.prueba.backend.dto.EmployeeRequest;
import com.prueba.backend.dto.EmployeeResponse;
import com.prueba.backend.entity.Employee;
import com.prueba.backend.exception.DuplicateEntityException;
import com.prueba.backend.exception.ResourceNotFoundException;
import com.prueba.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        return EmployeeResponse.fromEntity(employee);
    }

    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByNumeroIdentificacion(request.getNumeroIdentificacion())) {
            throw new DuplicateEntityException(
                    "Ya existe un empleado con el número de identificación: " + request.getNumeroIdentificacion());
        }

        Employee employee = new Employee();
        employee.setNombre(request.getNombre());
        employee.setApellidos(request.getApellidos());
        employee.setFechaNacimiento(request.getFechaNacimiento());
        employee.setGenero(request.getGenero());
        employee.setNumeroIdentificacion(request.getNumeroIdentificacion());

        Employee saved = employeeRepository.save(employee);
        return EmployeeResponse.fromEntity(saved);
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        if (!employee.getNumeroIdentificacion().equals(request.getNumeroIdentificacion())
                && employeeRepository.existsByNumeroIdentificacion(request.getNumeroIdentificacion())) {
            throw new DuplicateEntityException(
                    "Ya existe un empleado con el número de identificación: " + request.getNumeroIdentificacion());
        }

        employee.setNombre(request.getNombre());
        employee.setApellidos(request.getApellidos());
        employee.setFechaNacimiento(request.getFechaNacimiento());
        employee.setGenero(request.getGenero());
        employee.setNumeroIdentificacion(request.getNumeroIdentificacion());

        Employee saved = employeeRepository.save(employee);
        return EmployeeResponse.fromEntity(saved);
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empleado no encontrado con id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}
