package lu.codehackademy.demolabs.dal;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import lu.codehackademy.demolabs.transversal.util.Utilities;
import lu.codehackademy.demolabs.transversal.vo.CHClasses;
import lu.codehackademy.demolabs.transversal.vo.CHStudent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Class providing data access hiding data source type.<br>
 * We don't use a Inteface + Impl couple because it's just a example and data source will no be changed.<br>
 * BUT for a real prod application it's recommanded to use abstraction !
 * 
 * @author Dominique Righetto
 * @see "http://www.mkyong.com/spring/spring-jdbctemplate-querying-examples/"
 * 
 */
@SuppressWarnings("boxing")
@Component("dataProvider")
public class DataProvider {

	/** Data mgt helper */
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate = null;

	/**
	 * Extract all CH Classes informations
	 * 
	 * @return list of CH Classes informations as a list of value object
	 * @throws SQLException
	 */
	public List<CHClasses> retrieveAllClasses() throws SQLException {
		String query = "SELECT * FROM CH_CLASSES ORDER BY ID";
		List<CHClasses> chClasses = this.jdbcTemplate.query(query, new RowMapper<CHClasses>() {
			@Override
			public CHClasses mapRow(ResultSet rs, int rowNum) throws SQLException {
				CHClasses c = new CHClasses(rs.getInt("ID"), rs.getString("LABEL"));
				return c;
			}
		});
		return chClasses;
	}

