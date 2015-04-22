package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;
import play.db.ebean.Model;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by PKS on 4/22/15.
 */
public class UserOperations extends Model {

    private MongoDatabase getdatabase(){
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("BookStore");
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
            MongoDatabase database = getdatabase();
            MongoCollection<Document> collection = database.getCollection("Users");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}
