define(function(require) {
	var Backbone = require("backbone");
	var MatchView = require("webapp/views/MatchView");
	var _ = require("underscore");
	
	return Backbone.View.extend({
		template: _.template(
				'<h2> Matches: </h2>' +
	    		'<table>' +
	    		'  <tr>' +
	    		'    <th>Id</th>' +
	    		'    <th>Timestamp</th>' +
	    		'    <th>Rule #</th>' +
	    		'    <th>IMSI</th>' +
	    		'  </tr>' +
	    		'</table>'),
	    		
	    initialize: function(options) {
	    	this.listenTo(this.collection, "add", this.addOne);
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
	    	var view = new MatchView({model: model});
	    	view.render();
	    	$('table', this.$el).append(view.el);
	    	model.on('remove', view.remove, view);
	    }
	});
});