package models;

import org.bson.types.ObjectId;
import play.db.ebean.Model;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Orders {
    public HashMap<String,Integer> Banks;
    public ObjectId id;
    public int quantity;
    public double price;
    public String status;
    public String Shippingid;
    public Date orderdate;
    public String userid;
    public String sellerid;
}
