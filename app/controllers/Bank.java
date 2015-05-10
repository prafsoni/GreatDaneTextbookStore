package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;

/**
 * Created by Rahul Srivastava on 5/5/15.
 * */
public class Bank extends Controller {

    public static Result getBank(){
        Http.Session session = Util.getCurrentSession();
        String userid = session.get("uuid");
        System.out.println("buyer id is: "+ userid);

        if (userid != null){
            BankInfoOperations bio = new BankInfoOperations();
            ArrayList<BankInfo> list = bio.getall(userid);
            if(list.size() > 0){
                return ok(account_bank.render("Your Bank Information", list, session));
            }
            else {
                return ok(account_bank.render("You have not added any bank information.", list, session));
            }
        }
        else {
            return ok(login.render("Please login first!", session));
        }
    }
    public static Result add(){
        Http.Session session = Util.getCurrentSession();
        String userid = session.get("uuid");
        System.out.println("buyer id is: "+ userid);

        if (userid != null){
            return ok(account_addbank.render("Add a bank account", session()));
        }
        else{
            return ok(login.render("Please login first!", session));
        }

    }

    public static Result doAdd(){
        Http.Session session = Util.getCurrentSession();
        String userid = session.get("uuid");
        String username = session.get("username");
        String role = session.get("role");
        //check if user is a buyer
        if(username != null)
        {
            // Get a bank info from the form
            BankInfo bank;
            try{
                bank = Form.form(BankInfo.class).bindFromRequest().get();
            }catch(Exception ex){
                System.out.println(ex);
                return ok(account_addbank.render("Please fill all fields!", session));
            }


            bank.userid = session("uuid");
            // Try to add the info to the DB
            BankInfoOperations bankinfoperations = new BankInfoOperations();

            if(bankinfoperations.save(bank)){
                ArrayList<BankInfo> list = bankinfoperations.getall(userid);
                if(list.size() > 0) {
                    return ok(account_bank.render("Your bank information has been stored!", list, session));
                }else{return ok(uploaded.render("Your bank information has been stored!", session));}
            }else {return ok(uploaded.render("Information not stored!", session));}
        }else {return unauthorized(uploaded.render("You must have a Buyer or Seller account!", session));}
    }

    public static Result update(){
        Http.Session session = Util.getCurrentSession();
        String bankid = Form.form().bindFromRequest().get("bankid");
        String userid = session.get("uuid");
        System.out.println("bankid is: "+ bankid);
        if (userid!= null){
            BankInfoOperations bio = new BankInfoOperations();
            ArrayList<BankInfo> list = bio.getall(userid);
            BankInfo bank = bio.getone(bankid);
            if(bankid != null){
                return ok(account_updatebank.render("Update Bank Account", bank, session()));
            }else if(list.size() > 0){
                return ok(account_bank.render("You have not added any bank information yet", list, session));
            }else{
                return ok(account_bank.render("You have not added any bank information yet.", list, session));
            }
        }
        else {
            return ok(login.render("Please login first!", session));
        }

    }

    public static Result doupdate(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        UserOperations uo = new UserOperations();
        BankInfoOperations bankinfoperations = new BankInfoOperations();
        if(username != null){
            // Get a bank info from the form
            String bankid;
            try{bankid = Form.form().bindFromRequest().get("bankid");
            }catch(Exception ex){
                return ok(uploaded.render("System error occurred. Please try again!",session));
            }
            BankInfo bank;
            try{
                bank = Form.form(BankInfo.class).bindFromRequest().get();
            }catch(Exception ex){
                System.out.println(ex);
                bank = bankinfoperations.getone(bankid);
                return ok(account_updatebank.render("Please fill all fields!", bank, session));
            }

            System.out.println(bank.bankid + " " + bank.bname);
            String uuid = session("uuid");
            if (uuid == null) {
                uuid = uo.getuserid(username); //java.util.UUID.randomUUID().toString();
                Util.insertIntoSession("uuid", uuid);
            }
            bank.userid = uuid;
            // Try to add the info to the DB

            if(bankinfoperations.update(bank)){
                return ok(uploaded.render("Your bank information has been updated!", session));
            }else {
                return ok(uploaded.render("Information not stored!", session));
            }
        }
        else {
            return unauthorized(uploaded.render("You must have a Buyer or Seller account!", session));
        }
    }


    public static Result delete(){
        Http.Session session = Util.getCurrentSession();
        String bankid = Form.form().bindFromRequest().get("bankid");
        String userid = session.get("uuid");
        System.out.println("bankid is: "+ bankid);

        if (userid!= null){
            BankInfoOperations bio = new BankInfoOperations();
            bio.delete(bankid);
            ArrayList<BankInfo> list = bio.getall(userid);
            if(list.size() > 0){
                return ok(account_bank.render("Your Bank Information", list, session));
            }else {
                return ok(account_bank.render("You have not added any bank information yet", list, session));
            }
        }
        else {
            return ok(login.render("Please login first!", session));
        }
    }
}
