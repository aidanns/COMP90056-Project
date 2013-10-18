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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		StringEqualityConstraint other = (StringEqualityConstraint) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value)) {
			return false;
		} else if (!field.equals(other.field)) {
			return false;
		}
		return true;
	}
	
	
}