	/**
	 * Add one or more students informations.
	 * 
	 * @param chStudents List of students to add
	 * @return A array of integer indicating the adding status for each student from the input collection (index matches)
	 * @throws SQLException
	 * @throws NoSuchAlgorithmException
	 */
	public int[] addStudents(final CHStudent... chStudents) throws SQLException, NoSuchAlgorithmException {
		int[] state = null;
		if ((chStudents != null) && (chStudents.length > 0)) {
			String query = "INSERT INTO CH_STUDENT(EMAIL, PASSWD, LASTNAME_FIRSTNAME, MOTIVATION, CLASSES_REGISTERED) VALUES(?,?,?,?,?)";
			// Hash password for each student infos passed
			for (CHStudent s : chStudents) {
				s.setPassword(Utilities.createPasswordHash(s.getPassword()));
			}
			// Insert students collection
			state = this.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CHStudent student = chStudents[i];
					ps.setString(1, student.getEmail());
					ps.setString(2, student.getPassword());
					ps.setString(3, student.getLastnameFirstname());
					ps.setString(4, student.getMotivation());
					if ((student.getClassesRegistered() != null) && !student.getClassesRegistered().isEmpty()) {
						String tmpStr = student.getClassesRegistered().toString().replaceAll("[", "").replaceAll("]", "").trim();
						ps.setString(5, tmpStr);
					} else {
						ps.setNull(5, Types.VARCHAR);
					}
				}

				@Override
				public int getBatchSize() {
					return chStudents.length;
				}
			});
		}
		return state;
	}

	/**
	 * Retrieve the profile informations of a student based on is email address
	 * 
	 * @param email Student email address
	 * @param loadPassword Flag to indicate if password must be loaded (is needed for Spring Security UserDetailsService)
	 * @return Student profile informations as value object
	 * @throws SQLException
	 */
	@SuppressWarnings("synthetic-access")
	public CHStudent retrieveStudent(final String email, final boolean loadPassword) throws SQLException {
		CHStudent student = null;
		if (StringUtils.isNoneBlank(email)) {
			String query = "SELECT * FROM CH_STUDENT WHERE EMAIL=?";
			student = this.jdbcTemplate.queryForObject(query, new Object[] { email }, new RowMapper<CHStudent>() {
				@Override
				public CHStudent mapRow(ResultSet rs, int rowNum) throws SQLException {
					CHStudent c = new CHStudent();
					c.setEmail(email);
					c.setLastnameFirstname(rs.getString("LASTNAME_FIRSTNAME"));
					c.setMotivation(rs.getString("MOTIVATION"));
					c.setPassword(((loadPassword) ? rs.getString("PASSWD") : "XXX"));
					c.setClassesRegistered(buildListOfClasses(rs.getString("CLASSES_REGISTERED")));
					return c;
				}
			});
		}
		return student;
	}

	/**
	 * Shortcut to {@link lu.codehackademy.demolabs.dal.DataProvider#retrieveStudent(java.lang.String, boolean) retrieveStudent(java.lang.String,boolean)} <br>
	 * but <b>without loading password (prefer use of this method to load student profile)</b>.
	 * 
	 * @param email Student email address
	 * @return Student profile informations as value object
	 * @throws SQLException
	 */
	public CHStudent retrieveStudent(final String email) throws SQLException {
		return this.retrieveStudent(email, false);
	}

	/**
	 * Check if a student email and password are present into storage
	 * 
	 * @param email Student email
	 * @param password Student hashed password
	 * @return TRUE is student credentials can be found FALSE otherwise
	 * @throws SQLException
	 * @throws NoSuchAlgorithmException
	 * @throws DataAccessException
	 */
	public boolean authenticate(final String email, final String password) throws SQLException, DataAccessException, NoSuchAlgorithmException {
		boolean isAuthenticated = false;
		if (StringUtils.isNoneBlank(email) && StringUtils.isNoneBlank(password)) {
			String query = "SELECT COUNT(EMAIL) FROM CH_STUDENT WHERE EMAIL=? AND PASSWD=?";
			Integer cnt = this.jdbcTemplate.queryForObject(query, Integer.class, email, Utilities.createPasswordHash(password));
			if (cnt == 1) {
				isAuthenticated = true;
			}

		}
		return isAuthenticated;
	}

	/**
	 * Update list of classes registered for a student
	 * 
	 * @param email target student email for which classes registration must be updated
	 * @param classesRegistered New set of classes of which the user is registered
	 * @throws SQLException
	 */
	public void updateStudentClassesRegistered(String email, List<CHClasses> classesRegistered) throws SQLException {
		if (StringUtils.isNoneBlank(email) && (classesRegistered != null) && !classesRegistered.isEmpty()) {
			String query = "UPDATE CH_STUDENT SET CLASSES_REGISTERED=? WHERE EMAIL=?";
			StringBuilder buffer = new StringBuilder();
			for (CHClasses chc : classesRegistered) {
				buffer.append(chc.getSessionIdentifier()).append(",");
			}
			String tmpStrTrimed = buffer.toString().trim();
			String tmpStr = tmpStrTrimed.substring(0, tmpStrTrimed.lastIndexOf(',')).trim();
			int recordUpdatedCount = this.jdbcTemplate.update(query, tmpStr, email);
			if (recordUpdatedCount != 1) {
				throw new SQLException("Abnormal count of record updated (" + recordUpdatedCount + ") !");
			}
		}
	}

	/**
	 * Check is a student is flagged as admin user
	 * 
	 * @param student Student informations for which the ckeck must be performed
	 * @return TRUE only is the student is flagged as admin
	 * @throws SQLException
	 */
	public boolean isAdmin(CHStudent student) throws SQLException {
		boolean isFlaggedAdmin = false;
		if ((student != null) && StringUtils.isNoneBlank(student.getEmail())) {
			String query = "SELECT COUNT(EMAIL) FROM CH_ADMIN WHERE EMAIL=?";
			Integer cnt = this.jdbcTemplate.queryForObject(query, Integer.class, student.getEmail());
			if (cnt == 1) {
				isFlaggedAdmin = true;
			}
		}
		return isFlaggedAdmin;
	}

	/**
	 * Create a list of CH Classes identifier from a string of CH Classes identifier separated by a comma
	 * 
	 * @param str String of classes identifier separated by a comma
	 * @return a list of CH Classes identifier
	 */
	@SuppressWarnings("static-method")
	private List<Integer> buildListOfClasses(String str) {
		if ((str == null) || str.isEmpty()) {
			return new ArrayList<>();
		}
		String[] classesIds = str.replaceAll(" ", "").split(",");
		List<Integer> tmpLst = new ArrayList<>();
		for (String classesId : classesIds) {
			tmpLst.add(Integer.parseInt(classesId.trim()));
		}
		return tmpLst;
	}

}
