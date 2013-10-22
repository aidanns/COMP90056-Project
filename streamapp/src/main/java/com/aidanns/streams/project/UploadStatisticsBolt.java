package com.aidanns.streams.project;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.aidanns.streams.project.models.StatisticsWindow;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * Bolt that uploads statistics to the REST API.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
@SuppressWarnings("serial")
public class UploadStatisticsBolt extends BaseRichBolt {

	private CloseableHttpClient httpClient;

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
			OutputCollector collector) {
		httpClient = HttpClients.createDefault();
	}

	@Override
	public void execute(Tuple input) {
		final HttpPost httpPost = new HttpPost("http://localhost:9000/api/statistics");
		StringEntity body = new StringEntity(((StatisticsWindow)input.getValueByField("Statistics")).toJson().toString(), ContentType.create("application/json", "UTF-8"));
		httpPost.setEntity(body);
		try {
			Logger.getLogger(UploadMatchBolt.class).debug(httpClient.execute(httpPost, new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					return response.toString();
				}
			}));
		} catch (ClientProtocolException e) {
			Logger.getLogger(UploadMatchBolt.class).error("Error while uploading statistics to the REST API.");
		} catch (IOException e) {
			// Should never happen - we don't throw this in the response handler.
			Logger.getLogger(RuleSpout.class).error("Threw an error in UploadStatisticsBolt that we didn't think we could.");
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {}
}
