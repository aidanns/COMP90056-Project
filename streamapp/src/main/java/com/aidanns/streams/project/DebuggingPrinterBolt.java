package com.aidanns.streams.project;

import java.util.Map;

import org.apache.log4j.Logger;

import com.aidanns.streams.project.models.RuleMatch;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * Simple Bolt that Logs input.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@SuppressWarnings("serial")
public class DebuggingPrinterBolt extends BaseRichBolt {

	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
			OutputCollector collector) {}

	public void execute(Tuple input) {
		switch (input.getSourceStreamId()) {
		case "UpdatedRulesStream":
			Logger.getLogger(DebuggingPrinterBolt.class).debug("Updating a Rule: " + input.getValue(0).toString());
			break;
		case "RemovedRuleIdsStream":
			Logger.getLogger(DebuggingPrinterBolt.class).debug("Removing a Rule: " + input.getValue(0).toString());
			break;
		case "CallDataRecordStream":
//			Logger.getLogger(DebuggingPrinterBolt.class).debug("Processing a CDR: " + input.getValue(0).toString());
			break;
		case "RuleMatchStream":
//			Logger.getLogger(DebuggingPrinterBolt.class).debug("Match: " + ((RuleMatch)input.getValue(0)).toJson());
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {}
}
