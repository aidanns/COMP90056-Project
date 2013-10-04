package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.webapp;
import views.html.main;

public class Application extends Controller {

    public static Result index() {
        // Render the main page with a title of "WebApp" and the contents of
        // the webapp template in it's body.
        return ok(main.render("WebApp", webapp.render()));
    }

}
