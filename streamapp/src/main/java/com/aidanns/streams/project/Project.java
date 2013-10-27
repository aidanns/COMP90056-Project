package com.aidanns.streams.project;

import java.util.Properties;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Logic that starts the program.
 * @author Aidan Nagorcka-Smith (aidann@student.unimelb.edu.au)
 */
public class Project {
	
	private static String PROJECT_PROPERTIES_FILE_NAME = "project.properties";
	
	/**
	 * Start the application.
	 * @param args The arguments to the app.
	 */
	public static void main(String args[]) {
		TopologyBuilder builder = new TopologyBuilder();
		
		Properties projectProperties = new Properties();
		try {
			projectProperties.load(Project.class.getClassLoader()
					.getResourceAsStream(PROJECT_PROPERTIES_FILE_NAME));
		} catch (Throwable e) {
			Logger.getLogger(Project.class).error("Failed to open file specifying the rate for tuple generation from the Call Data Record spouts.");
			System.exit(1);
		}
		
		Integer cdrTupleRate = 0;
		try {
			 cdrTupleRate = Integer.parseInt(projectProperties.getProperty("spout.cdr.rate.tuples_per_second"));
		} catch (NumberFormatException e) {
			Logger.getLogger(Project.class).warn("Value for property key 'spout.cdr.rate.tuples_per_second' could not be parsed as a valid Integer. Call Data Record generation will not be throttled.");
		}
		
		// Setup the spouts.
		builder.setSpout("rule-spout", new RuleSpout(), 1);
		builder.setSpout("cdr-spout-1", new CDRSpout(projectProperties.getProperty("spout.cdr.filename"), cdrTupleRate));
		
		// Setup the bolts
		builder.setBolt("statistics-gatherer", new StatisticsCalculationBolt(), 1)
				.shuffleGrouping("cdr-spout-1", "CallDataRecordStream");
		builder.setBolt("rule-matcher", new RuleMatchingBolt(), 5)
				.shuffleGrouping("rule-spout", "UpdatedRulesStream")
				.shuffleGrouping("rule-spout", "RemovedRuleIdsStream")
				.fieldsGrouping("cdr-spout-1", "CallDataRecordStream", new Fields("IMSI"));
		builder.setBolt("rule-match-uploader", new UploadMatchBolt(), 1)
				.shuffleGrouping("rule-matcher", "RuleMatchStream");
		builder.setBolt("statistics-uploader", new UploadStatisticsBolt(), 1)
				.shuffleGrouping("statistics-gatherer", "StatisticsWindowStream");
		
//		builder.setBolt("debugging-printer", new DebuggingPrinterBolt(), 1)
//				.shuffleGrouping("rule-spout", "UpdatedRulesStream")
//				.shuffleGrouping("rule-spout", "RemovedRuleIdsStream")
//				.shuffleGrouping("cdr-spout-1", "CallDataRecordStream")
//				.shuffleGrouping("rule-matcher", "RuleMatchStream")
//				.shuffleGrouping("statistics-gatherer", "StatisticsWindowStream");
		
		// Start the job.
		Config conf = new Config();
		conf.setDebug(true);
		conf.setMaxTaskParallelism(3);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("project", conf, builder.createTopology());
		
		try {
			Thread.sleep(600 * 1000); // Set the runtime for the app in ms.
		} catch (InterruptedException e) {
			Logger.getLogger(Project.class).error("Interrupted while"
					+ " waiting for local cluster to complete processing.");
			e.printStackTrace();
		}
		
		cluster.shutdown();
		System.exit(0);
	}
}
