// Require.js config and bootstrap for the application.
// Create by Aidan Nagorcka-Smith (aidanns@gmail.com)

require.config({
  // Mapping from the name we use for a library to it's location, relative to the root.
  paths: {
    jquery: 'lib/jquery-1.10.2',
    underscore: 'lib/underscore-1.5.2',
    backbone: 'lib/backbone-1.0.0'
  },
  // Config for libraries that don't use the AMD format already.
  shim: {
    underscore: {
      exports: "_"
    },
    backbone: {
      deps: ['underscore', 'jquery'],
      exports: 'Backbone'
    }
  }
});

// Entry point for our application.
require(['webapp/WebApp', 'jquery'], function(WebApp, $) {
  // Create a new app and attach it to the element with id "webapp".
  new WebApp($('#webapp'));
});