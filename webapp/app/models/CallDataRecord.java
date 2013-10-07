package models;

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
