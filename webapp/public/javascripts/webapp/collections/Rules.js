define(function(require) {
  var Backbone = require("backbone");
  var Rule = require ("webapp/models/Rule");

  return Backbone.Collection.extend({
    model: Rule,
    url: 'api/rule'
  });
});