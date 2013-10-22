package controllers;

import java.util.List;

import com.aidanns.streams.project.models.RuleMatch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class RuleMatchController extends Controller {

	/**
	 * Show all the matches as JSON.
	 */
	@Transactional
	public static Result index() {
		final List<RuleMatch> ruleMatches = JPA.em().createQuery("SELECT r from RuleMatch r", RuleMatch.class).getResultList();
		return ok(ruleMatchListToJson(ruleMatches));
	}
	
	/**
	 * Create a new RuleMatch.
	 */
	@Transactional
	public static Result create() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest("Expected JSON to be posted.");
		}
		RuleMatch match = RuleMatch.fromJson(json);
		if (match == null) {
			return badRequest("JSON was not a valid match.");
		}
		if (match.id != null) {
			return badRequest("You may not specify an Id for a new rule.");
		}
		JPA.em().persist(match);
		return ok(match.toJson());
	}
	
	/**
	 * Show an individual RuleMatch as JSON.
	 * @param di The id of the Match to show.
	 */
	@Transactional
	public static Result read(Long id) {
		final RuleMatch match = JPA.em().find(RuleMatch.class, id);
		if (match == null) {
			return notFound();
		} else {
			return ok(match.toJson());
		}
	}

	 /**
	  * Update an existing Rule with JSON.
	  * @param id The id of the Rule to update.
	  */
	 @Transactional
	 public static Result update(Long id) {
	 	JsonNode json = request().body().asJson();
	 	if (json == null) {
	 		return badRequest("Expected a JSON representation of a rule.");
	 	}
	 	RuleMatch postedMatch = RuleMatch.fromJson(json);
	 	if (postedMatch == null) {
	 		return badRequest("JSON was not a valid rule.");
	 	}
		
	 	final RuleMatch match = JPA.em().find(RuleMatch.class, id);
	 	if (match == null) {
	 		return badRequest("No rule exists with id " + id);
	 	}
		
	 	match.timestamp = postedMatch.timestamp;
	 	match.ruleId = postedMatch.ruleId;
	 	match.imsi = postedMatch.imsi;
		
	 	JPA.em().persist(match);
	 	return ok(match.toJson());
	 }

	 /**
	  * Delete a rule by id.
	  * @param id The id of the Rule to update.
	  */
	 @Transactional
	 public static Result delete(Long id) {
	 	final RuleMatch match = JPA.em().find(RuleMatch.class, id);
	 	if (match == null) {
	 		return notFound();
	 	} else {
	 		JPA.em().remove(match);
	 		return ok();
	 	}
	 }
	
	 /**
	  * Convert a list of Rules to JSON.
	  * @param ruleMatches The Rules to convert.
	  * @return A JSON array of the rules.
	  */
	 private static ArrayNode ruleMatchListToJson(List<RuleMatch> ruleMatches) {
	 	ArrayNode arrayNode = Json.newObject().arrayNode();
	 	for(RuleMatch r : ruleMatches) {
	 		arrayNode.add(r.toJson());
	 	}
	 	return arrayNode;
	 }
}
