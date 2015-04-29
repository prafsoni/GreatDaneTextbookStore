package models;

import org.bson.types.ObjectId;
import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Banks {
    public ObjectId id;
    public String bankname;
}
