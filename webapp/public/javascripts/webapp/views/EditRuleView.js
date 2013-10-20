define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");
  var Rule = require("webapp/models/Rule");

  return Backbone.View.extend({
	  
    template: _.template(
    		'<h2> Edit Rule: </h2>' +
    		'<form>' +
    		'  <label for="inputName">Name</label>' +
    		'  <input type="text" id="inputName" placeholder="Name" value="<%= name %>">' +
    		'  <label for="inputWindowSize">Window Size (Seconds)</label>' +
    		'  <input type="text" id="inputWindowSize" placeholder="Window Size" value="<%= windowSize %>">' +
    		'  <label for="inputNumberOfConstraintMatches"># of Constraint Matches</label>' +
    		'  <input type="text" id="inputNumberOfConstraintMatches" placeholder="# of Constraint Matches" value="<%= numberOfConstraintMatches %>">' +
    		'</form>' +
    		'<button class="save">Save</button>' +
    		'<button class="cancel">Cancel</button>'),
    
    events: {
    	"click .save": "save",
    	"click .cancel": "cancel"
    },
    		
    initialize: function(options) {
    	this.listenTo(this.model, 'change', this.render);
    },
    
    render: function() {
    	// In case the model hasn't yet loaded.
    	var toRender = {}
    	toRender.name = this.model.get("name") ? this.model.get("name") : "";
    	toRender.windowSize = this.model.get("windowSize") ? this.model.get("windowSize") : "";
    	toRender.numberOfConstraintMatches = this.model.get("numberOfConstraintMatches") ? this.model.get("numberOfConstraintMatches") : "";
    	this.$el.html(this.template(toRender));
    	return this;
    },
    
    save: function() {
    	console.log("Saving");
    	this.model.save({
    		name: $("#inputName", this.$el).val(),
    		windowSize: $("#inputWindowSize", this.$el).val(),
    		numberOfConstraintMatches: $("#inputNumberOfConstraintMatches", this.$el).val()
    	});
    	Backbone.history.navigate("/rules", {trigger: true});
    },
    
    cancel: function() {
    	console.log("Cancelling.");
    	Backbone.history.navigate("/rules", {trigger: true});
    }
  });
});
