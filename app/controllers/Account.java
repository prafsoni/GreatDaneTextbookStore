package controllers;

import models.UserOperations;
import models.Users;
import org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;
import views.html.register;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PKS on 4/22/15.
 */
public class Account extends Controller {

    //private UserOperations useroperations = new UserOperations();

    public static Result register(){
        return ok(register.render("SignUp"));
    }

    public static Result doregister(){
        DynamicForm requestData = Form.form().bindFromRequest();
        Users user = Form.form(Users.class).bindFromRequest().get();
        Date date = new Date();
        user.cdate = date;
        ArrayList<String> role = new ArrayList<>();
        if(requestData.get("utype").matches("1")){
            role.add("user");
        }else if(requestData.get("utype").matches("2")){
            role.add("user");
            role.add("seller");
        }else if(requestData.get("utype").matches("3")){
            role.add("user");
            role.add("seller");
            role.add("admin");
        }
        user.role = role;
        UserOperations useroperations = new UserOperations();
        Boolean result = useroperations.createuser(user);
        if(result){
            return redirect("/");
        }else {
            return redirect("/register");
        }
    }

    public static Result login(){
        return ok(login.render("Login"));
    }

    public static Result dologin(){
        DynamicForm requestData = Form.form().bindFromRequest();
        if(requestData.get("login")!=null) {
            String uname = requestData.get("uname");
            String pwd = requestData.get("pwd");
            UserOperations useroperations = new UserOperations();
            Boolean result = useroperations.checkuserpass(uname, pwd);
            if (result) {
                request().setUsername(uname);
                return redirect("/");
            } else {
                return redirect("/login");
            }
        }else if(requestData.get("register")!=null){
            return redirect("/register");
        }else {
            return redirect("/");
        }
    }
}
