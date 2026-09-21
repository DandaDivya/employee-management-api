package com.divya.employeemanagement.repository;

import com.divya.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable);

    Page<Employee> findByNameContainingIgnoreCaseAndDepartment(
            String name,
            String department,
            Pageable pageable);
    Page<Employee> findByDepartment(
            String department,
            Pageable pageable);
}