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
import views.html.search;
import views.html.categories;
import views.html.product;
import java.util.*;

/**
 * Created by Joe on 4/30/2015.
 */
public class Search extends Controller{
    public static Result search(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        return ok(search.render("search",user));
    }

    public static Result showAll(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        BookOperations bo = new BookOperations();
        ArrayList<Books> list = bo.getall();
        return ok(categories.render("Books in all categories", list,user));
    }

    /**
     * Searches the DB of Books based on form data
     * @return the view render of the book list retrieved from the DB, or redirect back to /search
     * @todo should redirect back to where the request originated
     */
    public static Result doSearch(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        // Retrieve the form from the POST
        DynamicForm requestData = Form.form().bindFromRequest();
        // Get the "booksearch" field"
        String text = requestData.get("booksearch");

        if (text.length() <= 0) return redirect("/search");

        // Query the DB of books with the search text"
        BookOperations bookOperations = new BookOperations();

        ArrayList<Books> list = bookOperations.search(text);

        if (list.size() > 0) {
            return ok(categories.render("Following books match your search.", list,user)); // TODO fix this too... why cant we pass both args?
        }
        else{
            return ok(categories.render("Not book found!", list,user));
        }
    }
    public static Result getbooks(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        DynamicForm requestData = Form.form().bindFromRequest();
        String id;
        String c;
        id = requestData.get("id");
        if (id == null){id = "";}
        c = requestData.get("category");
        if (c == null){c = "";}
        if (id.length()>0){
            BookOperations bo = new BookOperations();
            Books book = bo.getone(id);

            System.out.println(book.title);
            if(book.title.length() > 0){
                return ok(product.render("Here are the books matching your search", book,user));
            }else {
                return ok(product.render("No book found!", book,user));
            }
        }
        else if(c.length() > 0){
            System.out.println("requested book's category is: " + c);
            BookOperations bo = new BookOperations();
            ArrayList<Books> list = bo.getcategory(c);

            if(list.size() > 0){
                return ok(categories.render("Books in this category", list,user));
            }else {
                return ok(categories.render("No book found in this category!", list,user));
            }
        }
        else {
            ArrayList<Books> list = null;
            return ok(categories.render("No book found", list,user));
        }

    }


    //public static Result advancedsearch(){

    //}
}

