package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Departments {
    public String id;
    public String dname;
    public List<String> courseids;
}
