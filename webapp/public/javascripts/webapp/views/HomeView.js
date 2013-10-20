define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");

  return Backbone.View.extend({
	  
    template: _.template(
    		'<h2> Home </h2>' +
    		'<button class="showRules">Show Rules</button>'),
    
    events: {
    	"click .showRules": "showRules"
    },		
    		
    render: function() {
    	this.$el.html(this.template());
    	return this;
    },
    
    showRules: function() {
    	Backbone.history.navigate("/rules", {trigger: true});
    }
  });
});