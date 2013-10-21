package com.aidanns.streams.project.models;

import java.text.SimpleDateFormat;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Transient;

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
	
	@Transient
	private SimpleDateFormat _dateParser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	@Override
	public boolean matches(CallDataRecord cdr) {
		switch(this.field) {
		case AnswerTime:
			return value.equals(_dateParser.format(cdr.answerTime()));
		case BasicService:
			return value.equals(cdr.basicService());
		case CDRType:
			return value.equals(cdr.cdrType().getAbbreviation());
		case CallDuration:
			return value.equals(cdr.callDuration().toString());
		case CallReference:
			return value.equals(cdr.callReference());
		case CalledNumber:
			return value.equals(cdr.calledNumber());
		case CallingNumber:
			return value.equals(cdr.callingNumber());
		case CauseForTermination:
			return value.equals(cdr.causeForTermination().getAbbreviation());
		case IMEI:
			return value.equals(cdr.imei());
		case IMSI:
			return value.equals(cdr.imsi());
		case Location:
			return value.equals(cdr.location());
		case MSCAddress:
			return value.equals(cdr.mscAddress());
		case RecordingEntitiy:
			return value.equals(cdr.recordingEntity());
		case ReleaseTime:
			return value.equals(_dateParser.format(cdr.releaseTime()));
		case SeizureTime:
			return value.equals(_dateParser.format(cdr.seizureTime()));
		default:
			return false;
		
		}
	}
	
	
}
