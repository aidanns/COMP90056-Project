define(function(require) {
	var Backbone = require("backbone");
	var Match = require("webapp/models/Match");
	
	return Backbone.Collection.extend({
		model: Match,
		url: "/api/match"
	})
	
});