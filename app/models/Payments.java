package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Payments {
    public String id;
    public String orderid;
    public double amount;
    public String status;
    public Date pdate;
    public String bankid;
}
