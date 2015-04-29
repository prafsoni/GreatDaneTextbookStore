package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import play.db.ebean.Model;

/**
 * Created by PKS on 4/28/15.
 */
public class CourseOperations extends Model {
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
}
