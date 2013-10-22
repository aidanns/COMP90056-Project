define(function(require) {
	var Backbone = require("backbone");
	var _ = require("underscore");
	
	return Backbone.View.extend({
		
		template: _.template(
				'<h2> Match <%= id %> Detail </h2>' +
				'<p> <strong> Rule #: </strong> <%= ruleId %> </p>' +
				'<p> <strong> Timestamp: </strong> <%= timestamp %> </p>' +
				'<p> <strong> IMSI: </strong> <%= imsi %> </p>' +
				'<h3> Contributing CDRs: </h3>' +
	    		'<table>' +
	    		'  <tr>' +
	    		'    <th>CDR Type</th>' +
	    		'    <th>IMSI</th>' +
	    		'    <th>IMEI</th>' +
	    		'    <th>Calling Number</th>' +
	    		'    <th>Called Number</th>' +
	    		'    <th>Recording Entity</th>' +
	    		'    <th>Location</th>' +
	    		'    <th>Call Reference</th>' +
	    		'    <th>Call Duration</th>' +
	    		'    <th>Answer Time</th>' +
	    		'    <th>Seizure Time</th>' +
	    		'    <th>Release Time</th>' +
	    		'    <th>Cause For Termination</th>' +
	    		'    <th>Basic Service</th>' +
	    		'    <th>MSC Address</th>' +
	    		'  </tr>' +
	    		'</table>'),
	    		
	    cdrTemplate: _.template(
	    		'  <tr>' +
	    		'    <td><%= cdrType %></td>' +
	    		'    <td><%= imsi %></td>' +
	    		'    <td><%= imei %></td>' +
	    		'    <td><%= callingNumber %></td>' +
	    		'    <td><%= calledNumber %></td>' +
	    		'    <td><%= recordingEntity %></td>' +
	    		'    <td><%= location %></td>' +
	    		'    <td><%= callReference %></td>' +
	    		'    <td><%= callDuration %></td>' +
	    		'    <td><%= answerTime %></td>' +
	    		'    <td><%= seizureTime %></td>' +
	    		'    <td><%= releaseTime %></td>' +
	    		'    <td><%= causeForTermination %></td>' +
	    		'    <td><%= basicService %></td>' +
	    		'    <td><%= mscAddress %></td>' +
	    		'  </tr>'),
	    
	    render: function() {
	    	this.$el.html(this.template(this.model.toJSON()));
	    	var cdrs = this.model.get("callDataRecords");
	    	for (var i = 0; i < cdrs.length; ++i) {
	    		$('table', this.$el).append(this.cdrTemplate(cdrs[i]));
	    	}
	    	return this;
	    }
	});
});