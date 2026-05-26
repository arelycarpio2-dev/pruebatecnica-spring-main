package com.prueba.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.backend.config.JwtUtil;
import com.prueba.backend.dto.EmployeeRequest;
import com.prueba.backend.dto.EmployeeResponse;
import com.prueba.backend.exception.ResourceNotFoundException;
import com.prueba.backend.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private EmployeeService employeeService;

    private String authToken;

    @BeforeEach
    void setUp() {
        authToken = jwtUtil.generateToken("admin");
    }

    @Test
    void findAll() throws Exception {
        EmployeeResponse emp1 = createResponse(1L, "Juan", "Perez", "DPI-001");
        EmployeeResponse emp2 = createResponse(2L, "Maria", "Lopez", "DPI-002");
        when(employeeService.findAll()).thenReturn(List.of(emp1, emp2));

        mockMvc.perform(get("/api/empleados")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Maria"));
    }

    @Test
    void findById() throws Exception {
        EmployeeResponse emp = createResponse(1L, "Juan", "Perez", "DPI-001");
        when(employeeService.findById(1L)).thenReturn(emp);

        mockMvc.perform(get("/api/empleados/1")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.numeroIdentificacion").value("DPI-001"));
    }

    @Test
    void findByIdNotFound() throws Exception {
        when(employeeService.findById(99L)).thenThrow(new ResourceNotFoundException("Empleado no encontrado con id: 99"));

        mockMvc.perform(get("/api/empleados/99")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Empleado no encontrado con id: 99"));
    }

    @Test
    void create() throws Exception {
        EmployeeRequest request = createRequest("Juan", "Perez", "DPI-001");
        EmployeeResponse response = createResponse(1L, "Juan", "Perez", "DPI-001");
        when(employeeService.create(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/empleados")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void update() throws Exception {
        EmployeeRequest request = createRequest("Juan", "Perez Updated", "DPI-001");
        EmployeeResponse response = createResponse(1L, "Juan", "Perez Updated", "DPI-001");
        when(employeeService.update(eq(1L), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/empleados/1")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellidos").value("Perez Updated"));
    }

    @Test
    void deleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/empleados/1")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    private EmployeeResponse createResponse(Long id, String nombre, String apellidos, String dpi) {
        EmployeeResponse res = new EmployeeResponse();
        res.setId(id);
        res.setNombre(nombre);
        res.setApellidos(apellidos);
        res.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        res.setGenero("Masculino");
        res.setNumeroIdentificacion(dpi);
        return res;
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
