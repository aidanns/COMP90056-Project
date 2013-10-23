define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");

  return Backbone.View.extend({
	  
    template: _.template(
    		'<h2> Home </h2>' +
    		'<button class="showRules">Show Rules</button>' +
    		'<button class="showMatches">Show Matches</button>' +
    		'<button class="showStats">Show Stats</button>'),
    
    events: {
    	"click .showRules": "showRules",
    	"click .showMatches": "showMatches",
    	"click .showStats": "showStats"
    },		
    		
    render: function() {
    	this.$el.html(this.template());
    	return this;
    },
    
    showRules: function() {
    	Backbone.history.navigate("/rules", {trigger: true});
    },
    
    showMatches: function() {
    	Backbone.history.navigate("/matches", {trigger: true});
    },
    
    showStats: function() {
    	Backbone.history.navigate("/statistics", {trigger: true});
    }
  });
});