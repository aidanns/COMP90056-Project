package com.aidanns.streams.project.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A constraint that evaluates to true if either of it's children evaluate to true.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class OrConstraint extends Constraint {

	/** The first child. */
	@OneToOne
	@Cascade({CascadeType.ALL})
	public Constraint firstChild;
	
	/** The second child. */
	@OneToOne
	@Cascade({CascadeType.ALL})
	public Constraint secondChild;
	
	protected OrConstraint() {/* Hibernate. */}
	
	/**
	 * Create an OrConstraint.
	 * @param firstChild First child.
	 * @param secondChild Second child.
	 */
	public OrConstraint(Constraint firstChild, Constraint secondChild) {
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
		object.put("type", "or");
		object.put("firstChild", firstChild.toJson());
		object.put("secondChild", secondChild.toJson());
		return object;
	}

}
