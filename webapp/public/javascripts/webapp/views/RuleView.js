define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");
  var Rule = require("webapp/models/Rule");

  return Backbone.View.extend({
	
	tagName: "tr",
	
	className: "rule",
	
	events: {
		"click .remove": "destroy",
		"click .active": "toggleActive",
		"click .edit": "edit"
	},
	
    template: _.template(
    		'<td> <%= id %> </td>' +
    		'<td> <%= name %> </td>' +
    		'<td> <%= windowSize %> </td>' +
    		'<td> <%= numberOfConstraintMatches %> </td>' +
    		'<td class="active"> <%= active %> </td>' +
    		'<td><button class="remove">Remove</button></td>' +
    		'<td><button class="edit">Edit</button></td>'),
    
    initialize: function(options) {
    	this.listenTo(this.model, "change", this.render);
    	this.listenTo(this.model, "destroy", this.remove);
    },
    
    render: function() {
    	this.$el.html(this.template(this.model.toJSON()));
    	return this;
    },
    
    destroy: function() {
    	this.model.destroy();
    },
    
    toggleActive: function() {
    	this.model.set("active", !this.model.get("active"));
    	this.model.save();
    },
    
    edit: function() {
    	Backbone.history.navigate("/edit/" + this.model.id, {trigger: true});
    }
  });
});