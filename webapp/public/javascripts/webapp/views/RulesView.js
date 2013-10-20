define(function(require) {
  var Backbone = require("backbone");
  var RuleView = require("webapp/views/RuleView");

  return Backbone.View.extend({
    template: _.template('<h2> Here is a list of Rules: </h2>'),
    
    initialize: function(options) {
    	this.listenTo(this.collection, 'add', this.addOne);
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
    	this.$el.append(view.el);
    	model.on('remove', view.remove, view);
    }
  });
});