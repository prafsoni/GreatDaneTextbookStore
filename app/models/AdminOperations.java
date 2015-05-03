package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import play.db.ebean.Model;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by PKS on 5/3/15.
 */
public class AdminOperations extends Model {
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

    public boolean approveuser(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Users");
        Document result = collection.find(eq("_id", userid)).first();
        Document r = result;
        r.put("status",0);
        UpdateResult re = collection.replaceOne(result,r);
        if(re.wasAcknowledged()){
            return true;
        }
        else {
            return false;
        }
    }
}
