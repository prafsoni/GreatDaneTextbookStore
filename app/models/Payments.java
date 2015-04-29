package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Payments {
    public String paymentid;
    public String orderid;
    public float amount;
    public String status;
    public Date pdate;
    public String bankid;
}
