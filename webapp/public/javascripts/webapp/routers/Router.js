define(function(require) {
  var Backbone = require("backbone");

  return Backbone.Router.extend({
    // Mapping from route to the function that is called to execute that route.
    routes: {
      "": "home"
    },

    // When we execute the home route, print something to the console so we know it's working.
    home: function() {
      console.log("Went to the home route.");
    }
  });
});