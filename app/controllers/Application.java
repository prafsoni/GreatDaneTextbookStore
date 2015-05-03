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

        String username = Util.getFromUserCache("uuid", "username");
        BookOperations bo = new BookOperations();
        ArrayList<Books> list = bo.getThree();
        Users user = new Users();
        UserOperations uo = new UserOperations();
        if (username != null) {
            System.out.println("Current user: " + username);
            user = uo.getuserbyuname(username);
            return ok(index.render("GreatDane BookStore.",list,user));

        }

        return ok(index.render("GreatDane BookStore.",list,user));
    }
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        Users user = new Users();
        return ok(login.render("Please login first!",user));
    }

}
