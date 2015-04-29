package models;

import org.bson.types.ObjectId;

import javax.persistence.Entity;

/**
 * Created by PKS on 4/22/15.
 */
@Entity
public class Books {
    public ObjectId id;
    public String title;
    public String isbn;
    public String authors;
    public int stock;
    public float price;
    public int year;
    public int edition;
    public String seller;
    public String picid;
    //public String description;

    public Books(){}
}
