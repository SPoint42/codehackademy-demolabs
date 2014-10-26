package lu.codehackademy.demolabs.sal;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import lu.codehackademy.demolabs.dal.DataProvider;
import lu.codehackademy.demolabs.transversal.Constants;
import lu.codehackademy.demolabs.transversal.vo.CHClasses;
import lu.codehackademy.demolabs.transversal.vo.CHStudent;
import lu.codehackademy.demolabs.transversal.vo.CHStudentCollection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MVC controller providing REST style services. <br>
 * To access to services provided by this controller student must be authenticated unless registeration service.
 * 
 * @author Dominique Righetto
 * @see "http://docs.spring.io/spring/docs/4.1.1.RELEASE/spring-framework-reference/htmlsingle/#spring-web"
 * 
 */
@RestController
@RequestMapping("/services")
@Secured(Constants.STUDENT_SEC_ROLES)
@SuppressWarnings({ "static-method", "boxing" })
public class ServicesProviderController {
	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ServicesProviderController.class);

	/** Data accessor */
	@Autowired
	@Qualifier("dataProvider")
	private DataProvider dataProvider = null;

	/**
	 * Service to retrieve all CH available classes
	 * 
	 * @return Classes details as JSON representation
	 */
	@RequestMapping(value = "/classes", method = RequestMethod.GET, produces = Constants.JSON_MIME_TYPE)
	public List<CHClasses> availableClasses() {
		List<CHClasses> avClasses = null;

		try {
			avClasses = this.dataProvider.retrieveAllClasses();
		}
		catch (Exception e) {
			LOG.error("Error during retrieving of available classes !", e);
			avClasses = null;
		}

		return avClasses;
	}

	/**
	 * Service to retrieve if the current logged student is an site admin
	 * 
	 * @return Admin status as JSON representation
	 */
	@RequestMapping(value = "/isadmin", method = RequestMethod.GET, produces = Constants.JSON_MIME_TYPE)
	public boolean isadmin() {
		boolean isadmin = false;
		String email = null;

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			email = auth.getName();
			isadmin = auth.getAuthorities().contains(new SimpleGrantedAuthority(Constants.ADMIN_SEC_ROLES));
		}
		catch (Exception e) {
			LOG.error("Error during retrieving admin status of student profile '{}' !", email, e);
			isadmin = false;
		}

		return isadmin;
	}

	/**
	 * Service to retrieve the profile (student informations) for the the current connected student
	 * 
	 * @return Student informations as JSON representation
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET, produces = Constants.JSON_MIME_TYPE)
	public CHStudent profile() {
		CHStudent student = null;
		String email = null;
		try {
			// Get student from user connected
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			email = auth.getName();
			// Load student profile
			student = this.dataProvider.retrieveStudent(email);
		}
		catch (Exception e) {
			LOG.error("Error during retrieving of student profile '{}' !", email, e);
			student = null;
		}

		return student;
	}

	/**
	 * Service to update the list of classes for which the student is registered
	 * 
	 * @param classesRegistered List of classes for which the student is registered
	 * @return JSON representation indicating if update status
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = Constants.JSON_MIME_TYPE, consumes = Constants.JSON_MIME_TYPE)
	public boolean updateClassesRegistration(@RequestBody ArrayList<CHClasses> classesRegistered) {
		boolean updateStatus = false;
		String email = null;

		try {
			if ((classesRegistered != null) && !classesRegistered.isEmpty()) {

				// Get student from user connected
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				email = auth.getName();

				// Apply security check on input
				Validator val = Validation.buildDefaultValidatorFactory().getValidator();
				Set<ConstraintViolation<CHClasses>> constraintViolations = null;
				StringBuilder buffer = new StringBuilder();
				int violationTotalCount = 0;
				for (CHClasses chc : classesRegistered) {
					constraintViolations = val.validate(chc);
					violationTotalCount += constraintViolations.size();
					if (!constraintViolations.isEmpty()) {
						buffer.delete(0, buffer.length());
						buffer.append("Input validation failed for properties : ");
						for (ConstraintViolation<CHClasses> constraintViolation : constraintViolations) {
							buffer.append("'");
							buffer.append(constraintViolation.getPropertyPath());
							buffer.append("' ; ");
						}
						LOG.warn(buffer.toString());
						updateStatus = false;
					}
				}

				// Try update if no security violation has been found
				if (violationTotalCount == 0) {
					this.dataProvider.updateStudentClassesRegistered(email, classesRegistered);
					updateStatus = true;
				}
			}
		}
		catch (Exception e) {
			LOG.error("Error during update of classe registration for student '{}' !", email, e);
			updateStatus = false;
		}

		return updateStatus;
	}

	/**
	 * Service to add a new student
	 * 
	 * @param newStudent Informations of the new student to add
	 * @return JSON representation indicating the registration status
	 */
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = Constants.JSON_MIME_TYPE, consumes = Constants.JSON_MIME_TYPE)
	public boolean register(@RequestBody CHStudent newStudent) {
		boolean registrationSucceed = false;

		try {
			// [EPLVULN] No quality check on provided password in order to show risk of weak password
			// Apply security check on input
			Validator val = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<CHStudent>> constraintViolations = val.validate(newStudent);
			if (!constraintViolations.isEmpty()) {
				StringBuilder buffer = new StringBuilder("Input validation failed for properties : ");
				for (ConstraintViolation<CHStudent> constraintViolation : constraintViolations) {
					buffer.append("'");
					buffer.append(constraintViolation.getPropertyPath());
					buffer.append("' ; ");
				}
				LOG.warn(buffer.toString());
				registrationSucceed = false;
			} else {
				// Try adding
				newStudent.setClassesRegistered(new ArrayList<Integer>());// Override any existing set of classes defined
				int[] state = this.dataProvider.addStudents(newStudent);
				if ((state != null) && (state.length == 1) && (state[0] == 1)) {
					registrationSucceed = true;
				} else {
					registrationSucceed = false;
				}
			}
		}
		catch (Exception e) {
			LOG.error("Error during registration of a new student '{}' !", newStudent, e);
			registrationSucceed = false;
		}

		return registrationSucceed;
	}

	/**
	 * Service to import several students using XML content source
	 * 
	 * @param xmlContent XML content
	 * @return JSON representation indicating the import status for each student from the XML (Student position + Import status (boolean))
	 */
	@Secured(Constants.ADMIN_SEC_ROLES)
	@RequestMapping(value = "/import", method = RequestMethod.POST, produces = Constants.JSON_MIME_TYPE, consumes = Constants.FORM_URLENCODED_MIME_TYPE)
	public Map<Integer, Boolean> importStudent(@RequestParam(required = true, value = "XML") String xmlContent) {
		Map<Integer, Boolean> importStatus = null;

		try {
			// Check presence of input XML
			if (StringUtils.isNoneBlank(xmlContent)) {
				// Decode input XML
				String xml = URLDecoder.decode(xmlContent, "utf8");
				// [EPLVULN] Do not check XML content and enable JAXB External Entities and DTD supports configuration in order to allow XXE
				XMLInputFactory factory = XMLInputFactory.newInstance();
				factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
				factory.setProperty(XMLInputFactory.SUPPORT_DTD, true);
				XMLStreamReader xmlStreamReader = factory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes()));
				// Deserialize XML to Objects through JAXB annoted POJO
				JAXBContext context = JAXBContext.newInstance(CHStudentCollection.class, CHStudent.class);
				Unmarshaller handler = context.createUnmarshaller();
				CHStudentCollection studentsColl = (CHStudentCollection) handler.unmarshal(xmlStreamReader);
				List<CHStudent> refColl = studentsColl.getStudents();
				// Apply security check on students
				Validator val = Validation.buildDefaultValidatorFactory().getValidator();
				Set<ConstraintViolation<CHStudent>> constraintViolations = null;
				StringBuilder buffer = new StringBuilder();
				int violationTotalCount = 0;
				for (CHStudent c : refColl) {
					constraintViolations = val.validate(c);
					violationTotalCount += constraintViolations.size();
					if (!constraintViolations.isEmpty()) {
						buffer.delete(0, buffer.length());
						buffer.append("Input validation failed for properties : ");
						for (ConstraintViolation<CHStudent> constraintViolation : constraintViolations) {
							buffer.append("'");
							buffer.append(constraintViolation.getPropertyPath());
							buffer.append("' ; ");
						}
						LOG.warn(buffer.toString());
					}
				}
				// Add students if all security check passed
				if (violationTotalCount == 0) {
					if (!refColl.isEmpty()) {
						// Override any existing set of classes defined
						for (CHStudent c : refColl) {
							c.setClassesRegistered(new ArrayList<Integer>());
						}
						// Add
						int[] state = this.dataProvider.addStudents(refColl.toArray(new CHStudent[refColl.size()]));
						// Check adding
						importStatus = new HashMap<>(refColl.size());
						for (int x = 0; x < refColl.size(); x++) {
							importStatus.put(x + 1, ((state[x] == 1) ? true : false));
						}
					}
				} else {
					LOG.info("Students collection not added because all security check do not passed !");
					importStatus = new HashMap<>(refColl.size());
					for (int x = 0; x < refColl.size(); x++) {
						importStatus.put(x + 1, false);
					}
				}

			}
		}
		catch (Exception e) {
			LOG.error("Error during import of a new students !", e);
		}

		return importStatus;
	}

}
