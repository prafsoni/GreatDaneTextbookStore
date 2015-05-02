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
import views.html.product;
import java.util.*;

/**
 * Created by Joe on 4/30/2015.
 */
public class Search extends Controller{
    public static Result search(){
        return ok(search.render("search"));
    }

    public static Result showAll(){
        BookOperations bo = new BookOperations();
        ArrayList<Books> list = bo.getall();
        return ok(categories.render("Categories", list));
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

        if (list.size() > 0) {
            return ok(categories.render("Following books match your search.", list)); // TODO fix this too... why cant we pass both args?
        }
        else{
            return ok(categories.render("Not book found!", list));
        }
    }
    public static Result getone(){
        DynamicForm requestData = Form.form().bindFromRequest();
        String id = requestData.get("id");
        System.out.println(id);
        BookOperations bo = new BookOperations();
        Books book = bo.getone(id);

        System.out.println(book.title);
        if(book.title.length() > 0){
            return ok(product.render("Here are the books matching your search", book));
        }else {
            return redirect("/categories");
        }
    }
    //public static Result advancedsearch(){

    //}
}

