package controllers;



import models.ContactMsg;
import models.ContactMsgOperations;
import models.UserOperations;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rahul Srivastava on 5/5/15.
 */
public class Contacts extends Controller{
    private static Date now() {return new Date();}
    public static Result contact(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String uuid = session.get("uuid");
        String receiver = Form.form().bindFromRequest().get("receiver");
        if(username == null){
            return unauthorized(login.render("Please login first!",session));
        }

        return ok(contact.render("Send a message to user", receiver, session));

    }
    public static Result contactAdmin(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        if(username == null){
            return unauthorized(login.render("Please login first!", session));
        }
        return ok(contactAdmin.render("Send a message to user", session));
    }

    public static Result savemsg(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String uuid = session.get("uuid");
        String role = session.get("role");

        if(username == null){
            return unauthorized(login.render("Please login first!",session));
        }

        String receiver = Form.form().bindFromRequest().get("receiver");
        UserOperations uo = new UserOperations();
        //String rid = uo.getuserid(receiver);

        ContactMsg msg = new ContactMsg();

        msg.receiverid = receiver;
        msg.senderid = username;
        msg.sdate = now();
        msg.subject = Form.form().bindFromRequest().get("reason") + ": " + Form.form().bindFromRequest().get("title");
        msg.msg = Form.form().bindFromRequest().get("message");

        ContactMsgOperations cmo = new ContactMsgOperations();

        if(cmo.save(msg)){
            return ok(uploaded.render("Message successfully sent", session));
        }else {
            // if adding failed, redirect to the addproduct page
            return ok(uploaded.render("Failed! Please try again.", session));
        }
    }
    public static Result getall() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("username");
        if (u == null) {
            return ok(login.render("Please login first!", session));
        }else {
            ContactMsgOperations cmo = new ContactMsgOperations();
            ArrayList<ContactMsg> list = cmo.getall(u);
            if (list.size() > 0) {
                return ok(messagebox.render("Your Messages", list, session));
            } else {
                return ok(messagebox.render("No Messages.", list, session));
            }
        }
    }
    public static Result getallsentmsg() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("username");
        if (u != null) {
            ContactMsgOperations cmo = new ContactMsgOperations();

            ArrayList<ContactMsg> list = cmo.getallsent(u);
            if (list.size() > 0) {
                return ok(messagesent.render("Messages Sent", list, session));
            } else {
                return ok(messagesent.render("No Messages.", list, session));
            }
        } else {
            return ok(login.render("Please login first!", session));
        }
    }
    public static Result getallreceived() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("username");
        String role = session.get("role");
        ArrayList<ContactMsg> list = new ArrayList<>();
        if (u != null) {
            ContactMsgOperations cmo = new ContactMsgOperations();
            if(role.equals("3")) {
                list = cmo.getallreceived("admin");
            }else{
                list = cmo.getallreceived(u);
            }
            if (list.size() > 0) {
                return ok(messagebox.render("Messages Received", list, session));
            } else {
                return ok(messagebox.render("No Messages.", list, session));
            }
        } else {
            return ok(login.render("Please login first!", session));
        }
    }
    public static Result getone() {
        Http.Session session = Util.getCurrentSession();
        String u = session.get("username");
        String msgid = Form.form().bindFromRequest().get("msgid");
        if (u != null) {
            ContactMsgOperations cmo = new ContactMsgOperations();

            ContactMsg msg = cmo.getone(msgid);
            if (msg!=null) {
                return ok(message.render("Messages Sent", msg, session));
            } else {
                return ok(message.render("Failed to retrieve the message!", msg, session));
            }
        } else {
            return ok(login.render("Please login first!", session));
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
