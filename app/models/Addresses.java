package models;

import javax.persistence.Entity;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Addresses {
    public String id;
    public String addr;
    public String stat;
    public String addrtype;
    public int zip;
    public String userid;
}
