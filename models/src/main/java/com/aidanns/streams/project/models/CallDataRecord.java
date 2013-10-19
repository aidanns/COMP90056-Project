package com.aidanns.streams.project.models;

import java.util.Date;
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
		RecordingEntitiy,
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
		
		// Map from the abbreviation to the CDRType itself.
		private static Map<String, CDRType> _abbreviationToTypeMap = new HashMap<String, CDRType>();
		
		static {
			// Populate a map from abbreviation to CDRType.
			for (CDRType t : CDRType.values()) {
				_abbreviationToTypeMap.put(t.getAbbreviation(), t);
			}
		}
		
		/**
		 * Retrieve the CDRType for a given abbreviation.
		 * @param abbreviation The abbreviation for the CDRType.
		 * @return The CDRType itself.
		 */
		public static CDRType fromAbbreviation(String abbreviation) {
			return _abbreviationToTypeMap.get(abbreviation);
		}
	}
	
	/**
	 * Possible content of the cause for termination field according to the GSM CDR specification.
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
		
		public String getAbbreviation() {
			return _abbreviation;
		}
		
		// Map from the string to the CauseForTermination itself.
		private static Map<String, CauseForTermination> _stringToCauseForTerminationMap = new HashMap<String, CauseForTermination>();
		
		static {
			for (CauseForTermination c : CauseForTermination.values()) {
				_stringToCauseForTerminationMap.put(c.getAbbreviation(), c);
			}
		}
		
		/**
		 * Get the CauseForTermination represented by a particular abbreviation.
		 * @param abbreviation The abbreviation to source a CauseForTermination from.
		 * @return The CauseForTermination.
		 */
		public static CauseForTermination fromAbbreviation(String abbreviation) {
			return _stringToCauseForTerminationMap.get(abbreviation);
		}
	}
	
	private CDRType _cdrType;
	private String _imsi;
	private String _imei;
	private String _callingNumber;
	private String _calledNumber;
	private String _recordingEntity;
	private String _location;
	private String _callReference;
	private Float _callDuration;
	private Date _answerTime;
	private Date _seizureTime;
	private Date _releaseTime;
	private CauseForTermination _causeForTermination;
	private String _basicService;
	private String _mscAddress;
	
	/**
	 * Create a new CallDataRecord. See the GSM specification for details of the contents of each field.
	 * @param _cdrType
	 * @param _imsi
	 * @param _imei
	 * @param _callingNumber
	 * @param _calledNumber
	 * @param _recordingEntity
	 * @param _location
	 * @param _callReference
	 * @param _callDuration
	 * @param _answerTime
	 * @param _seizureTime
	 * @param _releaseTime
	 * @param _causeForTermination
	 * @param _basicService
	 * @param _mscAddress
	 */
	public CallDataRecord(CDRType _cdrType, String _imsi, String _imei,
			String _callingNumber, String _calledNumber,
			String _recordingEntity, String _location, String _callReference,
			Float _callDuration, Date _answerTime, Date _seizureTime,
			Date _releaseTime, CauseForTermination _causeForTermination,
			String _basicService, String _mscAddress) {
		super();
		this._cdrType = _cdrType;
		this._imsi = _imsi;
		this._imei = _imei;
		this._callingNumber = _callingNumber;
		this._calledNumber = _calledNumber;
		this._recordingEntity = _recordingEntity;
		this._location = _location;
		this._callReference = _callReference;
		this._callDuration = _callDuration;
		this._answerTime = _answerTime;
		this._seizureTime = _seizureTime;
		this._releaseTime = _releaseTime;
		this._causeForTermination = _causeForTermination;
		this._basicService = _basicService;
		this._mscAddress = _mscAddress;
	}

	public CDRType get_cdrType() {
		return _cdrType;
	}

	public String get_imsi() {
		return _imsi;
	}

	public String get_imei() {
		return _imei;
	}

	public String get_callingNumber() {
		return _callingNumber;
	}

	public String get_calledNumber() {
		return _calledNumber;
	}

	public String get_recordingEntity() {
		return _recordingEntity;
	}

	public String get_location() {
		return _location;
	}

	public String get_callReference() {
		return _callReference;
	}

	public Float get_callDuration() {
		return _callDuration;
	}

	public Date get_answerTime() {
		return _answerTime;
	}

	public Date get_seizureTime() {
		return _seizureTime;
	}

	public Date get_releaseTime() {
		return _releaseTime;
	}

	public CauseForTermination get_causeForTermination() {
		return _causeForTermination;
	}

	public String get_basicService() {
		return _basicService;
	}

	public String get_mscAddress() {
		return _mscAddress;
	}

	@Override
	public String toString() {
		return "CallDataRecord [_cdrType=" + _cdrType + ", _imsi=" + _imsi
				+ ", _imei=" + _imei + ", _callingNumber=" + _callingNumber
				+ ", _calledNumber=" + _calledNumber + ", _recordingEntity="
				+ _recordingEntity + ", _location=" + _location
				+ ", _callReference=" + _callReference + ", _callDuration="
				+ _callDuration + ", _answerTime=" + _answerTime
				+ ", _seizureTime=" + _seizureTime + ", _releaseTime="
				+ _releaseTime + ", _causeForTermination="
				+ _causeForTermination + ", _basicService=" + _basicService
				+ ", _mscAddress=" + _mscAddress + "]";
	}
}
