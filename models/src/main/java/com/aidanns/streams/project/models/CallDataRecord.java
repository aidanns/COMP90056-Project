package com.aidanns.streams.project.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a GSM Call Data Record.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
public class CallDataRecord {
	
	/**
	 * GSM CDR fields according to the specification.
	 */
	public enum Field {
		CDRType,
		IMSI,
		IMEI,
		CallingNumber,
		CalledNumber,
		RecordingEnttiy,
		Location,
		CallReference,
		CallDuration,
		AnswerTime,
		SeizureTime,
		ReleaseTime,
		CauseForTermination,
		BasicService,
		MSCAddress;
		
		// Map from the title of the field to the Field itself.
		private static Map<String, Field> _stringToFieldMap = new HashMap<String, Field>();
		
		static {
			// Populate a map from field title to Field.
			for (Field f : Field.values()) {
				_stringToFieldMap.put(f.toString(), f);
			}
		}
		
		/**
		 * Retrieve the field for a given title.
		 * @param s The title of the field.
		 * @return The Field itself.
		 */
		public static Field fromString(String s) {
			return _stringToFieldMap.get(s);
		}
		
	}
	
	/**
	 * Possible contents of the CDRType field according to the GSM CDR specification.
	 */
	public enum CDRType {
		MobileOriginatedCall("MOC"),
		MobileTerminatedCall("MTC");
		
		/** The abbreviation that's used in the GSM CDR. */
		String _abbreviation;
		
		CDRType(String abbreviation) {
			_abbreviation = abbreviation;
		}
		
		public String getAbbreviation() {
			return _abbreviation;
		}
	}
	
	/**
	 * Possible contenst of the cause for termination field according to the GSM CDR specification.
	 */
	public enum CauseForTermination {
		Normal("00"),
		AbnormallyDropped("04"),
		UnsuccessfulAttempt("03");
		
		/** The abbreviation that's used in the GSM CDR. */
		String _abbreviation;
		
		CauseForTermination(String abbreviation) {
			_abbreviation = abbreviation;
		}
	}
}
