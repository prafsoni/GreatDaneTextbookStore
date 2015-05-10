package controllers;



import models.ContactMsg;
import models.ContactMsgOperations;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;

/**
 * Created by Rahul Srivastava on 5/5/15.
 */
public class Contacts extends Controller{
    public static Result contact(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");

        return ok(contact.render("Send a message to user",session));
    }
    public static Result contactAdmin(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");

        return ok(contact.render("Send a message to Admin",session));
    }

    public static Result add(){

        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        if(role.equals("1") && username != null) {

            return ok(contact.render("add", session));
        }

        return unauthorized(error.render("Buyer can send message to seller", session));
    }


    public static Result savemsg(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        //check if user is a buyer
        if(role.equals(username == null || role.equals("2"))){
            return unauthorized(error.render("You must have a buyer account if you want to communicate with seller.",session));
        }


        ContactMsg conmsg = Form.form(ContactMsg.class).bindFromRequest().get();
        // book.seller = session.get("uuid");
        // Try to add the book to the DB
        ContactMsgOperations cmo = new ContactMsgOperations();

        if(cmo.save(conmsg)){
            return ok(msgsent.render("Successfuly sent", session));
        }else {
            // if adding failed, redirect to the addproduct page
            return ok(error.render("Message failed,Please go back and try again", session));
        }
    }
    public static Result getall() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("uuid");
        System.out.println("Buyer is: " + u);
        if (u == null) {
            return ok(login.render("Please login first!", session));
        }

        if (u != null) {
            ContactMsgOperations cmo = new ContactMsgOperations();
            ArrayList<ContactMsg> list = cmo.getall(u);
            if (list.size() > 0) {
                return ok(messagebox.render("Your messages", list, session));
            } else {
                return ok(messagebox.render("No Messages.", list, session));
            }
        } else {
            ArrayList<ContactMsg> list = new ArrayList<>();
            return ok(messagebox.render("No message found", list, session));
        }
    }
    public static Result getallsentmsg() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("uuid");
        System.out.println("Buyer is: " + u);
        if (u == null) {
            return ok(login.render("Please login first!", session));
        }

        if (u != null) {
            ContactMsgOperations cmo = new ContactMsgOperations();
            ArrayList<ContactMsg> list = cmo.getallsent(u);
            if (list.size() > 0) {
                return ok(messagebox.render("Your messages", list, session));
            } else {
                return ok(messagebox.render("No Messages.", list, session));
            }
        } else {
            ArrayList<ContactMsg> list = new ArrayList<>();
            return ok(messagebox.render("No message found", list, session));
        }
    }
    public static Result getallreceived() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("uuid");
        System.out.println("Buyer is: " + u);
        if (u == null) {
            return ok(login.render("Please login first!", session));
        }

        if (u != null) {
            ContactMsgOperations cmo = new ContactMsgOperations();
            ArrayList<ContactMsg> list = cmo.getallreceived(u);
            if (list.size() > 0) {
                return ok(messagebox.render("Received messages", list, session));
            } else {
                return ok(messagebox.render("No Messages.", list, session));
            }
        } else {
            ArrayList<ContactMsg> list = new ArrayList<>();
            return ok(messagebox.render("No message found", list, session));
        }
    }

    public static Result delete(){
        Http.Session session = Util.getCurrentSession();
        //DynamicForm requestData = Form.form().bindFromRequest();
        String u = session.get("uuid");
        System.out.println("user is: "+ u);
        if (u == null){return ok(login.render("Please login first!", session));}

        if (u != null){
            ContactMsgOperations cmo = new ContactMsgOperations();
            ArrayList<ContactMsg> list = cmo.getall(u);
            if(list.size() > 0){
                return ok(messagebox.render("Your Messages", list, session));
            }else {
                return ok(messagebox.render("No Messages.", list, session));
            }
        }
        else {
            ArrayList<ContactMsg> list = new ArrayList<>();
            return ok(messagebox.render("No Messages found", list,session));
        }
    }
}
