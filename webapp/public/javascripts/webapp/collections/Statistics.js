define(function(require) {
	var Backbone = require("backbone");
	var Statistic = require("webapp/models/Statistic");
	
	return Backbone.Collection.extend({
		model: Statistic,
		url: "/api/statistics"
	})
});