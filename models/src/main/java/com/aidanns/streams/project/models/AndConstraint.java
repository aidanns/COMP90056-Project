package com.aidanns.streams.project.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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
		ObjectNode object = JsonNodeFactory.instance.objectNode();
		object.put("type", "and");
		object.put("firstChild", firstChild.toJson());
		object.put("secondChild", secondChild.toJson());
		return object;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstChild == null) ? 0 : firstChild.hashCode());
		result = prime * result
				+ ((secondChild == null) ? 0 : secondChild.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AndConstraint other = (AndConstraint) obj;
		if (firstChild == null) {
			if (other.firstChild != null)
				return false;
		} else if (!firstChild.equals(other.firstChild))
			return false;
		if (secondChild == null) {
			if (other.secondChild != null)
				return false;
		} else if (!secondChild.equals(other.secondChild))
			return false;
		return true;
	}

	@Override
	public boolean matches(CallDataRecord cdr) {
		return firstChild.matches(cdr) && secondChild.matches(cdr);
	}
}
