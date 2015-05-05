package controllers;

import models.UserOperations;
import models.Users;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.contact;
/**
 * Created by Joe on 4/29/2015.
 */
public class Contact extends Controller{
    public static Result contact(){
        //String username = Util.getFromUserCache("uuid", "username");
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");

        return ok(contact.render("contact",session));
    }
    //public static Result docontact(){

    //}
}
