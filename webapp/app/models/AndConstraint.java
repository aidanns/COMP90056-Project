package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * A constraint that evaluates to true if it's two children both evaluate to true.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class AndConstraint extends Constraint {
	
	// TODO: Make instance variables private, but still persisted.
	
	/** The first child. */
	@OneToOne
	@Cascade({CascadeType.ALL})
	public Constraint firstChild;
	
	/** The second child. */
	@OneToOne
	@Cascade({CascadeType.ALL})
	public Constraint secondChild;
	
	protected AndConstraint() {/* Hibernate. */}

	/**
	 * Create an AndConstraint.
	 * @param firstChild First child.
	 * @param secondChild Second child.
	 */
	public AndConstraint(Constraint firstChild, Constraint secondChild) {
		this.firstChild = firstChild;
		this.secondChild = secondChild;
	}
	
}
