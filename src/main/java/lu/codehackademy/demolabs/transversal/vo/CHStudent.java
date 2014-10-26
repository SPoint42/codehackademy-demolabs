package lu.codehackademy.demolabs.transversal.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.righettod.hvsc.annotation.CheckContent;

/**
 * Value object storing information about a Student.<br>
 * JAXB annotations has been added to support Java-XML serailzation/deserialization.
 * 
 * @author Dominique Righetto
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Student")
public class CHStudent implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 5L;

	/**
	 * Annotation to define security input validation rules to apply
	 */
	@CheckContent(continuousRepetitionLimitationMapJsonExpr = "{\"-\":1,\"_\":1,\".\":1,\"@\":1}", whitelistIdentifier = "email", whitelistLocale = "en")
	@Length(max = 100, min = 6)
	@Email
	@NotEmpty
	@XmlElement(name = "Email", nillable = false, required = true)
	private String email = null;

	/**
	 * Annotation to define security input validation rules to apply
	 */
	@CheckContent(continuousRepetitionLimitationMapJsonExpr = "{\"-\":1}", whitelistIdentifier = "naming", whitelistLocale = "en")
	@Length(max = 100, min = 6)
	@NotEmpty
	@XmlElement(name = "LastnameFirstname", nillable = false, required = true)
	private String lastnameFirstname = null;

	/**
	 * Annotation to define security input validation rules to apply
	 */
	@CheckContent(continuousRepetitionLimitationMapJsonExpr = "{}", whitelistIdentifier = "password", whitelistLocale = "en")
	@Length(max = 200, min = 8)
	@NotEmpty
	@XmlElement(name = "Password", nillable = false, required = true)
	private String password = null;

	/**
	 * Annotation to define security input validation rules to apply <br>
	 * [EPLVULN] : Explicit vulnerability in order to allow injection (no content check)
	 */
	@Length(max = 4000, min = 10)
	@NotEmpty
	@XmlElement(name = "Motivation", nillable = false, required = true)
	private String motivation = null;

	private List<Integer> classesRegistered = null;

	/**
	 * Getter
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Getter
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Setter
	 * 
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter
	 * 
	 * @return the lastnameFirstname
	 */
	public String getLastnameFirstname() {
		return this.lastnameFirstname;
	}

	/**
	 * Setter
	 * 
	 * @param lastnameFirstname the lastnameFirstname to set
	 */
	public void setLastnameFirstname(String lastnameFirstname) {
		this.lastnameFirstname = lastnameFirstname;
	}

	/**
	 * Getter
	 * 
	 * @return the motivation
	 */
	public String getMotivation() {
		return this.motivation;
	}

	/**
	 * Setter
	 * 
	 * @param motivation the motivation to set
	 */
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	/**
	 * Getter
	 * 
	 * @return the classesRegistered
	 */
	public List<Integer> getClassesRegistered() {
		return this.classesRegistered;
	}

	/**
	 * Setter
	 * 
	 * @param classesRegistered the classesRegistered to set
	 */
	public void setClassesRegistered(List<Integer> classesRegistered) {
		this.classesRegistered = classesRegistered;
	}

	/**
	 * Setter
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.email == null) ? 0 : this.email.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CHStudent)) {
			return false;
		}
		CHStudent other = (CHStudent) obj;
		if (this.email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!this.email.equals(other.email)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CHStudent [" + (this.email != null ? "email=" + this.email + ", " : "") + (this.lastnameFirstname != null ? "lastnameFirstname=" + this.lastnameFirstname + ", " : "")
				+ (this.password != null ? "password=" + this.password + ", " : "") + (this.motivation != null ? "motivation=" + this.motivation + ", " : "")
				+ (this.classesRegistered != null ? "classesRegistered=" + this.classesRegistered : "") + "]";
	}

}
