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
 * Representation of CDR statistics that are collected over a certain window.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@Entity
public class StatisticsWindow {
	
	@Id
	@GeneratedValue
	public Long id;
	
	/**
	 * In seconds.
	 */
	@Basic
	public Integer windowSize;
	
	/**
	 * Timestamp of the end of the window.
	 */
	@Basic
	public Date timestamp;
	
	/**
	 * In seconds.
	 */
	@Basic
	public Float averageCallDuration;
	
	@Basic 
	public Long callVolume;
	
	@Basic
	public Long droppedCallVolume;

	public ObjectNode toJson() {
		ObjectNode o = JsonNodeFactory.instance.objectNode();
		if (id != null) { o.put("id", id); }
		o.put("windowSize", windowSize);
		o.put("averageCallDuration", averageCallDuration);
		o.put("callVolume", callVolume);
		o.put("droppedCallVolume", droppedCallVolume);
		o.put("timestamp", _dateParser.format(timestamp));
		return o;
	}
	
	static private SimpleDateFormat _dateParser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	public static StatisticsWindow fromJson(JsonNode o) {
		if (!o.isObject()) {
			return null;
		}
		
		StatisticsWindow w = new StatisticsWindow();
		
		w.id = o.get("id") == null ? null : o.get("id").asLong();
		w.windowSize = o.get("windowSize") == null ? null : o.get("windowSize").asInt();
		try {
			w.timestamp = o.get("timestamp") == null ? null : _dateParser.parse(o.get("timestamp").asText());
		} catch (ParseException e) {
			w.timestamp = null;
		}
		w.averageCallDuration = o.get("averageCallDuration") == null ? null : new Double(o.get("averageCallDuration").asDouble()).floatValue();
		w.callVolume = o.get("callVolume") == null ? null : o.get("callVolume").asLong();
		w.droppedCallVolume = o.get("droppedCallVolume") == null ? null : o.get("droppedCallVolume").asLong();
		if (w.validate()) {
			return w;
		} else {
			return null;
		}
	}
	
	private boolean validate() {
		// Id isn't needed always.
		return windowSize != null && timestamp != null && averageCallDuration != null && callVolume != null && droppedCallVolume != null;
	}
	
}
