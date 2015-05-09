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
import views.html.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

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
        return ok(addproduct.render("Add a book to inventory",session));
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
    public static Result getInventory(){
        Http.Session session = Util.getCurrentSession();
        //DynamicForm requestData = Form.form().bindFromRequest();

        String seller = session.get("uuid");
        System.out.println("seller is: "+seller);
        if (seller == null){return ok(login.render("Please login first!", session));}

        if (seller != null){
            BookOperations bo = new BookOperations();
            ArrayList<Books> list = bo.getInventory(seller);
            if(list.size() > 0){
                return ok(account_inventory.render("Your inventory", list, session));
            }else {
                return ok(account_inventory.render("You do not have any book yet. Please use the bellow link to list books.", list, session));
            }
        }
        else {
            ArrayList<Books> list = new ArrayList<>();
            return ok(account_inventory.render("No book found", list, session));
        }
    }

    // Redirect to the update page
    public static Result update(){
        String bookid = Form.form().bindFromRequest().get("bookid");
        BookOperations bo = new BookOperations();
        Books book = bo.getone(bookid);
        return ok(updateproduct.render("Update Book Info", book, session()));
    }

    //TODO This is a method copied from getInventory, please make it to update and render message, book, session into updateproduct
    public static Result doupdate(){
        Http.Session session = Util.getCurrentSession();
        //DynamicForm requestData = Form.form().bindFromRequest();
        String seller = session.get("uuid");
        System.out.println("seller is: "+seller);
        if (seller == null){return ok(login.render("Please login first!", session));}

        if (seller != null){
            BookOperations bo = new BookOperations();
            ArrayList<Books> list = bo.getInventory(seller);
            if(list.size() > 0){
                return ok(account_inventory.render("Your inventory", list, session));
            }else {
                return ok(account_inventory.render("You do not have any book yet. Please use the bellow link to list books.", list, session));
            }
        }
        else {
            ArrayList<Books> list = new ArrayList<>();
            return ok(account_inventory.render("No book found", list,session));
        }
    }

    //TODO This is a method copied from getInventory, please make it to delete and render message, list, session into account_inventory
    public static Result delete(){
        Http.Session session = Util.getCurrentSession();
        //DynamicForm requestData = Form.form().bindFromRequest();
        String seller = session.get("uuid");
        System.out.println("seller is: "+seller);
        if (seller == null){return ok(login.render("Please login first!", session));}

        if (seller != null){
            BookOperations bo = new BookOperations();
            ArrayList<Books> list = bo.getInventory(seller);
            if(list.size() > 0){
                return ok(account_inventory.render("Your inventory", list, session));
            }else {
                return ok(account_inventory.render("You do not have any book yet. Please use the bellow link to list books.", list, session));
            }
        }
        else {
            ArrayList<Books> list = new ArrayList<>();
            return ok(account_inventory.render("No book found", list,session));
        }
    }

    public static  Result upload() {
        String bookid = Form.form().bindFromRequest().get("bookid");
        Util.insertIntoSession("bookid", bookid);
        System.out.println("bookid: "+bookid);
        return ok(addproductpic.render("Upload your Profile Cover",session()));
    }

    public static Result doupload(String bookid){

        if(session().get("uuid")!=null) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart picture = body.getFile("picture");
            if (picture != null) {
                String fileName = picture.getFilename();
                String contentType = picture.getContentType();

                try {
                    String current = new java.io.File(".").getCanonicalPath();
                    System.out.println("Current dir:" + current);
                    File file = new File(picture.getFile(), session().get("uuid") + ".jpg");
                    byte[] data = Files.readAllBytes(picture.getFile().toPath());
                    FileOutputStream fos = new FileOutputStream(current + "/public/images/productpics/" + bookid + ".jpg");
                    fos.write(data);
                    System.out.println("File uploaded to: " + current + "/public/images/productpics/" + bookid + ".jpg");
                } catch (Exception e) {
                    System.out.print(e);
                }
                return ok(uploaded.render("Uploaded Successfully!", session()));
            } else {
                flash("error", "Missing file");
                return redirect("/");
            }
        }else {
            return unauthorized(login.render("Please login first!", session()));
        }
    }

}
