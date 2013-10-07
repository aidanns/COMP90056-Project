package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Base class for all constraints that do comparisons against a certain field in the CDR.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public abstract class BaseComparisonConstraint extends Constraint {
	
	// TODO: Make instance variables private, but still persisted.

	/** The CDR Field that the constraint will work against. */
	@Enumerated(EnumType.STRING)
	public CallDataRecord.Field field;
	
	protected BaseComparisonConstraint() {/* Hibernate. */}
	
	/**
	 * Create a new BaseComparisonConstraint.
	 * @param field The field that the comparison will be made against.
	 */
	public BaseComparisonConstraint(CallDataRecord.Field field) {
		this.field = field;
	}
}
