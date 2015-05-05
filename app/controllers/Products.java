package controllers;

import models.BookOperations;
import models.Books;
import models.UserOperations;
import models.Users;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.addproduct;
import views.html.error;
import views.html.productadded;
/**
 * Created by PKS on 4/22/15.
 */
public class Products extends Controller {


    public static Result add(){
        //String username = Util.getFromUserCache("uuid", "username");
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        //check if user is a seller
        if(role.equals("1") || username == null){
            return unauthorized(error.render("You must have a seller account to list book! You may register as a seller using the following link.",session));
        }
        return ok(addproduct.render("add",session));
    }

    public static Result doAdd(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        //check if user is a seller
        if(role.equals("1") || username == null){
            return unauthorized(error.render("You must have a seller account to list book! You may register as a seller using the following link.",session));
        }

        // Get a book from the form
        Books book = Form.form(Books.class).bindFromRequest().get();
        book.seller = session.get("uuid");
        // Try to add the book to the DB
        BookOperations bookOperations = new BookOperations();

        if(bookOperations.addbook(book)){
            return ok(productadded.render("You book was listed successfully!",session));
        }else {
            // if adding failed, redirect to the addproduct page
            return ok(productadded.render("Listing failed", session));
        }
    }
}
