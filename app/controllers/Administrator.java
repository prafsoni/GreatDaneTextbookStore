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
        String username = session.get("username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        if (username != null && user.role.size()==3) {
            System.out.println("Current user: " + username);
            return ok(account.render("Welcome back!", user));
        }else{return unauthorized(account.render("Please login first!", user));}

    }
}
