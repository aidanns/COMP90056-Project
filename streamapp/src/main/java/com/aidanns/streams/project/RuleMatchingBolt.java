package com.aidanns.streams.project;

import java.util.HashMap;
import java.util.Map;

import com.aidanns.streams.project.models.CallDataRecord;
import com.aidanns.streams.project.models.Rule;
import com.aidanns.streams.project.models.RuleMatch;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class RuleMatchingBolt extends BaseRichBolt {

	private OutputCollector _collector;
	
	private Map<Long, Rule> _idRuleMap = new HashMap<Long, Rule>();
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		switch(input.getSourceStreamId()) {
		case "UpdatedRulesStream":
			Rule r = (Rule) input.getValueByField("rule");
			_idRuleMap.put(r.id, r);
			break;
		case "RemovedRulesIdsStream":
			Long id = (Long) input.getValueByField("id");
			_idRuleMap.remove(id);
			break;
		case "CallDataRecordStream":
			CallDataRecord cdr = (CallDataRecord) input.getValueByField("CallDataRecord");
			for (Rule rule : _idRuleMap.values()) {
				if (rule.active) {
					if (rule.offer(cdr) && rule.isMatched(cdr.imsi())) {
						RuleMatch match = new RuleMatch();
						match.imsi = cdr.imsi();
						match.timestamp = cdr.releaseTime();
						match.ruleId = rule.id;
						match.callDataRecords.addAll(rule.matchedCallDataRecords(cdr.imsi()));
						_collector.emit("RuleMatchStream", new Values(match));
					}
				}
			}
			break;
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("RuleMatchStream", new Fields("match"));
	}

}
