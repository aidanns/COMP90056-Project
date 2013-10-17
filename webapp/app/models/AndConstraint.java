package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

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

	/*
	 * (non-Javadoc)
	 * @see models.Constraint#toJson()
	 */
	@Override
	public ObjectNode toJson() {
		ObjectNode object = Json.newObject();
		object.put("type", "and");
		object.put("firstChild", firstChild.toJson());
		object.put("secondChild", secondChild.toJson());
		return object;
	}
	
}
