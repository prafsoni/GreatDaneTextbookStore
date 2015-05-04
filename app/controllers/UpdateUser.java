package controllers;

import models.BookOperations;
import models.Books;
import models.UserOperations;
import models.Users;
import org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.cache.Cache;
import play.mvc.Http.Session;
import views.html.*;
import java.io.File;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Joe on 5/4/2015.
 */
public class UpdateUser extends Controller{
    public static Result update(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        if (username.length()==0){
            return unauthorized(login.render("Please login first!", user));
        }else if(user.role.size()==3){
            return ok(views.html.adminindex.render("Update",user));
        }else{
            return ok(update.render("Update",user));
        }
    }
    public static Result doupdate(){
        //DynamicForm requestData = Form.form().bindFromRequest();
        //Users user = Form.form(Users.class).bindFromRequest().get();
        //TODO need to implement doupdate method.



        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);


        return ok(account.render("Update Accepted",user));
    }
}
