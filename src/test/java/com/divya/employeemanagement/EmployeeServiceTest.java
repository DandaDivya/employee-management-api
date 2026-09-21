package com.divya.employeemanagement;

import com.divya.employeemanagement.dto.EmployeeDTO;
import com.divya.employeemanagement.dto.EmployeeResponseDTO;
import com.divya.employeemanagement.entity.Employee;
import com.divya.employeemanagement.exception.EmployeeNotFoundException;
import com.divya.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.divya.employeemanagement.service.EmployeeService;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;
	@InjectMocks
	private EmployeeService employeeService;
	@Test
	void getEmployeeById_shouldReturnEmployee() {

		Employee employee = new Employee();

		employee.setId(1L);
		employee.setName("Divya");
		employee.setEmail("divya@gmail.com");
		employee.setDepartment("IT");
		when(employeeRepository.findById(1L))
				.thenReturn(Optional.of(employee));

		EmployeeResponseDTO result =
				employeeService.getEmployeeById(1L);
		assertEquals("Divya", result.getName());
	}
	@Test
	void getEmployeeById_shouldThrowException_whenEmployeeNotFound() {

		when(employeeRepository.findById(100L))
				.thenReturn(Optional.empty());

		assertThrows(
				EmployeeNotFoundException.class,
				() -> employeeService.getEmployeeById(100L)
		);
	}
	@Test
	void saveEmployee_shouldSaveAndReturnEmployee() {

		EmployeeDTO employeeDTO = new EmployeeDTO();

		employeeDTO.setName("Divya");
		employeeDTO.setEmail("divya@gmail.com");
		employeeDTO.setDepartment("IT");

		Employee savedEmployee = new Employee();

		savedEmployee.setId(1L);
		savedEmployee.setName("Divya");
		savedEmployee.setEmail("divya@gmail.com");
		savedEmployee.setDepartment("IT");

		when(employeeRepository.save(any(Employee.class)))
				.thenReturn(savedEmployee);

		EmployeeResponseDTO result =
				employeeService.saveEmployee(employeeDTO);

		assertEquals("Divya", result.getName());
		assertEquals("IT", result.getDepartment());

		verify(employeeRepository).save(any(Employee.class));
	}
	@Test
	void updateEmployee_shouldUpdateEmployee() {

		Employee existingEmployee = new Employee();

		existingEmployee.setId(1L);
		existingEmployee.setName("Divya");
		existingEmployee.setEmail("old@gmail.com");
		existingEmployee.setDepartment("IT");

		EmployeeDTO employeeDTO = new EmployeeDTO();

		employeeDTO.setName("Divya Updated");
		employeeDTO.setEmail("new@gmail.com");
		employeeDTO.setDepartment("HR");

		when(employeeRepository.findById(1L))
				.thenReturn(Optional.of(existingEmployee));

		when(employeeRepository.save(any(Employee.class)))
				.thenReturn(existingEmployee);

		EmployeeResponseDTO result =
				employeeService.updateEmployee(1L, employeeDTO);

		assertEquals("Divya Updated", result.getName());
		assertEquals("new@gmail.com", result.getEmail());
		assertEquals("HR", result.getDepartment());

		verify(employeeRepository).save(any(Employee.class));
	}
	@Test
	void deleteEmployee_shouldDeleteEmployee() {

		Employee employee = new Employee();

		employee.setId(1L);
		employee.setName("Divya");
		employee.setEmail("divya@gmail.com");
		employee.setDepartment("IT");

		when(employeeRepository.findById(1L))
				.thenReturn(Optional.of(employee));

		employeeService.deleteEmployee(1L);

		verify(employeeRepository).delete(employee);
	}
	@Test
	void deleteEmployee_shouldThrowException_whenEmployeeNotFound() {

		when(employeeRepository.findById(100L))
				.thenReturn(Optional.empty());

		assertThrows(
				EmployeeNotFoundException.class,
				() -> employeeService.deleteEmployee(100L)
		);
	}
	@Test
	void getEmployeesByDepartment_shouldReturnEmployees() {

		Employee employee1 = new Employee();
		employee1.setId(1L);
		employee1.setName("Divya");
		employee1.setEmail("divya@gmail.com");
		employee1.setDepartment("IT");

		Employee employee2 = new Employee();
		employee2.setId(2L);
		employee2.setName("Rahul");
		employee2.setEmail("rahul@gmail.com");
		employee2.setDepartment("IT");

		List<Employee> employees = List.of(employee1, employee2);

		Page<Employee> employeePage =
				new PageImpl<>(employees);

		Pageable pageable =
				PageRequest.of(0, 5);

		when(employeeRepository.findByDepartment(
				"IT", pageable))
				.thenReturn(employeePage);

		Page<EmployeeResponseDTO> result =
				employeeService.getEmployeesByDepartment(
						"IT", pageable);

		assertEquals(2, result.getContent().size());
		assertEquals("Divya", result.getContent().get(0).getName());
		assertEquals("Rahul", result.getContent().get(1).getName());

		verify(employeeRepository)
				.findByDepartment("IT", pageable);
	}
	@Test
	void getEmployeesByName_shouldReturnEmployees() {

		Employee employee1 = new Employee();
		employee1.setId(1L);
		employee1.setName("Divya");
		employee1.setEmail("divya@gmail.com");
		employee1.setDepartment("IT");

		Employee employee2 = new Employee();
		employee2.setId(2L);
		employee2.setName("Divya Sharma");
		employee2.setEmail("sharma@gmail.com");
		employee2.setDepartment("HR");

		List<Employee> employees = List.of(employee1, employee2);

		Page<Employee> employeePage =
				new PageImpl<>(employees);

		Pageable pageable =
				PageRequest.of(0, 5);

		when(employeeRepository.findByNameContainingIgnoreCase(
				"Divya", pageable))
				.thenReturn(employeePage);

		Page<EmployeeResponseDTO> result =
				employeeService.getEmployeesByName(
						"Divya", pageable);

		assertEquals(2, result.getContent().size());
		assertEquals("Divya", result.getContent().get(0).getName());
		assertEquals("Divya Sharma", result.getContent().get(1).getName());

		verify(employeeRepository)
				.findByNameContainingIgnoreCase("Divya", pageable);
	}
	@Test
	void searchEmployees_shouldReturnEmployeesByNameAndDepartment() {

		Employee employee = new Employee();

		employee.setId(1L);
		employee.setName("Divya");
		employee.setEmail("divya@gmail.com");
		employee.setDepartment("IT");

		List<Employee> employees = List.of(employee);

		Page<Employee> employeePage =
				new PageImpl<>(employees);

		Pageable pageable =
				PageRequest.of(0, 5);

		when(employeeRepository
				.findByNameContainingIgnoreCaseAndDepartment(
						"Divya",
						"IT",
						pageable))
				.thenReturn(employeePage);

		Page<EmployeeResponseDTO> result =
				employeeService.searchEmployees(
						"Divya",
						"IT",
						pageable);

		assertEquals(1, result.getContent().size());
		assertEquals("Divya", result.getContent().get(0).getName());
		assertEquals("IT", result.getContent().get(0).getDepartment());

		verify(employeeRepository)
				.findByNameContainingIgnoreCaseAndDepartment(
						"Divya",
						"IT",
						pageable);
	}
}