package com.prueba.backend.service;

import com.prueba.backend.dto.EmployeeRequest;
import com.prueba.backend.dto.EmployeeResponse;
import com.prueba.backend.entity.Employee;
import com.prueba.backend.exception.DuplicateEntityException;
import com.prueba.backend.exception.ResourceNotFoundException;
import com.prueba.backend.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    void findAll() {
        Employee emp1 = createEmployee(1L, "Juan", "Perez", "DPI-001");
        Employee emp2 = createEmployee(2L, "Maria", "Lopez", "DPI-002");
        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));

        List<EmployeeResponse> result = employeeService.findAll();

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Maria", result.get(1).getNombre());
    }

    @Test
    void findByIdSuccess() {
        Employee emp = createEmployee(1L, "Juan", "Perez", "DPI-001");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        EmployeeResponse result = employeeService.findById(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("DPI-001", result.getNumeroIdentificacion());
    }

    @Test
    void findByIdNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(99L));
    }

    @Test
    void createSuccess() {
        EmployeeRequest request = createRequest("Juan", "Perez", "DPI-001");
        Employee saved = createEmployee(1L, "Juan", "Perez", "DPI-001");

        when(employeeRepository.existsByNumeroIdentificacion("DPI-001")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeResponse result = employeeService.create(request);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("DPI-001", result.getNumeroIdentificacion());
    }

    @Test
    void createDuplicateIdentification() {
        EmployeeRequest request = createRequest("Juan", "Perez", "DPI-001");
        when(employeeRepository.existsByNumeroIdentificacion("DPI-001")).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> employeeService.create(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateSuccess() {
        Employee existing = createEmployee(1L, "Juan", "Perez", "DPI-001");
        EmployeeRequest request = createRequest("Juan", "Perez Updated", "DPI-001");
        Employee updated = createEmployee(1L, "Juan", "Perez Updated", "DPI-001");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updated);

        EmployeeResponse result = employeeService.update(1L, request);

        assertEquals("Perez Updated", result.getApellidos());
    }

    @Test
    void updateNotFound() {
        EmployeeRequest request = createRequest("Juan", "Perez", "DPI-001");
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.update(99L, request));
    }

    @Test
    void deleteSuccess() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFound() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.delete(99L));
        verify(employeeRepository, never()).deleteById(any());
    }

    private Employee createEmployee(Long id, String nombre, String apellidos, String dpi) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setNombre(nombre);
        emp.setApellidos(apellidos);
        emp.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        emp.setGenero("Masculino");
        emp.setNumeroIdentificacion(dpi);
        return emp;
    }

    private EmployeeRequest createRequest(String nombre, String apellidos, String dpi) {
        EmployeeRequest req = new EmployeeRequest();
        req.setNombre(nombre);
        req.setApellidos(apellidos);
        req.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        req.setGenero("Masculino");
        req.setNumeroIdentificacion(dpi);
        return req;
    }
}
