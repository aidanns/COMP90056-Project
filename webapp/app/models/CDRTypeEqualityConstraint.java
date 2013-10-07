package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import models.CallDataRecord.CDRType;

/**
 * Constraint that compares against the CDRType field of a CDR.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class CDRTypeEqualityConstraint extends BaseComparisonConstraint {

	// TODO: Make instance variables private, but still persisted.
	
	@Enumerated(EnumType.STRING)
	public CallDataRecord.CDRType value;
	
	protected CDRTypeEqualityConstraint() {/* Hibernate */}
	
	/**
	 * Create a new CDRTypeEqualityConstraint.
	 * @param type The type that must be in the CDRType field for this constraint evaluate to true.
	 */
	public CDRTypeEqualityConstraint(CDRType type) {
		super(CallDataRecord.Field.CDRType);
		this.value = type;
	}
}
