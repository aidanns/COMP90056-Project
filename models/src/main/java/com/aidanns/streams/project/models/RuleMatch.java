package com.aidanns.streams.project.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;
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
	
	@Basic
	public Date timestamp;
	
	@Basic
	public Long ruleId;

	/**
	 * Get a JSON representation of this Rule.
	 * @return A JSON representation of this rule.
	 */
	public ObjectNode toJson() {
		ObjectNode object = JsonNodeFactory.instance.objectNode();
		object.put("timestamp", _dateParser.format(timestamp));
		object.put("ruleId", ruleId);
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
		return match;
	}
}
