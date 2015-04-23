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
        DynamicForm requestData = Form.form().bindFromRequest();
        Books book = new Books();
        book.title = requestData.get("title");
        book.authors= requestData.get("authors");
        //book.edition = requestData.get("edition");
        book.isbn = requestData.get("isbn");
        book.picid = requestData.get("picid");
        //book.price = requestData.get("price");
        //book.year = requestData.get("year");
        //book.stock = requestData.get("stock");
        book.seller = "greatdanebookstore";
        BookOperations bo = new BookOperations();
        boolean result = bo.addbook(book);
        if(result){
            return redirect("/");
        }else {
            return redirect("/addproduct");
        }
    }
}
