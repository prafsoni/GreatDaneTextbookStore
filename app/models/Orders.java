package models;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Orders {
    public ArrayList<String> Bookids;
    public String orderid;
    public int quantity;
    public float price;
    public String status;
    public String Shippingid;
    public Date orderdate;
}
