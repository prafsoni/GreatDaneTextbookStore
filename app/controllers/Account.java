package controllers;

import models.UserOperations;
import models.Users;
import org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by PKS on 4/22/15.
 */
public class Account extends Controller {

    //private UserOperations useroperations = new UserOperations();

    public static Result register(){
        return ok(register.render("SignUp"));
    }

    public static Result createuser(){
        DynamicForm requestData = Form.form().bindFromRequest();
        Users user = new Users();
        user.fname = requestData.get("frstName");
        user.lname = requestData.get("lastName");
        user.uname = requestData.get("userName");
        user.email = requestData.get("email");
        user.mob = requestData.get("mob");
        user.password = requestData.get("pwd");
        user.cdate = null;
        UserOperations useroperations = new UserOperations();
        Boolean result = useroperations.createuser(user);
        if(result){
            return redirect("/");
        }else {
            return badRequest();
        }
    }
}
