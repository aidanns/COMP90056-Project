define(function(require) {
  var Backbone = require("backbone");
  var Rule = require("webapp/models/Rule");
  var Rules = require("webapp/collections/Rules");
  var RulesView = require("webapp/views/RulesView");
  var HomeView = require("webapp/views/HomeView");
  var EditRuleView = require("webapp/views/EditRuleView");
  var Matches = require("webapp/collections/Matches");
  var MatchesView = require("webapp/views/MatchesView");
  var MatchesDetailView = require("webapp/views/MatchesDetailView");

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
	  this.matchesCollection = new Matches();
	},
	  
    // Mapping from route to the function that is called to execute that route.
    routes: {
      "": "home",
      "rules": "rules",
      "rules/:id/edit": "edit",
      "rules/add": "add",
      "matches": "matches",
      "matches/:id": "matchesDetail"
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
    	this.rulesCollection.fetch().done(_.bind(function() {
			this.rulesView = new RulesView({collection: this.rulesCollection}).render();
			this.$el.append(this.rulesView.el);
    	}, this));
    },
    
    reset_rules: function() {
      if (this.rulesView) {
    	  this.rulesView.remove();
      }
    },
    
    matches: function() {
    	this.matchesCollection.fetch().done(_.bind(function() {
	    	this.matchesView = new MatchesView({collection: this.matchesCollection}).render();
	    	this.$el.append(this.matchesView.el);
    	}, this));
    },
    
    reset_matches: function() {
    	if (this.matchesView) {
    		this.matchesView.remove();
    	}
    },
    
    matchesDetail: function(id) {
    	this.matchesCollection.fetch().done(_.bind(function() {
    		this.matchesDetailView = new MatchesDetailView({model: this.matchesCollection.get(id)}).render();
    		this.$el.append(this.matchesDetailView.el);
    	}, this));
    },
    
    reset_matchesDetail: function() {
    	if (this.matchesDetailView) {
    		this.matchesDetailView.remove();
    	}
    },
    
    edit: function(id) {
    	this.rulesCollection.fetch().done(_.bind(function() {
	    	this.editRuleView = new EditRuleView({model: this.rulesCollection.get(id)}).render();
	    	this.$el.append(this.editRuleView.el);
	    	this.editRuleView.getSaveDeferred().done(_.bind(function() {this.rulesCollection.fetch();}, this));
    	}, this));
    },
    
    reset_edit: function() {
    	if (this.editRuleView) {
    		this.editRuleView.remove();
    	}
    },
    
    add: function() {
    	this.rulesCollection.fetch().done(_.bind(function() {
	    	this.addRuleView = new EditRuleView({model: new Rule()}).render();
	    	this.$el.append(this.addRuleView.el);
	    	this.addRuleView.getSaveDeferred().done(_.bind(function() {this.rulesCollection.fetch();}, this));
    	}, this));
    },
    
    reset_add: function() {
    	if (this.addRuleView) {
    		this.addRuleView.remove();
    	}
    }
  });
});