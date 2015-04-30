package models;

import org.bson.types.ObjectId;
import play.db.ebean.Model;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Departments {
    public ObjectId id;
    public String dname;
}
