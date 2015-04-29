package controllers;

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
        return ok(contact.render("contact"));
    }
    //public static Result docontact(){

    //}
}
