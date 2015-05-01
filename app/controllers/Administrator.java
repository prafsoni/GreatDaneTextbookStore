package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.adminindex;

/**
 * Created by PKS on 5/1/15.
 */
public class Administrator extends Controller {
    public static Result index(){
        return ok(adminindex.render("Administrator"));
    }
}
