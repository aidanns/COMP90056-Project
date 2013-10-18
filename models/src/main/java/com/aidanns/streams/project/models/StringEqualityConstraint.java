package com.aidanns.streams.project.models;

import javax.persistence.Basic;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	
	/*
	 * (non-Javadoc)
	 * @see models.Constraint#toJson()
	 */
	@Override
	public ObjectNode toJson() {
		ObjectNode object = JsonNodeFactory.instance.objectNode();
		object.put("type", "stringEquality");
		object.put("field", field.toString());
		object.put("value", value);
		return object;
	}
}
