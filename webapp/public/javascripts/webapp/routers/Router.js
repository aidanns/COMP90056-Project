define(function(require) {
  var Backbone = require("backbone");
  var Rules = require("webapp/collections/Rules");
  var RulesView = require("webapp/views/RulesView");

  return Backbone.Router.extend({
	  
	initialize: function(options) {
		this.el = options.el;
	},
	  
    // Mapping from route to the function that is called to execute that route.
    routes: {
      "": "rules",
      "rules": "rules"
    },

    // When we execute the home route, print something to the console so we know it's working.
    home: function() {
      console.log("Went to the home route.");
    },
    
    rules: function() {
      console.log("Went to the rules route.");
      var rules = new Rules();
      rules.fetch();
      var rulesView = new RulesView({collection: rules, el: this.el}).render();
    }
  });
});