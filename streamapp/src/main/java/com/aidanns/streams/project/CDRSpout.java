package com.aidanns.streams.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aidanns.streams.project.models.CallDataRecord;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Spout for call data records that reads them from a file.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@SuppressWarnings("serial")
public class CDRSpout extends BaseRichSpout {
	
	/** Logger, lazily loaded by {@link #getLogger()}. */
	private Logger _logger;
	
	/**
	 * Lazily load a logger.
	 * @return Logger to be used in this class.
	 */
	private Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(CDRSpout.class);
		}
		return _logger;
	}

	private SpoutOutputCollector _collector;
	
	private String _fileName;
	
	private CSVReader<CallDataRecord> _csvCallDataRecordReader;
	
	// Parse the dates as per the format in the example data.
	private SimpleDateFormat _dateParser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	/**
	 * Create a new CDRSpout.
	 * @param fileName The csv file to read call data records from.
	 */
	public CDRSpout(String fileName) {
		_fileName = fileName;
	}
	
	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		
		try {
			BufferedReader cdrFileReader = new BufferedReader(new InputStreamReader(
					CDRSpout.class.getClassLoader().getResourceAsStream(_fileName)));
			_csvCallDataRecordReader = new CSVReaderBuilder<CallDataRecord>(cdrFileReader).entryParser(new CSVEntryParser<CallDataRecord>() {
				@Override
				public CallDataRecord parseEntry(String... data) {
					// If any of the dates of the call duration can't be parsed, just record it as null.
					Date answerTime = null;
					Date seizureTime = null;
					Date releaseTime = null;
					
					try { answerTime = _dateParser.parse(data[9]); } catch (Throwable t) {}
					try { seizureTime = _dateParser.parse(data[10]); } catch (Throwable t) {}
					try { releaseTime = _dateParser.parse(data[11]); } catch (Throwable t) {}
					
					Float callDuration = null;
					
					try { callDuration = Float.parseFloat(data[8]); } catch (Throwable t) {}
					
					return new CallDataRecord(
								CallDataRecord.CDRType.fromAbbreviation(data[0]), 
								data[1], data[2], data[3], data[4], data[5], data[6], data[7],
								callDuration, answerTime, seizureTime, releaseTime,
								CallDataRecord.CauseForTermination.fromAbbreviation(data[12]), data[13], data[14]);
				}
			}).strategy(CSVStrategy.UK_DEFAULT).build();
			
		} catch (Throwable e) {
			getLogger().error(getIOExcpetionString());
			System.exit(1);
		}

	}
	
	private String getIOExcpetionString() {
		return "Unable to open file " + _fileName
				+ " in the 'input' directory to read call data records.";
	}

	@Override
	public void nextTuple() {
		try {
			CallDataRecord cdr = _csvCallDataRecordReader.readNext();
			if (cdr != null) {
				_collector.emit("CallDataRecordStream", new Values(cdr));
			} else {
				System.exit(0); // No more values to read, so end the program.
			}
		} catch (IOException e) {
			getLogger().error(getIOExcpetionString());
			System.exit(1);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("CallDataRecordStream", new Fields("CallDataRecord"));

	}

}
