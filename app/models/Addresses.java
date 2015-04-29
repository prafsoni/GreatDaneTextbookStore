package models;

import org.bson.types.ObjectId;
import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Addresses {
    public ObjectId id;
    public String addr;
    public String state;
    public String addrtype;
    public int zip;
    public String userid;
}
