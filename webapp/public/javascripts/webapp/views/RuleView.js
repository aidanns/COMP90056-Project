define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");
  var Rule = require("webapp/models/Rule");

  return Backbone.View.extend({
    template: _.template(
    		'<span class="rule">' +
    		'    <span class="name"> {{ name }} </span>' +
    		'    <span class="delete">X</span>' +
    		'</span>'),
    
    initialize: function(options) {
    	this.listenTo(this.model, "change", this.render);
    },
    
    render: function() {
    	this.$el.html(this.template(this.model.toJSON()));
    	return this;
    }
  });
});