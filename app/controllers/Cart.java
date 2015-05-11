package controllers;

import models.BookOperations;
import models.Books;
import models.UserOperations;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;
import models.Carts;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import play.cache.Cache;

/**
 * Created by Joe on 5/09/15.
 */
public class Cart extends Controller {


    public static Result add(){
        Http.Session session = Util.getCurrentSession();
        String uname = session.get("username");
        String bookid;
        try{ bookid = Form.form().bindFromRequest().get("bookid");
        }catch(Exception ex){
            System.out.println(ex);
            return ok(uploaded.render("System error occurred. Please try again!", session));
        }

        String q;
        try{ q = Form.form().bindFromRequest().get("quantity");
        }catch(Exception ex){
            System.out.println(ex);
            return ok(uploaded.render("Please enter quantity", session));
        }

        if(uname == null){
            return unauthorized(login.render("Please login first!", session));
        }
        UserOperations uo = new UserOperations();
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }
        Carts cart = new Carts();
        if(Cache.get(uuid + "cart") != null){
            cart = (Carts) Cache.get(uuid + "cart");
        }
        int quantity = 0;
        try{
            if (q!=null){
            quantity = Integer.valueOf(q);
            }
        }catch(Exception ex){
            System.out.println(ex);
            return ok(uploaded.render("Please enter quantity", session));
        }
        BookOperations bo = new BookOperations();
        Books book = bo.getone(bookid);

        // if list is not empty, find it and update quantity using stock, replace the old book object in list
        ArrayList<String> ids = new ArrayList<>();
        if (cart.list != null ){ // list is not empty
            ids = cart.getIdList();
            if (ids.contains(book.id.toHexString())){ //list contains the book
                int idx = -1;
                for (int i=0;i < cart.list.size();i++) {
                    if (cart.list.get(i).title.equals(book.title)) {
                        idx = i;
                        break;
                    }
                }
                book.stock = cart.list.get(idx).stock + quantity;
                cart.list.remove(idx);
                cart.list.add(book);
                cart.update();
                Cache.set(uuid+"cart", cart);
            }else{ // list doesn't contain the book
                book.stock = quantity;
                cart.list.add(book);
                cart.update();
                Cache.set(uuid + "cart", cart);
            }
        }else{ // list is empty
            book.stock = quantity;
            cart.list.add(book);
            cart.update();
            Cache.set(uuid + "cart", cart);
        }
        cart.buyer = uuid;
        Cache.set(uuid + "cart", cart);
        return ok(shoppingcart.render("Your Shopping Cart",cart, session));
    }

    public static Result getCart(){
        Http.Session session = Util.getCurrentSession();
        String uname = session.get("username");
        if(uname == null){
            return unauthorized(login.render("Please login first!", session));
        }
        UserOperations uo = new UserOperations();
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }
        Carts cart = new Carts();
        if(Cache.get(uuid + "cart") != null){
            cart = (Carts) Cache.get(uuid + "cart");
        }
        return ok(shoppingcart.render("Your Shopping Cart", cart, session));
    }
    public static Result delete(){
        Http.Session session = Util.getCurrentSession();
        String uname = session.get("username");
        String bookid = Form.form().bindFromRequest().get("bookid");
        if(uname == null){
            return unauthorized(login.render("Please login first!", session));
        }
        UserOperations uo = new UserOperations();
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);

        }
        Carts cart = new Carts();
        if(Cache.get(uuid + "cart") != null){
            cart = (Carts) Cache.get(uuid + "cart");
        }
        BookOperations bo = new BookOperations();
        Books book = bo.getone(bookid);

        if(cart.list == null){
            return ok(shoppingcart.render("You shopping cart is empty!",cart,session));
        }

        // Find the book if it exists and remove it from list
        int idx = -1;
        for (int i=0;i < cart.list.size();i++) {
            if (cart.list.get(i).title.equals(book.title)) {
                idx = i;
                break;
            }
        }
        if (idx == -1){
            return ok(error.render("Book is not in your cart!",session()));
        }
        cart.list.remove(idx);
        cart.update();
        Cache.set(uuid + "cart", cart);
        return ok(shoppingcart.render("Your Shopping Cart",cart, session));
    }
    public static Result clear(){
        Http.Session session = Util.getCurrentSession();
        String uname = session.get("username");
        if(uname == null){
            return unauthorized(login.render("Please login first!", session));
        }
        UserOperations uo = new UserOperations();
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }
        Carts cart = new Carts();
        if(Cache.get(uuid + "cart") != null){
            cart = (Carts) Cache.get(uuid + "cart");
        }
        cart.clear();
        cart.update();
        Cache.remove(uuid + "cart");
        return ok(shoppingcart.render("Your Shopping Cart",cart,session));
    }

    public static Result update(){
        Http.Session session = Util.getCurrentSession();
        String uname = session.get("username");
        String bookid = Form.form().bindFromRequest().get("bookid");
        System.out.println("bookid: " + bookid);
        String q = Form.form().bindFromRequest().get("quantity");

        if(uname == null){
            return unauthorized(login.render("Please login first!", session));
        }
        UserOperations uo = new UserOperations();
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }
        Carts cart = new Carts();
        if(Cache.get(uuid + "cart") != null){
            cart = (Carts) Cache.get(uuid + "cart");
        }
        int quantity = 0;
        if (q!=null){
            quantity = Integer.valueOf(q);
        }else{
            return ok(shoppingcart.render("Please input new quantity!", cart, session));
        }

        BookOperations bo = new BookOperations();
        Books book = bo.getone(bookid);

        // if list is not empty, find it and update quantity using stock, replace the old book object in list
        ArrayList<String> ids = new ArrayList<>();
        if (cart.list != null ){ // list is not empty
            ids = cart.getIdList();
            if (ids.contains(book.id.toHexString())){ //list contains the book
                int idx = -1;
                for (int i=0;i < cart.list.size();i++) {
                    if (cart.list.get(i).title.equals(book.title)) {
                        idx = i;
                        break;
                    }
                }
                System.out.println("updated q: " + quantity + cart.list.get(idx).title + String.valueOf(cart.list.size()));
                book = cart.list.get(idx);
                book.stock = quantity;
                cart.list.remove(idx);
                cart.list.add(book);
                cart.update();
                Cache.set(uuid+"cart", cart);
                if (cart.number <=0){
                    cart.clear();
                    Cache.set(uuid+"cart", cart);
                    return ok(shoppingcart.render("Your Shopping Cart Is Empty!",cart, session));
                }
            }else{ // list doesn't contain the book
                return ok(shoppingcart.render("Updating failed!",cart, session));
            }
        }else{ // list is empty
            return ok(shoppingcart.render("Your Shopping Cart Is Empty!",cart, session));
        }

        Cache.set(uuid + "cart", cart);
        return ok(shoppingcart.render("Your Shopping Cart",cart, session));
    }

}
