package controllers;

import play.cache.Cache;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        // test, get uuid from cache
        String uuid = session("uuid");
        if (uuid != null) {
            String currentUser = (String)Cache.get(uuid+"username");
            System.out.println("Current user: " + currentUser);
        }
        return ok(index.render("GreatDane BookStore."));
    }

}
