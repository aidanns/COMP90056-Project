package com.aidanns.streams.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.aidanns.streams.project.models.Rule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * A spout that reads rules from the REST API.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@SuppressWarnings("serial")
public class RuleSpout extends BaseRichSpout {
	
	// Rules that have been added or updated and should be replaced in the individual bolts.
	private BlockingQueue<Rule> _updatedRulesQueue = new LinkedBlockingQueue<Rule>();
	
	// Rules that have been deleted and should be removed from the individual bolts.
	private BlockingQueue<Long> _removedRuleIds = new LinkedBlockingQueue<Long>();
	
	// The output collector that we send output to.
	private SpoutOutputCollector _collector;
	
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		final HttpGet httpGet = new HttpGet("http://localhost:9000/api/rule");
		// Response handler to convert from the JSON we receive and a list of POJO Rules.
		final ResponseHandler<List<Rule>> responseHandler = new ResponseHandler<List<Rule>>() {
			public List<Rule> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status == 200) {
					HttpEntity entity = response.getEntity();
					if (entity == null) {
						// If there's no body to the response give up.
						throw new ClientProtocolException("No response body found.");
					}
					ObjectMapper mapper = new ObjectMapper();
					JsonNode jsonObject = mapper.readTree(EntityUtils.toString(entity));
					if (!jsonObject.isArray()) {
						// If the body of the response isn't a JSON array, give up.
						throw new ClientProtocolException("Expected an array of rules to be returned.");
					}
					
					// Parse a Rule from each object in the JSON array.
					List<Rule> parsedRules = new ArrayList<Rule>(jsonObject.size());
					for (int i = 0; i < jsonObject.size(); i++) {
						parsedRules.add(Rule.fromJson(jsonObject.get(i)));
					}
					return parsedRules;
				} else {
					// If it's not a 200 OK, give up.
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};
		
		// Pull down the latest rules every few seconds.
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			
			// Store the list of rules from last run so that we can compare against them.
			Map<Long, Rule> _rulesById = new HashMap<Long, Rule>();
			
			@Override
			public void run() {
				try {
					List<Rule> response = httpClient.execute(httpGet, responseHandler);
					
					Map<Long, Rule> newRulesById = new HashMap<Long, Rule>();
					
					for (Rule r : response) {
						if (!(_rulesById.containsKey(r.id) && _rulesById.get(r.id).equals(r))) {
							// Either doesn't yet exist or has been updated, so queue it.
							_updatedRulesQueue.add(r);
						} 
						newRulesById.put(r.id, r);
					}
					
					for (Long l : _rulesById.keySet()) {
						if (!newRulesById.containsKey(l)) {
							// It's been deleted since last run.
							_removedRuleIds.add(l);
						}
					}
					
					_rulesById = newRulesById;
				} catch (ClientProtocolException e) {
					Logger.getLogger(RuleSpout.class).error("Error while handling resonse from server: " + e.getMessage());
				} catch (IOException e) {
					// Should never happen - we don't throw this from the ResponseHandler.
					Logger.getLogger(RuleSpout.class).error("Threw an error in RuleSpout that we didn't think we could.");
				}
			}
		}, 0, 5000);
	}

	public void nextTuple() {
		Rule updatedRule = _updatedRulesQueue.poll();
		if (updatedRule != null) {
			_collector.emit("UpdatedRulesStream", new Values(updatedRule));
		}
		Long removedRuleId = _removedRuleIds.poll();
		if (removedRuleId != null) {
			_collector.emit("RemovedRuleIdsStream", new Values(removedRuleId));
		}
		if (updatedRule == null && removedRuleId == null) {
			Utils.sleep(5000);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("UpdatedRulesStream", new Fields("rule"));
		declarer.declareStream("RemovedRuleIdsStream", new Fields("id"));
	}
}
