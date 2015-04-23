package models;

import org.bson.types.ObjectId;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by PKS on 4/22/15.
 */

@Entity
public class Users {
    public ObjectId id;
    public String fname;
    public String lname;
    public String uname;
    public String password;
    public String email;
    public String mob;
    public String[] address;
    public String role;
    public int status;
    public Date cdate;
    public String picid;

    public Users(){}

}
