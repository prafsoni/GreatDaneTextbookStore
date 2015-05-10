package controllers;

import models.PaymentOperations;
import models.Payments;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;

/**
 * Created by Rahul Srivastava on 5/6/15.
 */
public class Payment extends Controller {

    public static Result add(){

        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        //check if user is a buyer
        if(role.equals("1") && username != null){
            return ok(account_payment.render("add", session));
        }
        else
        {
            return unauthorized(login.render("You must have a buyer account", session));
        }
    }
    public static Result doAdd()
    {
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String role = session.get("role");
        //check if user is a buyer
        if(role.equals("1") && username != null)
        {
            // Get a bank info from the form
            Payments pay = Form.form(Payments.class).bindFromRequest().get();
            //bank.buyer = session.get("uuid");
            // Try to add the info to the DB
            PaymentOperations payop = new PaymentOperations();

            if(payop.save(pay)){
                return ok(account_payment.render("Your payment information has been stored",session));
            }else {
                // if adding failed, redirect to the addproduct page
                return ok(account_payment.render("Information not stored", session));
            }
        }else {

            return ok(uploaded.render("You need a buyer account", session));
        }
    }
    public static Result update(){
        Http.Session session = Util.getCurrentSession();

        String userid = session.get("uuid");
        System.out.println("user id is: "+ userid);
        if (userid == null) {
            return ok(login.render("please enter login id", session));
        }
        if( userid.equals("1")){return ok(login.render("Please login as admin or seller!", session));}

        if (userid != null){
            PaymentOperations payop = new PaymentOperations();
            ArrayList<Payments> list = new ArrayList<>();//payop.updatestatus();
            if(list.size() > 0){
                return ok(account_payment.render("Your payment status is", session));
            }else {
                return ok(account_payment.render("No Information.", session));
            }
        }
        else {
            ArrayList<Payments> list = new ArrayList<>();
            return ok(account_payment.render("No bank Info found", session));
        }
    }


    public static Result delete(){
        Http.Session session = Util.getCurrentSession();
        //DynamicForm requestData = Form.form().bindFromRequest();
        String userid = session.get("uuid");
        System.out.println(" user id is: "+ userid);
        if (userid== null){return ok(login.render("Please login first!", session));}
        if(userid.equals("1")) {
            return ok(login.render("please login as seller or admin", session));
        }
        if (userid!= null){
            PaymentOperations payop = new PaymentOperations();
            payop.delete(userid);
            ArrayList<Payments> list = payop.getall();
            if(list.size() > 0){
                return ok(account_payment.render("Now status is ",session));
            }else {
                return ok(account_payment.render("Operation failed", session));
            }
        }
        else {
            ArrayList<Payments> list = new ArrayList<>();
            return ok(account_payment.render("No info found", session));
        }
    }
}
