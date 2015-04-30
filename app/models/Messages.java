package models;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by PKS on 4/30/15.
 */
@Entity
public class Messages {
    public Object id;
    public String userid;
    public String sellerid;
    public String subject;
    public String msg;
    public Date sdate;
}
