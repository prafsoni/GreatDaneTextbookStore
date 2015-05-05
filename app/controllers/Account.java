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
import play.mvc.Http.Session;
import play.mvc.Result;
import play.cache.Cache;
import play.mvc.Http.Session;
import views.html.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PKS on 4/22/15.
 */
public class Account extends Controller {

    public static Result register(){
        Session session = Util.getCurrentSession();
        System.out.println("Current user: " + session.get("username"));
        return ok(register.render("Register",session));
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
        Session session = Util.getCurrentSession();
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

        if (UserOperations.createuser(user)) {
            // When successful, redirect user to the account page
            return ok(account.render("Please login using the account just created.", session));
        }else {
            // When unsuccessful, refresh the page and clear the form
            return ok(register.render("Creating account failed!",session));
        }
    }

    public static Result login(){
        //String username = Util.getFromUserCache("uuid", "username");
        Session session = Util.getCurrentSession();
        String username = session.get("username");

        if (username != null) {
            // if there is already username in session
            System.out.println("Current user: " + username);
            return ok(account.render("Welcome back!", session));
        }
        // else return a login box in account page
        return unauthorized(account.render("Please login first!", session));
    }

    public static Result dologin(){
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
        Session session = Util.getCurrentSession();
        if(uname.length()<=0){
            return unauthorized(account.render("Please enter username!",session));
        }
        else if(pwd.length()<=0){
            return unauthorized(account.render("Please enter password!",session));
        }

        // Test if the username/pass were correct, if so
        // set the session username to the entered name and redirect to the homepage
        if (UserOperations.checkuserpass(uname, pwd)) {
            //System.out.println("Logging in user: " + uname);
            if (UserOperations.getaccstatus(uname) != -1) {
                System.out.println("Logging in user: " + uname);

                String uuid = session("uuid");
                if (uuid == null) {
                    uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
                    Util.insertIntoSession("uuid", uuid);

                }
                user = uo.getuserbyuname(uname);
                Cache.set(uuid + "username", uname);
                session = Util.setUserToSession(user);

                if (session.get("role").equals("3")){
                    return ok(views.html.adminindex.render("Welcome back!", session));
                }
                System.out.println("******************" + session.toString());
                return ok(account.render("Welcome back!",session));
            } else {
                // check failed, let them try again
                request().setUsername("");
                return unauthorized(account.render("Login failed! Please try again.",session));
            }
        }
        else {
            return unauthorized(account.render("Account not verified. Please register first!", session));
        }
    }

    public static  Result upload() {
        return ok(uploadpic.render("Upload your Profile Pic"));
    }

    public static Result doupload(){
        if(session().get("uuid")!=null) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart picture = body.getFile("picture");
            if (picture != null) {
                String fileName = picture.getFilename();
                String contentType = picture.getContentType();

                try {
                    String current = new java.io.File(".").getCanonicalPath();
                    System.out.println("Current dir:" + current);
                    File file = new File(picture.getFile(), session().get("uuid") + ".jpg");
                    byte[] data = Files.readAllBytes(picture.getFile().toPath());
                    FileOutputStream fos = new FileOutputStream(current + "/public/images/profilepics/" + session().get("uuid") + ".jpg");
                    fos.write(data);
                } catch (Exception e) {
                    System.out.print(e);
                }
                return ok("File uploaded");
            } else {
                flash("error", "Missing file");
                return redirect("/");
            }
        }else {
            return unauthorized(uploadpic.render("you need to login first"));
        }
    }


}
