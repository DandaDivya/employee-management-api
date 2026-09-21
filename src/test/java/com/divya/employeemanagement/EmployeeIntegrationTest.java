import com.divya.employeemanagement.EmployeeManagementApiApplication;
import com.divya.employeemanagement.config.JwtService;
import com.divya.employeemanagement.entity.Employee;
import com.divya.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.divya.employeemanagement.repository.UserRepository;
import com.divya.employeemanagement.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.divya.employeemanagement.entity.Role;

@SpringBootTest(classes = EmployeeManagementApiApplication.class)
@AutoConfigureMockMvc
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    private String bearerToken() {                                      // NEW helper method
        return "Bearer " + jwtService.generateToken("testuser");
    }

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Test
    void shouldCreateEmployee() throws Exception {

        String employeeJson = """
                {
                    "name": "John",
                    "email": "john@gmail.com",
                    "department": "IT"
                }
                """;

        mockMvc.perform(post("/employees")
                        .header("Authorization", bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.department").value("IT"));
    }
    @Test
    void shouldGetAllEmployees() throws Exception {

        Employee employee1 = new Employee();
        employee1.setName("John");
        employee1.setEmail("john@gmail.com");
        employee1.setDepartment("IT");

        Employee employee2 = new Employee();
        employee2.setName("Alice");
        employee2.setEmail("alice@gmail.com");
        employee2.setDepartment("HR");

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        mockMvc.perform(get("/employees")
                .header("Authorization", bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("John"))
                .andExpect(jsonPath("$.content[1].name").value("Alice"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
    @Test
    void shouldGetEmployeeById() throws Exception {

        Employee employee = new Employee();
        employee.setName("John");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");

        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(get("/employees/" + savedEmployee.getId())
                .header("Authorization", bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEmployee.getId()))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.department").value("IT"));
    }
    @Test
    void shouldUpdateEmployee() throws Exception {

        Employee employee = new Employee();
        employee.setName("John");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");

        Employee savedEmployee = employeeRepository.save(employee);

        String updatedEmployeeJson = """
            {
                "name": "John Updated",
                "email": "john.updated@gmail.com",
                "department": "HR"
            }
            """;

        mockMvc.perform(put("/employees/" + savedEmployee.getId())
                        .header("Authorization", bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@gmail.com"))
                .andExpect(jsonPath("$.department").value("HR"));
    }
    @Test
    void shouldDeleteEmployee() throws Exception {

        Employee employee = new Employee();
        employee.setName("John");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");

        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(delete("/employees/" + savedEmployee.getId())
                .header("Authorization", bearerToken()))

                .andExpect(status().isNoContent());

        assertFalse(employeeRepository.findById(savedEmployee.getId()).isPresent());
    }
    @Test                                                               // NEW test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isForbidden());
    }
    @Test
    void shouldRejectEmployeeCreationForUserRole() throws Exception {

        // Create a normal USER
        User user = new User();
        user.setUsername("normaluser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);

        userRepository.save(user);

        // Generate JWT for normaluser
        String token = jwtService.generateToken("normaluser");

        String employeeJson = """
            {
                "name": "John",
                "email": "john@gmail.com",
                "department": "IT"
            }
            """;

        mockMvc.perform(post("/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isForbidden());
    }
    @Test
    void shouldAllowEmployeeReadForUserRole() throws Exception {

        User user = new User();
        user.setUsername("normaluser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken("normaluser");

        mockMvc.perform(get("/employees")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void shouldRejectEmployeeUpdateForUserRole() throws Exception {

        // Create employee
        Employee employee = new Employee();
        employee.setName("John");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");

        Employee savedEmployee = employeeRepository.save(employee);

        // Create normal USER
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken("updateuser");

        String updatedEmployeeJson = """
            {
                "name": "John Updated",
                "email": "john.updated@gmail.com",
                "department": "HR"
            }
            """;

        mockMvc.perform(put("/employees/" + savedEmployee.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isForbidden());
    }
    @Test
    void shouldRejectEmployeeDeleteForUserRole() throws Exception {

        // Create employee
        Employee employee = new Employee();
        employee.setName("John");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");

        Employee savedEmployee = employeeRepository.save(employee);

        // Create normal USER
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken("deleteuser");

        mockMvc.perform(delete("/employees/" + savedEmployee.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
}