package controllers;

import models.BookOperations;
import models.Books;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.addproduct;

/**
 * Created by PKS on 4/22/15.
 */
public class Products extends Controller {

    public static Result add(){
        return ok(addproduct.render("add"));
    }

    public static Result doAdd(){
        // Get a book from the form
        Books book = Form.form(Books.class).bindFromRequest().get();
        // Try to add the book to the DB
        if(BookOperations.addbook(book)){
            return redirect("/"); // TODO redirect to where the request came from
        }else {
            // if adding failed, redirect to the addproduct page
            // TODO this can happen is A) the form is incorrect or B) the DB failed. How does the user know?
            return redirect("/addproduct");
        }
    }
}
