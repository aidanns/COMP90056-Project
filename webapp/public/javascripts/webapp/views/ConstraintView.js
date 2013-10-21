define(function(require) {
  var Backbone = require("backbone");
  var _ = require("underscore");

	var makeNewStringConstraint = function() {
		return {
			type: "stringEquality",
			field: "CDRType",
			value: ""
		}
	};
	
	var makeNewOrConstraint = function() {
		return {
			type: "or",
			firstChild: makeNewStringConstraint(),
			secondChild: makeNewStringConstraint()
		}
	};
	
	var makeNewAndConstraint = function() {
		return {
			type: "and",
			firstChild: makeNewStringConstraint(),
			secondChild: makeNewStringConstraint()
		}
	};
  
  // This class is a massive dirty hack.
  var ConstraintView = Backbone.View.extend({
	  
    selectionTemplate: _.template(
    		'<select class="constraintTypeSelection">' +
    		'  <option value="and">And</option>' +
    		'  <option value="or">Or</option>' +
    		'  <option value="stringEquality">String Equality</option>' +
    		'</select>'),
    		
    andTemplate: _.template('<div class="first" style="padding-left:50px;"></div><div class="second" style="padding-left:50px;"></div>'),
    
    orTemplate: _.template('<div class="first" style="padding-left:50px;"></div><div class="second" style="padding-left:50px;"></div>'),
    
    stringEqualityTempalte: _.template(
    		'<label>Field</label>' +
    		'<select class="selectField">' +
    		'  <option>CDRType</option>' +
    		'  <option>IMSI</option>' +
    		'  <option>IMEI</option>' +
    		'  <option>CallingNumber</option>' +
    		'  <option>CalledNumber</option>' +
    		'  <option>RecordingEntitiy</option>' +
    		'  <option>Location</option>' +
    		'  <option>CallReference</option>' +
    		'  <option>CallDuration</option>' +
    		'  <option>AnswerTime</option>' +
    		'  <option>SeizureTime</option>' +
    		'  <option>ReleaseTime</option>' +
    		'  <option>CauseForTermination</option>' +
    		'  <option>BasicService</option>' +
    		'  <option>MSCAddress</option>' +
    		'</select>' +
    		'<label for="inputValue">Value</label>' +
    		'<input class="inputValue"></input>'),
    
    events: {
    	"change .constraintTypeSelection": "changeConstraintType",
    	"change .selectField": "changeSelectedField",
    	"change .inputValue": "changeInputValue"
    },
    		
    initialize: function(options) {
    	this.constraint = options.constraint || makeNewStringConstraint();
    	if (this.constraint.type == "or" || this.constraint.type == "and") {
    		this.firstChild = new ConstraintView({constraint: this.constraint.firstChild});
    		this.secondChild = new ConstraintView({constraint: this.constraint.secondChild});
    	}
    },
    
    render: function() {
    	this.$el.html(this.selectionTemplate());
    	$('.constraintTypeSelection option[value=' + this.constraint.type + ']', this.$el).prop('selected', true);
    	switch(this.constraint.type) {
    	case "stringEquality":
    		this.$el.append(this.stringEqualityTempalte());
    		$('.selectField option:contains("' + this.constraint.field + '")', this.$el).prop('selected', true);
    		$('.inputValue', this.$el).val(this.constraint.value);
    		break;
    	case "and":
    		this.$el.append(this.andTemplate());
    		$(".first", this.$el).html(this.firstChild.render().el);
    		$(".second", this.$el).html(this.secondChild.render().el);
    		break;
    	case "or":
    		this.$el.append(this.orTemplate());
    		$(".first", this.$el).html(this.firstChild.render().el);
    		$(".second", this.$el).html(this.secondChild.render().el);
    		break;
    	}
    	return this;
    },
    
    getConstraint: function() {
    	if (this.firstChild) { this.constraint.firstChild = this.firstChild.getConstraint(); }
    	if (this.secondChild) { this.constraint.secondChild = this.secondChild.getConstraint(); }
    	return this.constraint
    },
    
    changeConstraintType: function() {
    	var type = $('.constraintTypeSelection', this.$el).find(":selected").val();
    	switch(type) {
    	case "or":
    		this.constraint = makeNewOrConstraint();
    		this.firstChild = new ConstraintView({constraint: this.constraint.firstChild});
    		this.secondChild = new ConstraintView({constraint: this.constraint.secondChild});
    		break;
    	case "and":
	    	this.constraint = makeNewAndConstraint();
    		this.firstChild = new ConstraintView({constraint: this.constraint.firstChild});
    		this.secondChild = new ConstraintView({constraint: this.constraint.secondChild});
			break;
    	case "stringEquality":
    		this.constraint = makeNewStringConstraint();
    		break;
    	}
    	this.render();
    },
    
    changeSelectedField: function() {
    	this.constraint.field = $(".selectField", this.$el).find(":selected").text();
    },
    
    changeInputValue: function() {
    	this.constraint.value = $(".inputValue", this.$el).val();
    }
  });
  return ConstraintView;
});
