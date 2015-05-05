package controllers;

import models.UserOperations;
import models.Users;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.account;
import views.html.adminindex;

/**
 * Created by PKS on 5/1/15.
 */
public class Administrator extends Controller {
    public static Result index(){
        Http.Session session = Util.getCurrentSession();
        String role = session.get("role");
        String username = session.get("username");

        if (role.equals("3") && username != null) {
            System.out.println("Current user: " + username);
            return ok(views.html.adminindex.render("Welcome back!", session));
        }else{return unauthorized(account.render("Please login first!", session));}

    }
}
