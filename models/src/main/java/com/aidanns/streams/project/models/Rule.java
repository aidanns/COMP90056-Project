package com.aidanns.streams.project.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a Rule that may match a given CDR.
 * 
 * Rules are said to match if their constraint matches a certain number of
 * CDRs within a certain window of time.
 * 
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class Rule {

	/** Database Id for the rule. */
	@Id
	@GeneratedValue
	public Long id;
	
	/** Human-readable name for the rule. */
	@Basic
	public String name;
	
	/** Whether or not the current rule should be matching CDRs. */
	@Basic
	public Boolean active;
	
	/** The constraint that is used to match CDRs by this rule. */
	@OneToOne
	@Cascade({CascadeType.ALL})
	public Constraint constraint;
	
	/** Number of seconds to use as the window size for this rule. */
	@Basic
	public Integer windowSize;
	
	/** Number of times the constraint has to match in a single window for the rule to match. */
	@Basic 
	public Integer numberOfConstraintMatches;
	
	// Map from IMSI to Queue of Timestamps for individual matches against the constraint.
	@Transient
	private Map<String, PriorityQueue<CallDataRecord>> _imsiToMatchedCDRsMap = new HashMap<String, PriorityQueue<CallDataRecord>>();
	
	@Transient
	private Date _mostRecentTimestamp;
	
	
	/**
	 * Check whether the given date is out of the window.
	 * @param date The date to check.
	 * @param currentDate The current date.
	 * @return If the date is out of the current window.
	 */
	private boolean dateIsOutOfWindow(Date date, Date currentDate) {
		return (currentDate.getTime() - date.getTime()) > windowSize * 1000;
	}
	
	/**
	 * Offer a CallDataRecord to this rule, checking if it matches the constraint.
	 * 
	 * If a match occurs, the CDR will be added to the list of matches within the current window
	 * for that IMSI. The method will also adjust the current window forward if the cdr release time
	 * is newer than the previous most recent and discard any CDR matches that are now expired. 
	 * Note that the cdr may be immediately discarded if it has a release time prior to the
	 * start of the current window.
	 * 
	 * @param cdr The CallDataRecord to check.
	 * @return true if the CDR was matched by the constraint.
	 */
	public boolean offer(CallDataRecord cdr) {
		// Ensure the most recent timestamp is not null.
		_mostRecentTimestamp = _mostRecentTimestamp == null ? cdr.releaseTime() : _mostRecentTimestamp;
		// Update the most recent timestamp if the CDR we just received is newer.
		_mostRecentTimestamp = cdr.releaseTime().compareTo(_mostRecentTimestamp) > 0 ? cdr.releaseTime() : _mostRecentTimestamp;
		
		createQueueForIMSIIfNeeded(cdr.imsi());
		
		// Check the CDR against the constraint.
		boolean matchedConstraint = this.constraint.matches(cdr);
		if (matchedConstraint) {
			_imsiToMatchedCDRsMap.get(cdr.imsi()).add(cdr);
		}
		
		removeExpiredMatchesForIMSI(cdr.imsi());
		
		return matchedConstraint;
	}
	
	/**
	 * Ensures that the queue of matches for a given imsi has been properly created.
	 * 
	 * The queues are created only on an as-needed basis.
	 * 
	 * @param imsi The imsi to check.
	 */
	private void createQueueForIMSIIfNeeded(String imsi) {
		// Create the queue for the imsi if this is the first time we've seen it.
		if (!_imsiToMatchedCDRsMap.containsKey(imsi)) {
			_imsiToMatchedCDRsMap.put(imsi, new PriorityQueue<CallDataRecord>(10, new Comparator<CallDataRecord>() {
				@Override
				public int compare(CallDataRecord o1, CallDataRecord o2) {
					return o1.releaseTime().compareTo(o2.releaseTime());
				}
			}));
		}
	}
	
	/**
	 * Removes matches that are now expired as a result of the current window.
	 * @param imsi The imsi to remove matches for.
	 */
	private void removeExpiredMatchesForIMSI(String imsi) {
		createQueueForIMSIIfNeeded(imsi);
		PriorityQueue<CallDataRecord> matchedCDRs = _imsiToMatchedCDRsMap.get(imsi);
		// Remove any previous matches that are now out of date for the imsi we're interested in.
		// Could potentially remove the CDR we just matched if it was from more than one window ago.
		while (matchedCDRs.peek() != null && dateIsOutOfWindow(matchedCDRs.peek().releaseTime(), _mostRecentTimestamp)) {
			matchedCDRs.poll();
		}
	}
	
	/**
	 * Get a list of the CDRs that have been matched in the current window.
	 * @param imsi The imsi to check for matches.
	 * @return A list of CallDataRecords.
	 */
	@Transient
	public List<CallDataRecord> matchedCallDataRecords(String imsi) {
		createQueueForIMSIIfNeeded(imsi);
		return new ArrayList<CallDataRecord>(_imsiToMatchedCDRsMap.get(imsi));
	}
	
	/**
	 * Check whether the current window has enough constraint matches in it to consider the
	 * whole rule to be matched for a given IMSI.
	 * @param imsi The subscriber id to check the match for.
	 * @return Whether the rule is matched.
	 */
	public boolean isMatched(String imsi) {
		createQueueForIMSIIfNeeded(imsi);
		return _imsiToMatchedCDRsMap.get(imsi).size() >= numberOfConstraintMatches;
	}
	
	/**
	 * Get a JSON representation of this Rule.
	 * @return A JSON representation of this rule.
	 */
	public ObjectNode toJson() {
		ObjectNode object = JsonNodeFactory.instance.objectNode();
		object.put("id", id);
		object.put("name", name);
		object.put("active", active);
		object.put("windowSize", windowSize);
		object.put("numberOfConstraintMatches", numberOfConstraintMatches);
		object.put("constraint", constraint.toJson());
		return object;
	}
	
	/**
	 * Get a rule from some JSON.
	 * @param jsonObject The JSON to parse.
	 * @return The Rule.
	 */
	public static Rule fromJson(JsonNode jsonObject) {
		Rule r = new Rule();
		r.id = jsonObject.get("id") == null ? null : jsonObject.get("id").asLong();
		r.name = jsonObject.get("name") == null ? null : jsonObject.get("name").asText();
		r.active = jsonObject.get("active") == null ? null : jsonObject.get("active").asBoolean();
		r.windowSize = jsonObject.get("windowSize") == null ? null : jsonObject.get("windowSize").asInt();
		r.numberOfConstraintMatches = jsonObject.get("numberOfConstraintMatches") == null ? null : jsonObject.get("numberOfConstraintMatches").asInt();
		r.constraint = jsonObject.get("constraint") == null ? null : ConstraintFactory.fromJson(jsonObject.get("constraint")).get();
		if (!r.validate()) {
			return null;
		}
		return r;
	}
	
	public boolean validate() {
		// Id may be null in the case where the rule is not yet saved, but all other variables must
		// be set for the rule to be valid.
		return name != null && active != null && windowSize != null 
				&& numberOfConstraintMatches != null && constraint != null;
	}
	
	@Override
	public String toString() {
		return toJson().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result
				+ ((constraint == null) ? 0 : constraint.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((numberOfConstraintMatches == null) ? 0
						: numberOfConstraintMatches.hashCode());
		result = prime * result
				+ ((windowSize == null) ? 0 : windowSize.hashCode());
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
		Rule other = (Rule) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (constraint == null) {
			if (other.constraint != null)
				return false;
		} else if (!constraint.equals(other.constraint))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfConstraintMatches == null) {
			if (other.numberOfConstraintMatches != null)
				return false;
		} else if (!numberOfConstraintMatches
				.equals(other.numberOfConstraintMatches))
			return false;
		if (windowSize == null) {
			if (other.windowSize != null)
				return false;
		} else if (!windowSize.equals(other.windowSize))
			return false;
		return true;
	}
}
