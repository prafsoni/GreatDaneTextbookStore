package models;

import com.mongodb.BasicDBObject;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
//import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.db.ebean.Model;
import static com.mongodb.client.model.Filters.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by PKS on 4/22/15.
 */
public class UserOperations extends Model {

    private static com.mongodb.async.client.MongoDatabase getdatabaseasync(){
        MongoClient mongoClient = MongoClients.create();
        com.mongodb.async.client.MongoDatabase mongoDatabase = mongoClient.getDatabase("BookStore");
        return mongoDatabase;
    }

    private static com.mongodb.client.MongoDatabase getdatabase(){
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient();
        com.mongodb.client.MongoDatabase mongoDatabase = mongoClient.getDatabase("BookStore");
        return mongoDatabase;
    }
    class a extends ArrayList<String>{}
    /**
     * Creates a user in the DB from a User instance
     * @param user The User instance used to construct a user document in the DB.
     *             This instance must have a valid field for: lname,fname,uname,email,password,mod,cDate,
     *             role, and status
     * @return True if the user was inserted, False otherwise.
     */
    public static Boolean createuser(Users user ){
        try {
            Document doc = new Document("fname", user.fname)
                    .append("lname", user.lname)
                    .append("uname", user.uname)
                    .append("email", user.email)
                    .append("password", user.password)
                    .append("mob", user.mob)
                    //.append("address", Arrays.asList(user.address))
                    .append("cDate", user.cdate)
                    .append("role", user.role)
                    .append("status", user.status);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Users");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){ // TODO catch proper exceptions
            System.out.println(ex);
            return false;
        }
    }

    public static Boolean checkuserpass(String uname, String pwd){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");

        System.out.println("Checking user: " + uname + " with entered pass: " + pwd);

        Document result = collection.find(and(eq("uname", uname), eq("password", pwd))).first();

        return result == null ? false : true;
    }
    //this  method will check availability of username and will return true or false accordingly
    public boolean checkunameavailability(String uname){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("uname", uname)).first();
        if(result==null){
            return true;
        }
        else {
            return false;
        }
    }
    // this method will return objectid of user as a string
    public String getuserid(String uname){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("uname", uname)).first();
        return result.getObjectId("_id").toString();
    }

    //for changing the password for user
    public boolean changepasswd(String userid, String password){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("_id", userid)).first();
        Document r = result;
        r.put("password",password);
        UpdateResult re = collection.replaceOne(result,r);
        if(re.wasAcknowledged()){
            return true;
        }
        else {
            return false;
        }
    }
    // to get user by username
    public Users getuserbyuname(String uname){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("uname", uname)).first();
        Users user = new Users();
        user.cdate = result.getDate("cdate");
        user.email = result.getString("email");
        user.fname = result.getString("fname");
        user.lname = result.getString("lname");
        user.mob = result.getInteger("mob");
        user.picid = result.getString("picid");
        user.status = result.getInteger("status");
        user.id = result.getObjectId("_id");
        user.role = result.get("role", a.class);
        user.address = result.get("role",a.class);
        user.uname = result.getString("uname");
        return user;
    }

    // to get user by userid
    public Users getuserbyid(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("_id", id)).first();
        Users user = new Users();
        user.cdate = result.getDate("cdate");
        user.email = result.getString("email");
        user.fname = result.getString("fname");
        user.lname = result.getString("lname");
        user.mob = result.getInteger("mob");
        user.picid = result.getString("picid");
        user.status = result.getInteger("status");
        user.id = result.getObjectId("_id");
        user.role = result.get("role", a.class);
        user.address = result.get("role",a.class);
        user.uname = result.getString("uname");
        return user;
    }
}
