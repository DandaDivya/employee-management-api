package com.divya.employeemanagement.service;

import com.divya.employeemanagement.entity.Employee;
import com.divya.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import com.divya.employeemanagement.exception.EmployeeNotFoundException;
import com.divya.employeemanagement.dto.EmployeeDTO;
import com.divya.employeemanagement.dto.EmployeeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponseDTO saveEmployee(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDepartment(employeeDTO.getDepartment());

        Employee savedEmployee = employeeRepository.save(employee);

        return convertToResponseDTO(savedEmployee);
    }

    public Page<EmployeeResponseDTO> getAllEmployees(
            Pageable pageable) {

        Page<Employee> employees =
                employeeRepository.findAll(pageable);

        return employees.map(this::convertToResponseDTO);
    }
    public EmployeeResponseDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with id " + id + " not found"));

        return convertToResponseDTO(employee);
    }
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {

        Employee existingEmployee =
                employeeRepository.findById(id).orElse(null);

        if (existingEmployee == null) {
            throw new EmployeeNotFoundException(
                    "Employee with id " + id + " not found");
        }

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setDepartment(employeeDTO.getDepartment());

        Employee updatedEmployee =
                employeeRepository.save(existingEmployee);

        return convertToResponseDTO(updatedEmployee);
    }
    public void deleteEmployee(Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee with id " + id + " not found"));

        employeeRepository.delete(employee);
    }
    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {

        EmployeeResponseDTO dto = new EmployeeResponseDTO();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());

        return dto;
    }
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(
            String department,
            Pageable pageable) {

        Page<Employee> employees =
                employeeRepository.findByDepartment(
                        department,
                        pageable);

        return employees.map(this::convertToResponseDTO);
    }
    public Page<EmployeeResponseDTO> getEmployeesByName(
            String name,
            Pageable pageable) {

        Page<Employee> employees =
                employeeRepository.findByNameContainingIgnoreCase(
                        name,
                        pageable);

        return employees.map(this::convertToResponseDTO);
    }
    public Page<EmployeeResponseDTO> searchEmployees(
            String name,
            String department,
            Pageable pageable) {

        Page<Employee> employees =
                employeeRepository.findByNameContainingIgnoreCaseAndDepartment(
                        name,
                        department,
                        pageable);

        return employees.map(this::convertToResponseDTO);
    }
}