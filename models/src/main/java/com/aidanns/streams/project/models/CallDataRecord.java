package com.aidanns.streams.project.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Representation of a GSM Call Data Record.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
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
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Basic private CDRType _cdrType;
	@Basic private String _imsi;
	@Basic private String _imei;
	@Basic private String _callingNumber;
	@Basic private String _calledNumber;
	@Basic private String _recordingEntity;
	@Basic private String _location;
	@Basic private String _callReference;
	@Basic private Float _callDuration;
	@Basic private Date _answerTime;
	@Basic private Date _seizureTime;
	@Basic private Date _releaseTime;
	@Basic private CauseForTermination _causeForTermination;
	@Basic private String _basicService;
	@Basic private String _mscAddress;
	
	static private SimpleDateFormat _dateParser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	public ObjectNode toJson() {
		ObjectNode object = JsonNodeFactory.instance.objectNode();
		object.put("cdrType", _cdrType.getAbbreviation());
		object.put("imsi", _imsi);
		object.put("imei", _imei);
		object.put("callingNumber", _callingNumber);
		object.put("calledNumber", _calledNumber);
		object.put("recordingEntity", _recordingEntity);
		object.put("location", _location);
		object.put("callReference", _callReference);
		object.put("callDuration", _callDuration);
		object.put("answerTime", _answerTime == null ? null : _dateParser.format(_answerTime));
		object.put("seizureTime", _seizureTime == null ? null : _dateParser.format(_seizureTime));
		object.put("releaseTime", _releaseTime == null ? null : _dateParser.format(_releaseTime));
		object.put("causeForTermination", _causeForTermination.getAbbreviation());
		object.put("basicService", _basicService);
		object.put("mscAddress", _mscAddress);
		return object;
	}
	
	public static CallDataRecord fromJson(JsonNode jsonObject) {
		Long id = jsonObject.get("id") == null ? null : jsonObject.get("id").asLong();
		CDRType cdrType = jsonObject.get("cdrType") == null ? null : CDRType.fromAbbreviation(jsonObject.get("cdrType").asText());
		String imsi = jsonObject.get("imsi") == null ? null : jsonObject.get("imsi").asText();
		String imei = jsonObject.get("imei") == null ? null : jsonObject.get("imei").asText();
		String callingNumber = jsonObject.get("callingNumber") == null ? null : jsonObject.get("callingNumber").asText();
		String calledNumber = jsonObject.get("calledNumber") == null ? null : jsonObject.get("calledNumber").asText();
		String recordingEntity = jsonObject.get("recordingEntity") == null ? null : jsonObject.get("recordingEntity").asText();
		String location = jsonObject.get("location") == null ? null : jsonObject.get("location").asText();
		String callReference = jsonObject.get("callReference") == null ? null : jsonObject.get("callReference").asText();
		Double callDuration = jsonObject.get("callDuration") == null ? null : jsonObject.get("callDuration").asDouble();
		Date answerTime;
		try {
			answerTime = jsonObject.get("answerTime") == null ? null : _dateParser.parse(jsonObject.get("answerTime").asText());
		} catch (ParseException e) {
			answerTime = null;
		}
		Date seizureTime;
		try {
			seizureTime = jsonObject.get("seizureTime") == null ? null : _dateParser.parse(jsonObject.get("seizureTime").asText());
		} catch (ParseException e) {
			seizureTime = null;
		}
		Date releaseTime;
		try {
			releaseTime = jsonObject.get("releaseTime") == null ? null : _dateParser.parse(jsonObject.get("releaseTime").asText());
		} catch (ParseException e) {
			releaseTime = null;
		}
		CauseForTermination causeForTermination = jsonObject.get("causeForTermination") == null ? null : CauseForTermination.fromAbbreviation(jsonObject.get("causeForTermination").asText());
		String basicService = jsonObject.get("basicService") == null ? null : jsonObject.get("basicService").asText();
		String mscAddress = jsonObject.get("mscAddress") == null ? null : jsonObject.get("mscAddress").asText();
		
		if (cdrType == null || imsi == null || imei == null || callingNumber == null || calledNumber == null
				|| recordingEntity == null || location == null || callReference == null || callDuration == null
				|| answerTime == null || releaseTime == null || causeForTermination == null || basicService == null
				|| mscAddress == null) {
			// seizureTime may be null, but everything else must be filled in.
			CallDataRecord cdr = new CallDataRecord(cdrType, imsi, imei, callingNumber, calledNumber, 
					recordingEntity, location, callReference, callDuration.floatValue(), answerTime, 
					seizureTime, releaseTime, causeForTermination, basicService, mscAddress);
			System.out.println(cdr.toString());
			return null;
		} else {
			CallDataRecord cdr = new CallDataRecord(cdrType, imsi, imei, callingNumber, calledNumber, 
					recordingEntity, location, callReference, callDuration.floatValue(), answerTime, 
					seizureTime, releaseTime, causeForTermination, basicService, mscAddress);
			cdr.id = id;
			return cdr;
			
		}
	}
	
	public CallDataRecord() { /* Needed for Hibernate. */ };
	
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

	public CDRType cdrType() {
		return _cdrType;
	}

	public String imsi() {
		return _imsi;
	}

	public String imei() {
		return _imei;
	}

	public String callingNumber() {
		return _callingNumber;
	}

	public String calledNumber() {
		return _calledNumber;
	}

	public String recordingEntity() {
		return _recordingEntity;
	}

	public String location() {
		return _location;
	}

	public String callReference() {
		return _callReference;
	}

	public Float callDuration() {
		return _callDuration;
	}

	public Date answerTime() {
		return _answerTime;
	}

	public Date seizureTime() {
		return _seizureTime;
	}

	public Date releaseTime() {
		return _releaseTime;
	}

	public CauseForTermination causeForTermination() {
		return _causeForTermination;
	}

	public String basicService() {
		return _basicService;
	}

	public String mscAddress() {
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
