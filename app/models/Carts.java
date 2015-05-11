package models;

import controllers.Util;
import org.bson.types.ObjectId;
import play.data.Form;
import play.mvc.Http;
import views.html.login;
import javax.persistence.Entity;
import java.util.ArrayList;


/**
 * Created by PKS on 4/22/15.
 */
@Entity
public class Carts {
    public String buyer;
    public int number;
    public double tax;
    public double subtotal;
    public double total;
    public double shippingfee;
    public ArrayList<Books> list;

    public Carts(){
        Http.Session session = Util.getCurrentSession();
        String uname = session.get("username");
        UserOperations uo = new UserOperations();
        String uuid = session.get("uuid");
        if (uuid == null) {
            uuid = uo.getuserid(uname); //java.util.UUID.randomUUID().toString();
            Util.insertIntoSession("uuid", uuid);
        }
        this.buyer = uuid;
        this.number = 0;
        this.tax = 0;
        this.subtotal = 0;
        this.total = 0;
        this.shippingfee = 0;
        this.list = new ArrayList<>();
    }
    public void update(){
        this.number = 0;
        this.tax = 0;
        this.subtotal = 0;
        this.total = 0;
        this.shippingfee = 0;
        for (Books book: list){
            this.tax = this.tax + book.price * 0.08 * book.stock;
            this.subtotal = this.subtotal + book.price * book.stock;
            this.shippingfee = this.shippingfee + book.shippingfee;
            this.number = this.number + book.stock;
        }
        if (this.subtotal > 100){
            this.shippingfee = 0;
        }
        this.total = this.subtotal + this.tax + this.shippingfee;
    }
    public void clear(){
        this.number = 0;
        this.subtotal = 0;
        this.total = 0;
        this.shippingfee = 0;
        list.clear();
    }
    public ArrayList<String> getIdList(){
        ArrayList<String> idList = new ArrayList<>();
        for (Books book: this.list){
            idList.add(book.id.toHexString());
        }
        return idList;
    }
}
