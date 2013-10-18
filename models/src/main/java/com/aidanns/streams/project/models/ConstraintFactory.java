package com.aidanns.streams.project.models;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Factory class for all types of Constraints.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
public class ConstraintFactory {
	
	// TODO: Move this to another package and make it the facade.
	
	/**
	 * Used for composing constraints together.
	 */
	public static class ConstraintBuilder {
		
		// The current constraint that we're waiting to compose with.
		private Constraint _constraint;
		
		private ConstraintBuilder() {/* Don't let people make there own. */}
		
		/**
		 * Create a new ConstraintBuilder.
		 * @param constraint The initial constraint the builder should wrap.
		 */
		private ConstraintBuilder(Constraint constraint) {
			_constraint = constraint;
		}
		
		/**
		 * Compose the current constraint with another in a parent Or constraint.
		 * @param other The other constraint to compose this one with.
		 * @return An Or constraint with both the current constraint and the other as its children.
		 */
		public ConstraintBuilder or(ConstraintBuilder other) {
			_constraint = new OrConstraint(_constraint, other.get());
			return this;
		}
		
		/**
		 * Compose the current constraint with another in a parent And constraint.
		 * @param other The other constraint to compose this one with.
		 * @return An And constraint with both the current constraint and the other as its children.
		 */ 
		public ConstraintBuilder and(ConstraintBuilder other) {
			_constraint = new AndConstraint(_constraint, other.get());
			return this;
		}
		
		/**
		 * Get the constraint that is being wrapped.
		 * @return The constraint that is being wrapped.
		 */
		public Constraint get() {
			return _constraint;
		}
	}
	
	/**
	 * Make a constraint that matches CDRs with a given CDRType.
	 * @param type The CDRType that will be used to match CDRs.
	 * @return A constraint that will match CDRs with the given CDRType.
	 */
	public static ConstraintBuilder makeCDRTypeConstraint(CallDataRecord.CDRType type) {
		return new ConstraintBuilder(new StringEqualityConstraint(CallDataRecord.Field.CDRType, type.toString()));
	}
	
	/**
	 * Make a constraint that matches CDRs with a given IMSI.
	 * @param type The IMSI that will be used to match CDRs.
	 * @return A constraint that will match CDRs with the given IMSI.
	 */
	public static ConstraintBuilder makeIMSIConstraint(String IMSI) {
		return new ConstraintBuilder(new StringEqualityConstraint(CallDataRecord.Field.IMSI, IMSI));
	}
	
	/**
	 * Make a constraint that matches CDRs with a given IMEI.
	 * @param type The IMEI that will be used to match CDRs.
	 * @return A constraint that will match CDRs with the given IMEI.
	 */
	public static ConstraintBuilder makeIMEIConstraint(String IMEI) {
		return new ConstraintBuilder(new StringEqualityConstraint(CallDataRecord.Field.IMEI, IMEI));
	}
	
	/**
	 * Parse some JSON and extract a constraint tree. Returns null if the parse is not successful.
	 * @param jsonObject The JSON object containing the constraint tree.
	 * @return The parsed constraint tree.
	 */
	public static ConstraintBuilder fromJson(JsonNode jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		// Try and get the type of the constraint, return null if it's not set.
		String type = jsonObject.get("type") == null ? null : jsonObject.get("type").asText();
		if (type == null) {
			return null;
		}
		ConstraintBuilder childOne;
		ConstraintBuilder childTwo;
		// Return null if we can't find the needed children for a particular type of node.
		switch (type) {
		case "and":
			childOne = fromJson(jsonObject.get("firstChild"));
			childTwo = fromJson(jsonObject.get("secondChild"));
			if (childOne == null || childTwo == null) {
				return null;
			}
			return childOne.and(childTwo);
		case "or":
			childOne = fromJson(jsonObject.get("firstChild"));
			childTwo = fromJson(jsonObject.get("secondChild"));
			if (childOne == null || childTwo == null) {
				return null;
			}
			return childOne.or(childTwo);
		case "stringEquality":
			String fieldName = jsonObject.get("field") == null ? null : jsonObject.get("field").asText();
			String value = jsonObject.get("value") == null ? null : jsonObject.get("value").asText();
			if (fieldName == null || value == null) {
				return null;
			}
			return new ConstraintBuilder(new StringEqualityConstraint(CallDataRecord.Field.fromString(fieldName), value));
		}
		// If we don't recognise the type of the constraint, return null.
		return null;
	}

}
