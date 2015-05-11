package controllers;

import models.OrderOperations;
import models.Orders;
import models.UserOperations;
import models.Users;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;

import java.util.ArrayList;

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
            return ok(adminindex.render("Welcome back!", session));
        }else{return unauthorized(account.render("Please login first!", session));}

    }

    public static Result viewallusers(){
        Http.Session session = Util.getCurrentSession();
        String role = session.get("role");
        String username = session.get("username");
        if (role.equals("3") && username != null) {
            UserOperations uo = new UserOperations();
            ArrayList<Users> users = uo.getall();
            return ok(admin_user.render("users", users, session));
        }else{return unauthorized(account.render("Please login first!", session));}

    }

    public static Result viewallsellers(){
        Http.Session session = Util.getCurrentSession();
        String role = session.get("role");
        String username = session.get("username");
        if (role.equals("3") && username != null) {
            UserOperations uo = new UserOperations();
            ArrayList<Users> users = uo.getallsellers();
            return ok(admin_user.render("sellers",users,session));
        }else{return unauthorized(account.render("Please login first!", session));}
    }

    public static Result viewallorders(){
        Http.Session session = Util.getCurrentSession();
        String role = session.get("role");
        String username = session.get("username");
        if (role.equals("3") && username != null) {
            OrderOperations oo = new OrderOperations();
            ArrayList<Orders> orders = oo.getall();
            return ok(admin_order.render("All Orders",orders,session));
        }else{return unauthorized(account.render("Please login first!", session));}
    }
}
