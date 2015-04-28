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

    public static Result doadd(){
        Books book = Form.form(Books.class).bindFromRequest().get();
        BookOperations bo = new BookOperations();
        boolean result = bo.addbook(book);
        if(result){
            return redirect("/");
        }else {
            return redirect("/addproduct");
        }
    }
}
