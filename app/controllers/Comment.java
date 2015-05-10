package controllers;

/**
 * Created by Joe on 5/10/2015.
 */


import models.UserOperations;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;
import views.html.*;
import java.util.ArrayList;
import java.util.Date;


public class Comment extends Controller{
    private static Date now() {return new Date();}

    public static Result getComments(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String uuid = session.get("uuid");
        String bookid = Form.form().bindFromRequest().get("bookid");
        ArrayList<String> clist = new ArrayList<>();
        if (username == null){
            return ok(login.render("Please login first!", session));
        }
        return ok(comments.render("Comments",clist,session));
    }
    public static Result add(){
        Http.Session session = Util.getCurrentSession();
        String username = session.get("username");
        String uuid = session.get("uuid");
        String bookid = Form.form().bindFromRequest().get("bookid");
        if (username == null){
            return ok(login.render("Please login first!", session));
        }
        return ok(addComment.render("Comments",bookid, session));
    }
}
