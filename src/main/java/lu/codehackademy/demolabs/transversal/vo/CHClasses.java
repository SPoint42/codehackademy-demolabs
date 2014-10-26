package lu.codehackademy.demolabs.transversal.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.github.righettod.hvsc.annotation.CheckContent;

/**
 * Value object storing informatoin about a Classes.
 * 
 * @author Dominique Righetto
 * 
 */
public class CHClasses {

	/**
	 * Annotation to define security input validation rules to apply
	 */
	@Range(min = 1, max = 10)
	private int sessionIdentifier = 1;

	/**
	 * Annotation to define security input validation rules to apply
	 */
	@CheckContent(continuousRepetitionLimitationMapJsonExpr = "{\"-\":1,\".\":1,\"'\":1,\"(\":1,\")\":1}", whitelistIdentifier = "freetext", whitelistLocale = "en")
	@Length(max = 100, min = 0)
	private String label = "";

	/**
	 * Constructor
	 * 
	 * @param sessionIdentifier Classes Identifier
	 * @param label Classes label
	 */
	public CHClasses(int sessionIdentifier, String label) {
		super();
		this.sessionIdentifier = sessionIdentifier;
		this.label = label;
	}

	/**
	 * Constructor
	 */
	public CHClasses() {
		super();
	}

	/**
	 * Getter
	 * 
	 * @return the sessionIdentifier
	 */
	public int getSessionIdentifier() {
		return this.sessionIdentifier;
	}

	/**
	 * Setter
	 * 
	 * @param sessionIdentifier the sessionIdentifier to set
	 */
	public void setSessionIdentifier(int sessionIdentifier) {
		this.sessionIdentifier = sessionIdentifier;
	}

	/**
	 * Setter
	 * 
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Getter
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.sessionIdentifier;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CHClasses)) {
			return false;
		}
		CHClasses other = (CHClasses) obj;
		if (this.sessionIdentifier != other.sessionIdentifier) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CHClasses [sessionIdentifier=" + this.sessionIdentifier + ", " + (this.label != null ? "label=" + this.label : "") + "]";
	}

}
