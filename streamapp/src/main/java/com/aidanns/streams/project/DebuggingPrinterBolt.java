package com.aidanns.streams.project;

import java.util.Map;

import org.apache.log4j.Logger;

import com.aidanns.streams.project.models.StatisticsWindow;

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
		
		Logger logger = Logger.getLogger(DebuggingPrinterBolt.class);
		
		switch (input.getSourceStreamId()) {
		case "UpdatedRulesStream":
			logger.debug("Updating a Rule: " + input.getValue(0).toString());
			break;
		case "RemovedRuleIdsStream":
			logger.debug("Removing a Rule: " + input.getValue(0).toString());
			break;
		case "CallDataRecordStream":
//			logger.debug("Processing a CDR: " + input.getValue(0).toString());
			break;
		case "RuleMatchStream":
//			logger.debug("Match: " + ((RuleMatch)input.getValue(0)).toJson());
			break;
		case "StatisticsWindowStream":
			logger.debug("Stats: " + ((StatisticsWindow) input.getValueByField("Statistics")).toJson());
			break;
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {}
}
