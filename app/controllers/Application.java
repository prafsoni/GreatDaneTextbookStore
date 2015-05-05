package controllers;

import models.BookOperations;
import models.Books;
import models.UserOperations;
import models.Users;
import play.cache.Cache;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;

public class Application extends Controller {

    public static Result index() {

        //String username = Util.getFromUserCache("uuid", "username");
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        BookOperations bo = new BookOperations();
        ArrayList<Books> list = bo.getThree();

        if (username != null) {
            System.out.println("Current user: " + username);

            return ok(index.render("GreatDane BookStore.",list,session));

        }
        System.out.println("Current user: " + username);

        return ok(index.render("GreatDane BookStore.",list,session));
    }
    public static Result logout() {
        Http.Session session = Util.getCurrentSession();
        session().clear();
        flash("success", "You've been logged out");
        return ok(login.render("Please login first!",session));
    }

}
