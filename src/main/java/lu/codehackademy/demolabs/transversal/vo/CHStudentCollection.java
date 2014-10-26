package lu.codehackademy.demolabs.transversal.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Value object storing information about a set of student.<br>
 * JAXB annotations has been added to support Java-XML serailzation/deserialization.
 * 
 * @author Dominique Righetto
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Students")
public class CHStudentCollection implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Set of students
	 */
	@XmlElement(name = "Student", nillable = false)
	private List<CHStudent> students = null;

	/**
	 * Getter
	 * 
	 * @return the students
	 */
	public List<CHStudent> getStudents() {
		return this.students;
	}

	/**
	 * Setter
	 * 
	 * @param students the students to set
	 */
	public void setStudents(List<CHStudent> students) {
		this.students = students;
	}

}
