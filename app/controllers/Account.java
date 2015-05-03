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

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PKS on 4/22/15.
 */
public class Account extends Controller {

    //private UserOperations useroperations = new UserOperations();

    public static Result register(){
        String username = Util.getFromUserCache("uuid", "username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        return ok(register.render("",user));
    }

    /**
     * Semantic helper function to get the current server time
     * @return A new Date object to represent the time
     */
    private static Date now()
    {
        return new Date();
    }

    public static Result doregister(){
        // create form object to represent the data from the
        // submitted form.
        DynamicForm requestData = Form.form().bindFromRequest();

        // Create a User instance from the form data
        Users user = Form.form(Users.class).bindFromRequest().get();

        // Set the User instance cData (creation date) to now
        user.cdate = now();

        // Decide how to assign a role (or multiple roles) to the
        // created user.
        ArrayList<String> role = new ArrayList<>();

        // String switch-case with fallthrough.
        // 3 will let the user have all roles
        // 2 will let the user have seller and user roles
        // 1 will let the user have only user roles
        //
        // Throw an exception if some role is called that's unhandled
        switch(requestData.get("utype"))
        {
            case "3":
                role.add("admin");
                // fall through
            case "2":
                role.add("seller");
                // fall through
            case "1":
                role.add("user");
                break;
            default:
                throw new RuntimeException("Unsupported role chosen");
        }

        // Assigne the above list of roles to the user
        user.role = role;
        user.status = -1;
        // I can use the static function directly

        if (UserOperations.createuser(user))
        {
            // When successful, redirect user to the homepage
            return ok(login.render("Please login your account.", user));
        }else {
            // When unsuccessful, refresh the page and clear the form
            return ok(register.render("Creating account failed!",user));
        }
    }

    public static Result login(){
        //String username = Util.getFromUserCache("uuid", "username");
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        Users user = new Users();
        UserOperations uo = new UserOperations();
        user = uo.getuserbyuname(username);
        if (username != null) {
            System.out.println("Current user: " + username);
            return ok(account.render("Welcome back!", user));
        }

        return unauthorized(account.render("Please login first!", user));
    }

    public static Result dologin()
    {
        // Get the submitted form from the user
        DynamicForm requestData = Form.form().bindFromRequest();

        // print
        //System.out.println(requestData.data());

        // Error case, should not get here. That means the POST'ed data isnt
        // what we expect
        if (requestData.get("login") == null)
            throw new RuntimeException("Unhandled login form POST");

        // Get the name string from the `Username` text field.
        String uname = requestData.get("uname");
        // Get the password string from the `password` text field.
        String pwd = requestData.get("password");
        Users user = new Users();
        UserOperations uo = new UserOperations();

        if(uname.length()<=0){
            return unauthorized(account.render("Please enter username!",user));
        }
        else if(pwd.length()<=0){
            return unauthorized(account.render("Please enter password!",user));
        }

        // Test if the username/pass were correct, if so
        // set the session username to the entered name and redirect to the homepage
        if (UserOperations.checkuserpass(uname, pwd)) {
            //System.out.println("Logging in user: " + uname);
            if (UserOperations.getaccstatus(uname) != -1) {
                System.out.println("Logging in user: " + uname);

                String uuid = session("uuid");

                if (uuid == null) {
                    uuid = java.util.UUID.randomUUID().toString();
                    session("uuid", uuid);
                }
                user = uo.getuserbyuname(uname);
                Cache.set(uuid + "username", uname);
                Util.insertIntoSession("username", uname);
                //System.out.println(user.uname);
                return ok(account.render("Welcome back!",user));
            } else {
                // check failed, let them try again
                request().setUsername("");
                return unauthorized(account.render("Login failed! Please try again.",user));
            }
        }
        else {
            return unauthorized(account.render("Account not verified. Please register first!",user));
        }
    }


}
