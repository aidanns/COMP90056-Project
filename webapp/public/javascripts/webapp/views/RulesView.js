define(function(require) {
  var Backbone = require("backbone");
  var RuleView = require("webapp/views/RuleView");
  var _ = require("underscore");

  return Backbone.View.extend({
	  
    template: _.template(
    		'<h2> Rules: </h2>' +
    		'<table>' +
    		'  <tr>' +
    		'    <th>Id</th>' +
    		'    <th>Name</th>' +
    		'    <th>Window Size (Seconds)</th>' +
    		'    <th># Constraint Matches Needed</th>' +
    		'    <th>Rule Active?</th>' +
    		'    <th></th>' +
    		'    <th></th>' +
    		'  </tr>' +
    		'</table>' +
    		'<button class="addRule">Add Rule</button>'),
    
    events: {
    	"click .addRule": "addRule"
    },
    		
    initialize: function(options) {
    	this.listenTo(this.collection, 'add', this.addOne);
    	this.listenTo(this.collection, "reset", this.render);
    },
    
    render: function() {
    	this.$el.html(this.template());
    	this.addAll();
    	return this;
    },
    
    addAll: function() {
    	this.collection.each(this.addOne, this);
    },
    
    addOne: function(model) {
    	var view = new RuleView({model: model});
    	view.render();
    	$('table', this.$el).append(view.el);
    	model.on('remove', view.remove, view);
    },
    
    addRule: function() {
    	Backbone.history.navigate("/rules/add", {trigger: true});
    }
  });
});