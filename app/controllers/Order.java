package controllers;

import models.Books;
import models.OrderOperations;
import models.Orders;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;
import java.util.ArrayList;

/**
 * Created by Rahul Srivastava on 5/6/15.
 */
public class Order extends Controller{

    public static Result getOrder(){
        Http.Session session = Util.getCurrentSession();
        String userid = session.get("uuid");

        if (userid != null){
            OrderOperations oo = new OrderOperations();
            ArrayList<Orders> list = oo.getallplaced(userid);
            if(list.size() > 0){
                return ok(account_orders.render("Your orders", list, session));
            }
            else {
                return ok(account_orders.render("No order found", list, session));
            }
        }
        else {
            return ok(login.render("Please login first!", session));
        }
    }
    public static Result add(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        if(role.equals("1") && username != null) {

            return ok(addproduct.render("add", session));
        }
        else
        {
            return unauthorized(error.render("You must have a seller account to list book! You may register as a seller using the following link.",session));
        }
    }

    public static Result doAdd(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        String uuid = session.get("uuid");
        //check if user is a seller
        if(role.equals("1") || username == null){
            return unauthorized(error.render("You must have buyer account to place the order.",session));
        }

        // Get a order from the form
        Orders or = Form.form(Orders.class).bindFromRequest().get();
        // Try to add the book to the DB
        OrderOperations oo = new OrderOperations();
        ArrayList<Orders> list = oo.getallplaced(uuid);
        if(oo.save(or)){
            return ok(account_orders.render("successfully Ordered!",list, session));
        }else {
            // if adding failed, redirect to the addproduct page
            return ok(account_orders.render("Order failed,Try again", list, session));
        }
    }
    public static Result delete(){
        Http.Session session = Util.getCurrentSession();

        String uuid = session.get("uuid");
        System.out.println("The user is: "+ uuid);


        OrderOperations oo = new OrderOperations();
        ArrayList<Orders> list = oo.getallplaced(uuid);

        if (uuid == null){
            return ok(login.render("Please login first!",session));
        }
        else{
            if(list.size() > 0){
                return ok(account_orders.render("Your order", list, session));
            }
            else {
                return ok(account_orders.render("No order found", list, session));
            }
        }
    }
    public static Result update(){
        Http.Session session = Util.getCurrentSession();
        String id = session.get("id");
        String userid = session.get("uuid");
        System.out.println("userid is: "+ userid);
        if (userid!= null){
            OrderOperations oo = new OrderOperations();
            ArrayList<Orders> list = oo.getallplaced(userid);
            Orders o  = oo.getone(id);
            if(id != null){
                return ok(account_orders.render("Updated order", list, session()));
            }else if(list.size() > 0){
                return ok(account_orders.render("You have not placed any order", list, session));
            }else{
                return ok(account_orders.render("You have not placed any order.", list, session));
            }
        }
        else {
            return ok(login.render("Please login first!", session));
        }

    }



}
