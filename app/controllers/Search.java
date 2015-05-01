package controllers;

import models.BookOperations;
import models.Books;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.search;
import views.html.categories;
import java.util.*;

/**
 * Created by Joe on 4/30/2015.
 */
public class Search extends Controller{
    public static Result search(){
        return ok(search.render("search"));
    }

    public static Result showall(){
        String text = "";
        BookOperations bo = new BookOperations();
        ArrayList<Books> list = new ArrayList<>();//for temporary use due to DB failure, should be: bo.search(text);
        return ok(categories.render("Categories", list));

    }

    public static Result dosearch(){
        DynamicForm requestData = Form.form().bindFromRequest();
        String text = requestData.get("booksearch");
        BookOperations bo = new BookOperations();
        ArrayList<Books> list = bo.search(text);
        //list.addAll(BookOperations.search(text));

        if(text.length() > 0){
            return ok(categories.render("Following books match your search", list));
        }else {
            return redirect("/search");
        }
    }
    //public static Result advancedsearch(){

    //}
}
