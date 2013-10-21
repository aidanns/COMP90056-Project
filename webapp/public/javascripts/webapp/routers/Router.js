define(function(require) {
  var Backbone = require("backbone");
  var Rule = require("webapp/models/Rule");
  var Rules = require("webapp/collections/Rules");
  var RulesView = require("webapp/views/RulesView");
  var HomeView = require("webapp/views/HomeView");
  var EditRuleView = require("webapp/views/EditRuleView");

  return Backbone.Router.extend({
	  
	initialize: function(options) {
		this.el = options.el;
		
		this.$el = $(this.el);
		
		_(this.routes).each(function(destination) {
			_(this.routes).each(function(other) {
				if (destination === other) return;
				this.on('route:' + destination, this['reset_' + other], this);
			}, this);
		}, this);
		
  	  this.rulesCollection = new Rules();
	  this.rulesCollection.fetch();
	},
	  
    // Mapping from route to the function that is called to execute that route.
    routes: {
      "": "home",
      "rules": "rules",
      "edit/:id": "edit",
      "add": "add"
    },

    // When we execute the home route, print something to the console so we know it's working.
    home: function() {
      console.log("Went to the home route.");
      this.homeView = new HomeView().render();
      this.$el.append(this.homeView.el);
    },
    
    reset_home: function() {
    	if (this.homeView) {
    		this.homeView.remove();
    	}
    },
    
    rules: function() {
      console.log("Went to the rules route.");
      this.rulesView = new RulesView({collection: this.rulesCollection}).render();
      this.$el.append(this.rulesView.el);
    },
    
    reset_rules: function() {
      if (this.rulesView) {
    	  this.rulesView.remove();
    	  console.log("removing");
      }
    },
    
    edit: function(id) {
    	console.log("Went to the edit route.");
    	this.editRuleView = new EditRuleView({model: this.rulesCollection.get(id)}).render();
    	this.$el.append(this.editRuleView.el);
    	this.editRuleView.getSaveDeferred().done(_.bind(function() {this.rulesCollection.fetch();}, this));
    },
    
    reset_edit: function() {
    	if (this.editRuleView) {
    		this.editRuleView.remove();
    	}
    },
    
    add: function() {
    	this.addRuleView = new EditRuleView({model: new Rule()}).render();
    	this.$el.append(this.addRuleView.el);
    	this.addRuleView.getSaveDeferred().done(_.bind(function() {this.rulesCollection.fetch();}, this));
    },
    
    reset_add: function() {
    	if (this.addRuleView) {
    		this.addRuleView.remove();
    	}
    }
  });
});