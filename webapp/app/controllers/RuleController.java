package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.aidanns.streams.project.models.Rule;

public class RuleController extends Controller {

	 /**
	  * Show all the Rules as JSON.
	  */
	 @Transactional
	 public static Result index() {
	 	final List<Rule> rules = JPA.em().createQuery("SELECT r from Rule r", Rule.class).getResultList();
	 	return ok(ruleListToJson(rules));
	 }

	 /**
	  * Create a new Rule.
	  */
	 @Transactional
	 public static Result create() {
	 	JsonNode json = request().body().asJson();
	 	if (json == null) {
	 		return badRequest("Expected JSON to be posted.");
	 	}
	 	Rule rule = Rule.fromJson(json);
	 	if (rule == null) {
	 		return badRequest("JSON was not a valid rule.");
	 	}
	 	if (rule.id != null) {
	 		return badRequest("You may not specify an Id for a new rule.");
	 	}
	 	JPA.em().persist(rule);
	 	return ok(rule.toJson());
	 }

	 /**
	  * Show an individual Rule as JSON.
	  * @param id The id of the Rule to show.
	  */
	 @Transactional
	 public static Result read(Long id) {
	 	final Rule rule = JPA.em().find(Rule.class, id);
	 	if (rule == null) {
	 		return notFound();
	 	} else {
	 		return ok(rule.toJson());
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
	 	Rule postedRule = Rule.fromJson(json);
	 	if (postedRule == null) {
	 		return badRequest("JSON was not a valid rule.");
	 	}
		
	 	final Rule rule = JPA.em().find(Rule.class, id);
	 	if (rule == null) {
	 		return badRequest("No rule exists with id " + id);
	 	}
		
	 	rule.name = postedRule.name;
	 	rule.active = postedRule.active;
	 	rule.constraint = postedRule.constraint;
	 	rule.windowSize = postedRule.windowSize;
	 	rule.numberOfConstraintMatches = postedRule.numberOfConstraintMatches;
		
	 	JPA.em().persist(rule);
	 	return ok(rule.toJson());
	 }

	 /**
	  * Delete a rule by id.
	  * @param id The id of the Rule to update.
	  */
	 @Transactional
	 public static Result delete(Long id) {
	 	final Rule rule = JPA.em().find(Rule.class, id);
	 	if (rule == null) {
	 		return notFound();
	 	} else {
	 		JPA.em().remove(rule);
	 		return ok();
	 	}
	 }
	
	 /**
	  * Convert a list of Rules to JSON.
	  * @param rules The Rules to convert.
	  * @return A JSON array of the rules.
	  */
	 private static ArrayNode ruleListToJson(List<Rule> rules) {
	 	ArrayNode arrayNode = Json.newObject().arrayNode();
	 	for(Rule r : rules) {
	 		arrayNode.add(r.toJson());
	 	}
	 	return arrayNode;
	 }
}
