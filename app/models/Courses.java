package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by PKS on 4/27/15.
 */
@Entity
public class Courses {
    public String id;
    public String cname;
    public List<String> departments;
}
