package models;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Represents a Rule that may match a given CDR.
 * 
 * Rules are said to match if their constraint matches a certain number of
 * CDRs within a certain window of time.
 * 
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class Rule {

	/** Database Id for the rule. */
	@Id
	@GeneratedValue
	public Long id;
	
	/** Human-readable name for the rule. */
	@Basic
	public String name;
	
	/** Whether or not the current rule should be matching CDRs. */
	@Basic
	public Boolean active;
	
	/** The constraint that is used to match CDRs by this rule. */
	@OneToOne
	@Cascade({CascadeType.ALL})
	public Constraint constraint;
	
	/** Number of seconds to use as the window size for this rule. */
	@Basic
	public Integer windowSize;
	
	/** Number of times the constraint has to match in a single window for the rule to match. */
	@Basic 
	public Integer numberOfConstraintMatches;
	
}
