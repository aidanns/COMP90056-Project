define(function(require) {
	var Backbone = require("backbone");
	var _ = require("underscore");
	
	return Backbone.View.extend({
		
		template: _.template(
				'<h2> Call Volume </h2>' +
				'<table class="callVolume"><tr><th>Timestamp</th><th>Volume</th></tr></table>' +
				'<h2> % Dropped Calls </h2>' +
				'<table class="droppedCalls"><tr><th>Timestamp</th><th>%</th></tr></table>' +
				'<h2> Average Call Duration </h2>' +
				'<table class="averageCallDuration"><tr><th>Timestamp</th><th>Average Duration (seconds)</th></tr></table>'),
				
		lineTemplate: _.template('<tr><td><%= timestamp %></td><td> <%= value %> </td> <td> <%= hashes %> </td></tr>'),
		
		initialize: function() {
			this.listenTo(this.collection, "add", this.render);
		},
		
		render: function() {
			this.$el.html(this.template());
			this.collection.each(_.bind(function(model) {
				
				var callVolume = model.get("callVolume");
				var hashes = "";
				for (var i = 0; i < callVolume / 100; ++i) {
					hashes = hashes + "#";
				}
				
				$(".callVolume", this.$el).append(
						this.lineTemplate({
							timestamp: model.get("timestamp"),
							value: callVolume,
							hashes: hashes}));
				
				var droppedCallVolume = model.get("droppedCallVolume");
				var hashes = "";
				var droppedCallPercent = droppedCallVolume / callVolume;
				for (var i = 0; i < droppedCallPercent * 1000; ++i) {
					hashes = hashes + "#";
				}
				
				$(".droppedCalls", this.$el).append(
						this.lineTemplate({
							timestamp: model.get("timestamp"), 
							value: droppedCallPercent, 
							hashes: hashes}));
				
				var averageCallDuration = model.get("averageCallDuration");
				var hashes = "";
				for (var i = 0; i < averageCallDuration / 25; ++i) {
					hashes = hashes + "#";
				}
				
				$(".averageCallDuration", this.$el).append(
						this.lineTemplate({
							timestamp: model.get("timestamp"), 
							value: averageCallDuration, 
							hashes: hashes}));
			}, this));
			return this;
		}
		
	});
});