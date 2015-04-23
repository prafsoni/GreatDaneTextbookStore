package models;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
//import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import play.db.ebean.Model;
import static com.mongodb.client.model.Filters.*;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by PKS on 4/22/15.
 */
public class UserOperations extends Model {

    private com.mongodb.async.client.MongoDatabase getdatabaseasync(){
        MongoClient mongoClient = MongoClients.create();
        com.mongodb.async.client.MongoDatabase mongoDatabase = mongoClient.getDatabase("BookStore");
        return mongoDatabase;
    }

    private com.mongodb.client.MongoDatabase getdatabase(){
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient();
        com.mongodb.client.MongoDatabase mongoDatabase = mongoClient.getDatabase("BookStore");
        return mongoDatabase;
    }

    public Boolean createuser(Users user ){
        try {
            //user.address[0] = "";
            //user.role[0]= "user";
            Document doc = new Document("fname", user.fname)
                    .append("lname", user.lname)
                    .append("uname", user.uname)
                    .append("email", user.email)
                    .append("password", user.password)
                    .append("mob", user.mob)
                    //.append("address", Arrays.asList(user.address))
                    .append("cDate", user.cdate)
                    //.append("role", Arrays.asList(user.role))
                    .append("status", user.status);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Users");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public Boolean checkuserpass(String uname, String pwd){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");

        Document result = collection.find(and(eq("uname", uname), eq("password", pwd))).first();
        if(result!=null){
            return true;
        }
        else {
            return false;
        }
    }
}
