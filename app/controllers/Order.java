package controllers;

import models.*;
import play.cache.Cache;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rahul Srivastava on 5/6/15.
 */
public class Order extends Controller {

    public static Result getOrder() {
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        UserOperations uo = new UserOperations();
        String uuid = session.get("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(username); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }

        if (uuid != null) {
            OrderOperations oo = new OrderOperations();
            ArrayList<Orders> list = oo.getallplaced(uuid);
            if (list.size() > 0) {
                return ok(account_orders.render("Your orders", list, session));
            } else {
                return ok(account_orders.render("No order found", list, session));
            }
        } else {
            return ok(login.render("Please login first!", session));
        }
    }

    public static Result getTransaction() {
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        UserOperations uo = new UserOperations();
        String uuid = session.get("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(username); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }

        if (uuid != null) {
            OrderOperations oo = new OrderOperations();
            ArrayList<Orders> list = oo.getallreceived(uuid);
            if (list.size() > 0) {
                return ok(account_orders.render("Your transaction history", list, session));
            } else {
                return ok(account_orders.render("No transaction found", list, session));
            }
        } else {
            return ok(login.render("Please login first!", session));
        }
    }

    private static Date now() {
        return new Date();
    }

    public static Result add() {

        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        if (username == null) {
            return ok(login.render("Please login first!", session));
        }
        UserOperations uo = new UserOperations();
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(username); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }
        if (username == null) {
            return unauthorized(error.render("You must have buyer account to place the order.", session));
        }
        Carts cart = new Carts();
        OrderOperations oo = new OrderOperations();
        Orders o = new Orders();

        if (Cache.get(uuid + "cart") != null) {
            cart = (Carts) Cache.get(uuid + "cart");
            System.out.println(cart.list.get(0).title + "  " + String.valueOf(cart.list.get(0).stock));
            System.out.println(String.valueOf(cart.total));
            ArrayList<Books> list = cart.list;
            if (list.size() > 0) {
                for (Books book : list) {
                    System.out.println(book.title + "  " + String.valueOf(book.stock));
                    HashMap<String, Integer> bmap = new HashMap<>();
                    o.Books = bmap;
                    o.Books.put(book.title, book.stock);
                    o.price = cart.total;
                    o.status = "processing";
                    o.quantity = cart.number;
                    o.Shippingid = "";
                    o.orderdate = now();
                    o.sellerid = "";
                    o.userid = uuid;
                }
            } else {
                return ok(shoppingcart.render("Cart is empty!", cart, session));
            }
        } else {
            return ok(shoppingcart.render("Cart is empty!", cart, session));
        }
        if (oo.save(o)) {
            return ok(thankyou.render("Order placed successfully!", session));
        } else {
            return ok(uploaded.render("Failed! Please try again.", session));
        }
    }

    public static Result doAdd() {
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        String uuid = session.get("uuid");
        //check if user is a seller
        if (username == null) {
            return unauthorized(error.render("You must have buyer account to place the order.", session));
        }

        // Get a order from the form
        Orders or = Form.form(Orders.class).bindFromRequest().get();
        // Try to add the book to the DB
        OrderOperations oo = new OrderOperations();
        ArrayList<Orders> list = oo.getallplaced(uuid);
        if (oo.save(or)) {
            return ok(account_orders.render("successfully Ordered!", list, session));
        } else {
            // if adding failed, redirect to the addproduct page
            return ok(account_orders.render("Order failed,Try again", list, session));
        }
    }

    public static Result delete() {
        Http.Session session = Util.getCurrentSession();

        String uuid = session.get("uuid");
        System.out.println("The user is: " + uuid);


        OrderOperations oo = new OrderOperations();
        ArrayList<Orders> list = oo.getallplaced(uuid);

        if (uuid == null) {
            return ok(login.render("Please login first!", session));
        } else {
            if (list.size() > 0) {
                return ok(account_orders.render("Your order", list, session));
            } else {
                return ok(account_orders.render("No order found", list, session));
            }
        }
    }

    public static Result update() {
        Http.Session session = Util.getCurrentSession();
        String id = session.get("id");
        String userid = session.get("uuid");
        System.out.println("userid is: " + userid);
        if (userid != null) {
            OrderOperations oo = new OrderOperations();
            ArrayList<Orders> list = oo.getallplaced(userid);
            Orders o = oo.getone(id);
            if (id != null) {
                return ok(account_orders.render("Updated order", list, session()));
            } else if (list.size() > 0) {
                return ok(account_orders.render("You have not placed any order", list, session));
            } else {
                return ok(account_orders.render("You have not placed any order.", list, session));
            }
        } else {
            return ok(login.render("Please login first!", session));
        }

    }
}