package controllers;

import play.cache.Cache;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {

        String username = Util.getFromUserCache("uuid", "username");

        if (username != null) {
            System.out.println("Current user: " + username);
        }

        return ok(index.render("GreatDane BookStore."));
    }

}
