package lu.codehackademy.demolabs.sal.ut;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import lu.codehackademy.demolabs.transversal.vo.CHStudent;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests cases (test business behavior validity) for class "lu.codehackademy.demolabs.sal.ServicesProviderController".<br>
 * Explicitly let methods not tested in order to show an "Unit test code coverage quality issue"
 * 
 * @author Dominique RIGHETTO
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/resources/springmvc-context.xml")
public class ServicesProviderControllerTestCase {

	/** Spring context ref */
	@Autowired
	private WebApplicationContext wac;

	/** MVC utilites mocker */
	private MockMvc mockMvc;

	/**
	 * Test case init
	 */
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	/**
	 * Test case validating the student registration service in a valid case.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewStudentRegistrationCaseOK() throws Exception {
		// Prepare an input object (test content)
		List<Integer> classesRegistered = new ArrayList<>();
		classesRegistered.add(1);
		classesRegistered.add(2);
		classesRegistered.add(3);
		CHStudent newStudent = new CHStudent();
		newStudent.setLastnameFirstname("Test Case A");
		newStudent.setEmail("TestA@test.com");
		newStudent.setMotivation("My motivation is to validate that this test is functional");
		newStudent.setPassword("XXXXXXXXXXXXXXXXXX");
		newStudent.setClassesRegistered(classesRegistered);
		// Convert input object to JSON content
		StringWriter buffer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(buffer, newStudent);
		// Create input test Http request
		MockHttpServletRequestBuilder mockHttpRequest = post("/services/register").content(buffer.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		// Apply test
		ResultActions testCase = this.mockMvc.perform(mockHttpRequest);
		// Validate test
		testCase.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("true"));
	}

	/**
	 * Test case validating the student registration service in a invalid case.<br>
	 * Invalid Email address case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewStudentRegistrationCaseKO01() throws Exception {
		// Prepare an input object (test content)
		List<Integer> classesRegistered = new ArrayList<>();
		classesRegistered.add(1);
		classesRegistered.add(2);
		classesRegistered.add(3);
		CHStudent newStudent = new CHStudent();
		newStudent.setLastnameFirstname("Test Case B");
		newStudent.setEmail("TestB#test.com");
		newStudent.setMotivation("My motivation is to validate that this test is not functional");
		newStudent.setPassword("XXXXXXXXXXXXXXXXXX");
		newStudent.setClassesRegistered(classesRegistered);
		// Convert input object to JSON content
		StringWriter buffer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(buffer, newStudent);
		// Create input test Http request
		MockHttpServletRequestBuilder mockHttpRequest = post("/services/register").content(buffer.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		// Apply test
		ResultActions testCase = this.mockMvc.perform(mockHttpRequest);
		// Validate test
		testCase.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("false"));
	}

	/**
	 * Test case validating the student registration service in a invalid case.<br>
	 * Invalid Password case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewStudentRegistrationCaseKO02() throws Exception {
		// Prepare an input object (test content)
		List<Integer> classesRegistered = new ArrayList<>();
		classesRegistered.add(1);
		classesRegistered.add(2);
		classesRegistered.add(3);
		CHStudent newStudent = new CHStudent();
		newStudent.setLastnameFirstname("Test Case C");
		newStudent.setEmail("TestC@test.com");
		newStudent.setMotivation("My motivation is to validate that this test is not functional");
		newStudent.setPassword("");
		newStudent.setClassesRegistered(classesRegistered);
		// Convert input object to JSON content
		StringWriter buffer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(buffer, newStudent);
		// Create input test Http request
		MockHttpServletRequestBuilder mockHttpRequest = post("/services/register").content(buffer.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		// Apply test
		ResultActions testCase = this.mockMvc.perform(mockHttpRequest);
		// Validate test
		testCase.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("false"));
	}

	/**
	 * Test case validating the student registration service in a invalid case.<br>
	 * Invalid Firstname Lastname case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewStudentRegistrationCaseKO03() throws Exception {
		// Prepare an input object (test content)
		List<Integer> classesRegistered = new ArrayList<>();
		classesRegistered.add(1);
		classesRegistered.add(2);
		classesRegistered.add(3);
		CHStudent newStudent = new CHStudent();
		newStudent.setLastnameFirstname("");
		newStudent.setEmail("TestD@test.com");
		newStudent.setMotivation("My motivation is to validate that this test is not functional");
		newStudent.setPassword("XXXXXXXXXXXXXXX");
		newStudent.setClassesRegistered(classesRegistered);
		// Convert input object to JSON content
		StringWriter buffer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(buffer, newStudent);
		// Create input test Http request
		MockHttpServletRequestBuilder mockHttpRequest = post("/services/register").content(buffer.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		// Apply test
		ResultActions testCase = this.mockMvc.perform(mockHttpRequest);
		// Validate test
		testCase.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("false"));
	}
}
