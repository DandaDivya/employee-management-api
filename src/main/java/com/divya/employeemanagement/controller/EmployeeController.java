package com.divya.employeemanagement.controller;

import com.divya.employeemanagement.entity.Employee;
import com.divya.employeemanagement.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.divya.employeemanagement.dto.EmployeeDTO;
import com.divya.employeemanagement.dto.EmployeeResponseDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            Pageable pageable) {

        Page<EmployeeResponseDTO> employees =
                employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(employees);
    }
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> addEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        EmployeeResponseDTO savedEmployee =
                employeeService.saveEmployee(employeeDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEmployee);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @PathVariable Long id) {

        EmployeeResponseDTO employee =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        EmployeeResponseDTO updatedEmployee =
                employeeService.updateEmployee(id, employeeDTO);

        return ResponseEntity.ok(updatedEmployee);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            Pageable pageable) {

        if (name != null && department != null) {

            Page<EmployeeResponseDTO> employees =
                    employeeService.searchEmployees(
                            name,
                            department,
                            pageable);

            return ResponseEntity.ok(employees);
        }

        if (name != null) {

            Page<EmployeeResponseDTO> employees =
                    employeeService.getEmployeesByName(
                            name,
                            pageable);

            return ResponseEntity.ok(employees);
        }

        if (department != null) {

            Page<EmployeeResponseDTO> employees =
                    employeeService.getEmployeesByDepartment(
                            department,
                            pageable);

            return ResponseEntity.ok(employees);
        }

        return ResponseEntity.ok(Page.empty());
    }
}