package models;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Constraint that matches the contents of a GSM CDR field based on string equality.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class StringEqualityConstraint extends BaseComparisonConstraint {

	/** The string that we're matching against. */
	@Basic
	public String value;
	
	protected StringEqualityConstraint() {/* Hibernate. */}
	
	/** Create a new StringEqualityConstraint.
	 * @param field The field to match on.
	 * @param value The value to match.
	 */
	public StringEqualityConstraint(CallDataRecord.Field field, String value) {
		super(field);
		this.value = value;
	}
}
