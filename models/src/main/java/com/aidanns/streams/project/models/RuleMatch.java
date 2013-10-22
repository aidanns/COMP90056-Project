package com.aidanns.streams.project.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a match between a rule and a set of CDRs.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class RuleMatch {
	
	static private SimpleDateFormat _dateParser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	/** Database Id for the match. */
	@Id
	@GeneratedValue
	public Long id;
	
	/** The IMSI of the user that the match was for. */
	@Basic
	public String imsi;
	
	@Basic
	public Date timestamp;
	
	@Basic
	public Long ruleId;
	
	/** CDRs that cased this match. */
	@OneToMany
	@Cascade({CascadeType.ALL})
	public List<CallDataRecord> callDataRecords = new ArrayList<CallDataRecord>();

	/**
	 * Get a JSON representation of this Rule.
	 * @return A JSON representation of this rule.
	 */
	public ObjectNode toJson() {
		ObjectNode object = JsonNodeFactory.instance.objectNode();
		object.put("timestamp", _dateParser.format(timestamp));
		object.put("ruleId", ruleId);
		object.put("imsi", imsi);
		object.put("id", id);
		
		ArrayNode array = JsonNodeFactory.instance.arrayNode();
		for (CallDataRecord cdr : callDataRecords) {
			array.add(cdr.toJson());
		}
		
		object.put("callDataRecords", array);
		
		return object;
	}
	
	/**
	 * Get a RuleMatch from JSON.
	 * @param jsonObject The JSON to parse.
	 * @return The Rule.
	 */
	public static RuleMatch fromJson(JsonNode jsonObject) {
		RuleMatch match = new RuleMatch();
		try {
			match.timestamp = jsonObject.get("timestamp") == null ? null : _dateParser.parse(jsonObject.get("timestamp").asText());
		} catch (ParseException e) {
			match.timestamp = null;
		}
		match.ruleId = jsonObject.get("ruleId") == null ? null : jsonObject.get("ruleId").asLong();
		match.imsi = jsonObject.get("imsi") == null ? null : jsonObject.get("imsi").asText();
		JsonNode a = jsonObject.get("callDataRecords") == null ? null : jsonObject.get("callDataRecords");
		
		if (a != null && a.isArray()) {
			for (int i = 0; i < a.size(); i++) {
				CallDataRecord cdr = CallDataRecord.fromJson(a.get(i));
				if (cdr != null) {
					match.callDataRecords.add(cdr);
				}
			}
		}
		
		if (!match.validate()) {
			return null;
		}
		return match;
	}
	
	private boolean validate() {
		return timestamp != null && ruleId != null && callDataRecords.size() > 0 && imsi != null;
	}
}
