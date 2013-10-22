package controllers;

import java.util.List;

import com.aidanns.streams.project.models.StatisticsWindow;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class StatisticsController extends Controller {

	/**
	 * Show all the statistics as JSON.
	 */
	@Transactional
	public static Result index() {
		final List<StatisticsWindow> stats = JPA.em().createQuery("SELECT s from StatisticsWindow s", StatisticsWindow.class).getResultList();
		return ok(statisticsListToJson(stats));
	}
	
	/**
	 * Create a new StatisticsWindow.
	 */
	@Transactional
	public static Result create() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest("Expected JSON to be posted.");
		}
		StatisticsWindow stat = StatisticsWindow.fromJson(json);
		if (stat == null) {
			return badRequest("JSON was not a valid StatisticsWindow.");
		}
		if (stat.id != null) {
			return badRequest("You may not specify an Id for a new StatisticsWindow.");
		}
		JPA.em().persist(stat);
		return ok(stat.toJson());
	}
	
	/**
	 * Show an individual StatisticsWindow as JSON.
	 * @param di The id of the StatisticsWindow to show.
	 */
	@Transactional
	public static Result read(Long id) {
		final StatisticsWindow stat = JPA.em().find(StatisticsWindow.class, id);
		if (stat == null) {
			return notFound();
		} else {
			return ok(stat.toJson());
		}
	}

	 /**
	  * Update an existing StatisticsWindow with JSON.
	  * @param id The id of the StatisticsWindow to update.
	  */
	 @Transactional
	 public static Result update(Long id) {
	 	JsonNode json = request().body().asJson();
	 	if (json == null) {
	 		return badRequest("Expected a JSON representation of a StatisticsWindow.");
	 	}
	 	StatisticsWindow postedStat = StatisticsWindow.fromJson(json);
	 	if (postedStat == null) {
	 		return badRequest("JSON was not a valid StatisticsWindow.");
	 	}
		
	 	final StatisticsWindow stat = JPA.em().find(StatisticsWindow.class, id);
	 	if (stat == null) {
	 		return badRequest("No StatisticsWindow exists with id " + id);
	 	}
		
	 	stat.timestamp = postedStat.timestamp;
	 	stat.averageCallDuration = postedStat.averageCallDuration;
	 	stat.callVolume = postedStat.callVolume;
	 	stat.droppedCallVolume = postedStat.droppedCallVolume;
	 	stat.windowSize = postedStat.windowSize;
		
	 	JPA.em().persist(stat);
	 	return ok(stat.toJson());
	 }

	 /**
	  * Delete a rule by id.
	  * @param id The id of the StatisticsWindow to update.
	  */
	 @Transactional
	 public static Result delete(Long id) {
	 	final StatisticsWindow match = JPA.em().find(StatisticsWindow.class, id);
	 	if (match == null) {
	 		return notFound();
	 	} else {
	 		JPA.em().remove(match);
	 		return ok();
	 	}
	 }
	
	 /**
	  * Convert a list of StatisticsWindow to JSON.
	  * @param statistics The StatisticsWindow to convert.
	  * @return A JSON array of the StatisticsWindows.
	  */
	 private static ArrayNode statisticsListToJson(List<StatisticsWindow> statistics) {
	 	ArrayNode arrayNode = Json.newObject().arrayNode();
	 	for(StatisticsWindow s : statistics) {
	 		arrayNode.add(s.toJson());
	 	}
	 	return arrayNode;
	 }
}
