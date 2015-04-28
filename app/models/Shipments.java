package models;

import javax.persistence.Entity;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Shipments {
    public String shippmentid;
    public String stype;
    public String trackingid;
    public String orderid;
    public String status;
}
