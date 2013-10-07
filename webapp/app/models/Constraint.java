package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Representation of a constraint that can be used to match certain CDRs.
 * If the constraint evaluated to true for a given CDR, it is said to match.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Constraint {
	
	// TODO: Make instance variables private, but still persisted.
	
	@Id
	@GeneratedValue
	public Long id;
	
}
