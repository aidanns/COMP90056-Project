define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");
  var Rule = require("webapp/models/Rule");
  var ConstraintView = require("webapp/views/ConstraintView");

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
    		'<div class="constraint"></div>' +
    		'<button class="save">Save</button>' +
    		'<button class="cancel">Cancel</button>'),
    
    events: {
    	"click .save": "save",
    	"click .cancel": "cancel"
    },
    		
    initialize: function(options) {
    	this.saveDeferred = $.Deferred();
    	this.listenTo(this.model, 'change', this.render);
    	this.constraintView = new ConstraintView({constraint: this.model.get("constraint")});
    },
    
    getSaveDeferred: function() {
    	return this.saveDeferred.promise();
    },
    
    render: function() {
    	// In case the model hasn't yet loaded.
    	var toRender = {}
    	toRender.name = this.model.get("name") ? this.model.get("name") : "";
    	toRender.windowSize = _.isNumber(this.model.get("windowSize")) ? this.model.get("windowSize") : "";
    	toRender.numberOfConstraintMatches = _.isNumber(this.model.get("numberOfConstraintMatches")) ? this.model.get("numberOfConstraintMatches") : "";
    	this.$el.html(this.template(toRender));
    	$(".constraint", this.$el).html(this.constraintView.render().el);
    	return this;
    },
    
    save: function() {
    	this.model.save({
    		name: $("#inputName", this.$el).val(),
    		windowSize: $("#inputWindowSize", this.$el).val(),
    		numberOfConstraintMatches: $("#inputNumberOfConstraintMatches", this.$el).val(),
    		active: _.isBoolean(this.model.get('active')) ? this.model.get("active") : true,
    		constraint: this.constraintView.getConstraint()
    	}).done(_.bind(function() {
    		this.saveDeferred.resolve();
    	}, this));
		Backbone.history.navigate("/rules", {trigger: true});;
    },
    
    cancel: function() {
    	Backbone.history.navigate("/rules", {trigger: true});
    }
  });
});
