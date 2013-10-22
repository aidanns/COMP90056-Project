define(function(require) {
	var Backbone = require("backbone");
	var _ = require("underscore");
	var Match = require("webapp/models/Match");
	
	return Backbone.View.extend({
		
		tagName: "tr",
		
		className: "match",
		
		template: _.template(
	    		'    <td><%= id %></td>' +
	    		'    <td><%= timestamp %></td>' +
	    		'    <td><%= ruleId %></td>' +
	    		'    <td><%= imsi %></td>'),
	    		
	    events: {
	    	"click": "showDetailView"
	    },
	    		
	    initialize: function() {
	    	this.listenTo(this.model, "change", this.render);
	    	this.listenTo(this.model, "destroy", this.remove);
	    },
	    
	    render: function() {
	    	this.$el.html(this.template(this.model.toJSON()));
	    	return this;
	    },
	    
	    showDetailView: function() {
	    	Backbone.history.navigate("matches/" + this.model.id, {trigger: true});
	    }
	    
	});
});