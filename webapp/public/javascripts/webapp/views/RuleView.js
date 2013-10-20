define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");
  var Rule = require("webapp/models/Rule");

  return Backbone.View.extend({
	
	tagName: "tr",
	
	className: "rule",
	
	events: {
		"click .remove": "destroy"
	},
	
    template: _.template(
    		'<td class="id"> <%= id %> </td>' +
    		'<td class="name"> <%= name %> </td>' +
    		'<td class="remove">X</td>'),
    
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
    }
  });
});