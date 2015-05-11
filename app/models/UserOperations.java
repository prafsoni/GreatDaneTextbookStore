package models;

import com.mongodb.BasicDBObject;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
//import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
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
            ArrayList<String> al = new ArrayList<>();
            user.address = al;
            Document doc = new Document("fname", user.fname)
                    .append("lname", user.lname)
                    .append("uname", user.uname)
                    .append("email", user.email)
                    .append("password", user.password)
                    .append("mob", user.mob)
                    .append("address",user.address)
                    .append("cdate", user.cdate)
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
    public String getUserHexId(String uname){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("uname", uname)).first();
        return result.getObjectId("_id").toHexString();
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
        if(uname == null){return user;}
        user.cdate = result.getDate("cdate");
        user.email = result.getString("email");
        user.fname = result.getString("fname");
        user.lname = result.getString("lname");
        user.mob = result.getLong("mob");
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
        ObjectId uid = new ObjectId(id);
        Document result = collection.find(eq("_id", uid)).first();
        Users user = new Users();
        user.cdate = result.getDate("cdate");
        user.email = result.getString("email");
        user.fname = result.getString("fname");
        user.lname = result.getString("lname");
        user.mob = result.getLong("mob");
        user.status = result.getInteger("status");
        user.id = result.getObjectId("_id");
        user.role = result.get("role", a.class);
        user.address = result.get("role",a.class);
        user.uname = result.getString("uname");
        return user;
    }

    public static int getaccstatus(String uname){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("uname", uname)).first();

        //return result.getInteger("status");
        /*
         * Joe: I changed the return values as the same with the checkuserapss method.
         * Otherwise it doesn't for me.
         */
        if (result == null){
            return -1;
        }
        else {return 1;}
    }

    public ArrayList<Users> getall(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        FindIterable<Document> results = collection.find();
        ArrayList<Users> list = new ArrayList<>();
        for(Document result : results){
            Users user = new Users();
            user.cdate = result.getDate("cdate");
            user.email = result.getString("email");
            user.fname = result.getString("fname");
            user.lname = result.getString("lname");
            user.mob = result.getLong("mob");
            user.status = result.getInteger("status");
            user.id = result.getObjectId("_id");
            user.role = result.get("role", a.class);
            user.address = result.get("role",a.class);
            user.uname = result.getString("uname");
            list.add(user);
        }
        return list;
    }

    public ArrayList<Users> getallsellers(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        FindIterable<Document> results = collection.find();
        ArrayList<Users> list = new ArrayList<>();
        for(Document result : results){
            Users user = new Users();
            user.cdate = result.getDate("cdate");
            user.email = result.getString("email");
            user.fname = result.getString("fname");
            user.lname = result.getString("lname");
            user.mob = result.getLong("mob");
            user.status = result.getInteger("status");
            user.id = result.getObjectId("_id");
            user.role = result.get("role", a.class);
            user.address = result.get("address", a.class);
            user.uname = result.getString("uname");
            if(user.role.contains("seller")){
                list.add(user);
            }
        }
        return list;
    }

    public String getuname(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Users user = getuserbyid(id);
        //Document result = collection.find(eq("_id", id)).first();
        return user.id.toHexString();
    }

    public boolean update(Users user){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document doc = collection.find(eq("_id",user.id)).first();
        try {
            Document doc1 = new Document("fname", user.fname)
                    .append("lname", user.lname)
                    .append("uname", user.uname)
                    .append("email", user.email)
                    .append("password", user.password)
                    .append("mob", user.mob)
                    .append("cdate", user.cdate)
                    .append("role", user.role)
                    .append("status", user.status);
            collection.updateOne(doc,doc1);
                    return true;
        }catch(Exception ex){ // TODO catch proper exceptions
            System.out.println(ex);
            return false;
        }
    }

    public boolean approve(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        ObjectId uid = new ObjectId(userid);
        Document doc = collection.find(eq("_id",uid)).first();
        Document newDoc = new Document(doc);
        newDoc.put("status",0);
        newDoc.remove("_id");
        collection.updateOne(doc, newDoc);
        return true;
    }

    public boolean disapprove(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        ObjectId uid = new ObjectId(userid);
        Document doc = collection.find(eq("_id",uid)).first();
        Document newDoc = new Document(doc);
        newDoc.put("status",-1);
        newDoc.remove("_id");
        collection.updateOne(doc,newDoc);
        return true;
    }

    public void address(String addressid, String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        ObjectId uid = new ObjectId(userid);
        Document doc = collection.find(eq("_id",uid)).first();
        ArrayList<String> al = new ArrayList<>();
        al = doc.get("address", a.class);
        al.add(addressid);
        Document ndoc = new Document();
        ndoc = doc;
        ndoc.put("address",al);
        ndoc.remove("_id");
        collection.updateOne(doc,ndoc);
    }

    public void delete(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        ObjectId uid = new ObjectId(userid);
        Document doc = collection.find(eq("_id",uid)).first();
        collection.deleteOne(doc);
    }
}
