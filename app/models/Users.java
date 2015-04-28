package models;

import org.bson.types.ObjectId;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PKS on 4/22/15.
 */

@Entity
public class Users {
    public ObjectId id;
    @Constraints.Required
    public String fname;
    public String lname;
    public String uname;
    public String password;
    public String email;
    public long mob;
    public ArrayList<String> address;
    public ArrayList<String> role;
    public int status;
    public Date cdate;
    public String picid;

    public Users(){}

}
