define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");
  var Rule = require("webapp/models/Rule");

  return Backbone.View.extend({
	
	tagName: "tr",
	
	className: "rule",
	
    template: _.template(
    		'<td class="name"> {{ name }} </td>' +
    		'<td class="delete">X</td>'),
    
    initialize: function(options) {
    	this.listenTo(this.model, "change", this.render);
    },
    
    render: function() {
    	this.$el.html(this.template(this.model.toJSON()));
    	return this;
    }
  });
});