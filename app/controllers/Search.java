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

    public static Result showAll(){
//        String text = "";
//        BookOperations bo = new BookOperations();
        ArrayList<Books> list = new ArrayList<>();//for temporary use due to DB failure, should be: bo.search(text);
//        return ok(categories.render("Categories", list));
        return ok(categories.render("test", list)); // TODO fix this
    }

    /**
     * Searches the DB of Books based on form data
     * @return the view render of the book list retrieved from the DB, or redirect back to /search
     * @todo should redirect back to where the request originated
     */
    public static Result doSearch(){
        // Retrieve the form from the POST
        DynamicForm requestData = Form.form().bindFromRequest();
        // Get the "booksearch" field"
        String text = requestData.get("booksearch");

        if (text.length() <= 0) return redirect("/search");

        // Query the DB of books with the search text"
        ArrayList<Books> list = BookOperations.search(text);

        return ok(categories.render("test2", list)); // TODO fix this too... why cant we pass both args?
    }
}
