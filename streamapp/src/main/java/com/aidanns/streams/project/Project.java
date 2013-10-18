package com.aidanns.streams.project;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

/**
 * Logic that starts the program.
 * @author Aidan Nagorcka-Smith (aidann@student.unimelb.edu.au)
 */
public class Project {
	
	/**
	 * Start the application.
	 * @param args The arguments to the app.
	 */
	public static void main(String args[]) {
		TopologyBuilder builder = new TopologyBuilder();
		
		// Setup the spouts.
		builder.setSpout("rule-spout", new RuleSpout(), 1);
		
		// Setup the bolts
		builder.setBolt("rule-change-printer", new RuleChangePrinterBolt(), 1)
				.shuffleGrouping("rule-spout", "UpdatedRulesStream")
				.shuffleGrouping("rule-spout", "RemovedRuleIdsStream");
		
		// Start the job.
		Config conf = new Config();
		conf.setDebug(true);
		conf.setMaxTaskParallelism(3);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("project", conf, builder.createTopology());
		
		try {
			Thread.sleep(300 * 1000); // Set the runtime for the app in ms.
		} catch (InterruptedException e) {
			Logger.getLogger(Project.class).error("Interrupted while"
					+ " waiting for local cluster to complete processing.");
			e.printStackTrace();
		}
		
		cluster.shutdown();
		System.exit(0);
	}
}
