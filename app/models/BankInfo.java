package models;

import org.bson.types.ObjectId;
import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class BankInfo {
    public String bankid;
    public long routing;
    public long acc;
    public String acctype;
    public int flow;
    public ObjectId id;
    public String userid;
    public String billaddr;
}
