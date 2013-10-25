package com.aidanns.streams.project;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.aidanns.streams.project.models.CallDataRecord;
import com.aidanns.streams.project.models.CallDataRecord.CauseForTermination;
import com.aidanns.streams.project.models.StatisticsWindow;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class StatisticsCalculationBolt extends BaseRichBolt {

	private OutputCollector _collector;
	
	private Long _numberOfDroppedCalls;
	private Long _numberOfCalls;
	private Double _cumulativeCallLength;
	private Date _lastTimeStamp;
	
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		_numberOfCalls = 0L;
		_numberOfDroppedCalls = 0L;
		_cumulativeCallLength = 0.0;
	}

	@Override
	public void execute(Tuple input) {
		CallDataRecord cdr = (CallDataRecord) input.getValueByField("CallDataRecord");
		
		if (_lastTimeStamp == null) {
			_lastTimeStamp = cdr.releaseTime();
		}

		// Need Calendar versions of the dates so that we can modify them.
		Calendar lastTimeStamp = Calendar.getInstance();
		lastTimeStamp.setTime(_lastTimeStamp);
		
		Calendar currentTimeStamp = Calendar.getInstance();
		currentTimeStamp.setTime(cdr.releaseTime());
		
		if (currentTimeStamp.compareTo(lastTimeStamp) < 0) {
			// CDR is part of a previous window, throw it away.
		} else {
			if (currentTimeStamp.get(Calendar.MINUTE) != lastTimeStamp.get(Calendar.MINUTE)) {
				// We've moved past a minute boundary - send the stats for the previous minute.
				StatisticsWindow stats = new StatisticsWindow();
				stats.averageCallDuration = new Double(_cumulativeCallLength / _numberOfCalls).floatValue();
				stats.callVolume = _numberOfCalls;
				stats.droppedCallVolume = _numberOfDroppedCalls;
				stats.windowSize = 60;
				
				// Move the time forward 60 seconds, then truncate to the previous minute to get
				// the end time for that minute. Properly deals with moving between hours, days, etc.
				lastTimeStamp.setTimeInMillis(lastTimeStamp.getTimeInMillis() + 6000);
				lastTimeStamp.set(Calendar.SECOND, 0);
				lastTimeStamp.set(Calendar.MILLISECOND, 0);
				
				stats.timestamp = lastTimeStamp.getTime();
				_collector.emit("StatisticsWindowStream", new Values(stats));
				
				_numberOfDroppedCalls = 0L;
				_numberOfCalls = 0L;
				_cumulativeCallLength = 0.0;
			}
			
			// Increment the stored statistics.
			if (cdr.causeForTermination() == CauseForTermination.AbnormallyDropped) {
				_numberOfDroppedCalls++;
			}
			_numberOfCalls++;
			_cumulativeCallLength += cdr.callDuration().doubleValue();
			_lastTimeStamp = cdr.releaseTime();
			
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("StatisticsWindowStream", new Fields("Statistics"));

	}

}
