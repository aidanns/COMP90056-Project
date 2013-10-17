package models;

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

}
