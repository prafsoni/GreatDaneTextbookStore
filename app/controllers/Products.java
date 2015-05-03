package controllers;

import models.BookOperations;
import models.Books;
import models.UserOperations;
import models.Users;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.addproduct;
import views.html.productadded;
/**
 * Created by PKS on 4/22/15.
 */
public class Products extends Controller {


    public static Result add(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        return ok(addproduct.render("add",user));
    }

    public static Result doAdd(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        // Get a book from the form
        Books book = Form.form(Books.class).bindFromRequest().get();
        // Try to add the book to the DB
        BookOperations bookOperations = new BookOperations();

        if(bookOperations.addbook(book)){
            return ok(productadded.render("You book was listed successfully!",user));
        }else {
            // if adding failed, redirect to the addproduct page
            // TODO this can happen is A) the form is incorrect or B) the DB failed. How does the user know?
            return redirect("/addproduct");
        }
    }
}
