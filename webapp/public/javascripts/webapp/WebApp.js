define(function(require) {
  var Backbone = require("backbone");
  var WebAppRouter = require("webapp/routers/Router");

  return function(root_element) {
    // Create a new instance of the application router and save it on the window.
    window.webapp = {}
    window.webapp.router = new WebAppRouter(root_element);
    // Start routing based on the contents of the browser URL using HTML5 pushstate.
    Backbone.history.start({pushState: true})
  };
})