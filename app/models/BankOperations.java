package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import play.db.ebean.Model;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by PKS on 4/28/15.
 */
public class BankOperations extends Model {
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

    public boolean save(Banks banks){
        try{
            Document doc = new Document("bankname",banks.bankname);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Banks");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Banks");
        Document result = collection.find(eq("_id",id)).first();
        collection.deleteOne(result);
    }

    public void update(Banks banks){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Banks");
        Document doc = new Document("_id",banks.id)
                .append("bankname", banks.bankname);
        collection.updateOne(eq("_id", banks.id), doc);
    }

    public ArrayList<Banks> getall(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Banks");
        FindIterable<Document> results = collection.find();
        ArrayList<Banks> list = new ArrayList<>();
        for(Document result : results){
            Banks banks = new Banks();
            banks.id = result.getObjectId("_id");
            banks.bankname = result.getString("bankname");
            list.add(banks);
        }
        return list;
    }

    private String getbankname(String bankid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Banks");
        Document result = collection.find(eq("_id",bankid)).first();
        return result.getString("bankname");
    }
}
