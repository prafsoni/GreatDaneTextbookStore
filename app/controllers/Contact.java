package controllers;

import models.UserOperations;
import models.Users;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.contact;
/**
 * Created by Joe on 4/29/2015.
 */
public class Contact extends Controller{
    public static Result contact(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        return ok(contact.render("contact",user));
    }
    //public static Result docontact(){

    //}
}
