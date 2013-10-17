package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Rule extends Controller {

	/**
	 * Show all the Rules as JSON.
	 */
	@Transactional
	public static Result index() {
		final List<models.Rule> rules = JPA.em().createQuery("SELECT r from Rule r", models.Rule.class).getResultList();
		return ok(ruleListToJson(rules));
	}

	/**
	 * Create a new Rule.
	 */
	@Transactional
	public static Result create() {
		return ok();
	}

	/**
	 * Show an individual Rule as JSON.
	 * @param id The id of the Rule to show.
	 */
	@Transactional
	public static Result read(Long id) {
		final models.Rule rule = JPA.em().find(models.Rule.class, id);
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
		return ok();
	}

	/**
	 * Delete a rule by id.
	 * @param id The id of the Rule to update.
	 */
	@Transactional
	public static Result delete(Long id) {
		final models.Rule rule = JPA.em().find(models.Rule.class, id);
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
	private static ArrayNode ruleListToJson(List<models.Rule> rules) {
		ArrayNode arrayNode = Json.newObject().arrayNode();
		for(models.Rule r : rules) {
			arrayNode.add(r.toJson());
		}
		return arrayNode;
	}
}