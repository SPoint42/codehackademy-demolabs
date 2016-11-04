package lu.codehackademy.demolabs.sal.sut;

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
 * Security unit tests cases (test resistance against differents malicious input excluding any business validation) for class "lu.codehackademy.demolabs.sal.ServicesProviderController".<br>
 * Explicitly let methods not tested in order to show an "Unit test code coverage quality issue"
 * 
 * @author Dominique RIGHETTO
 * @see "http://docs.spring.io/spring/docs/current/spring-framework-reference/html/testing.html"
 * @see "http://docs.spring.io/spring/docs/4.0.0.RELEASE/javadoc-api/org/springframework/test/web/servlet/MockMvc.html"
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/resources/springmvc-context.xml")
public class ServicesProviderControllerSecurityTestCase {
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
	 * Test case validating the student registration service against JavaScript injection in firstname / lastname field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewStudentRegistrationCaseInjection01() throws Exception {
		// Prepare an input object (test content)
		List<Integer> classesRegistered = new ArrayList<>();
		classesRegistered.add(1);
		classesRegistered.add(2);
		classesRegistered.add(3);
		CHStudent newStudent = new CHStudent();
		newStudent.setLastnameFirstname("<script>alert(1)</script>");
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
		testCase.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("false"));
	}

	/**
	 * Test case validating the student registration service against JavaScript injection in email field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewStudentRegistrationCaseInjection02() throws Exception {
		// Prepare an input object (test content)
		List<Integer> classesRegistered = new ArrayList<>();
		classesRegistered.add(1);
		classesRegistered.add(2);
		classesRegistered.add(3);
		CHStudent newStudent = new CHStudent();
		newStudent.setLastnameFirstname("Security Test Case B");
		newStudent.setEmail("<script>alert(1)</script>");
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
		testCase.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("false"));
	}

	
}
