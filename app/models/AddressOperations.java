package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by PKS on 4/28/15.
 */
public class AddressOperations extends Model{
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

    public boolean save(Addresses addr){
        try{
            Document doc = new Document("addr", addr.addr)
                .append("addrtype",addr.addrtype)
                .append("state",addr.state)
                .append("userid",addr.userid)
                .append("zip",addr.zip);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Addresses");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
        return false;
        }
    }

    public void delete(String addrid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Addresses");
        Document result = collection.find(eq("_id",addrid)).first();
        collection.deleteOne(result);
    }

    public void Update(Addresses addr){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Addresses");
        Document doc = new Document("addr", addr.addr)
                .append("addrtype",addr.addrtype)
                .append("state",addr.state)
                .append("userid", addr.userid)
                .append("zip", addr.zip);
        collection.updateOne(eq("_id", addr.id), doc);
    }
    public ArrayList<Addresses> getall(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Addresses");
        FindIterable<Document> results = collection.find(eq("userid", userid));
        ArrayList<Addresses> list = new ArrayList<>();
        for(Document doc : results){
            Addresses addr = new Addresses();
            addr.addr = doc.getString("addr");
            addr.addrtype = doc.getString("addrtype");
            addr.id = doc.getObjectId("_id");
            addr.userid = doc.getString("userid");
            addr.state = doc.getString("state");
            addr.zip = doc.getInteger("zip");
            list.add(addr);
        }
        return list;
    }

    public ArrayList<Addresses> getallshipping(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Addresses");
        FindIterable<Document> results = collection.find(and(eq("userid", userid), eq("addrtype", "shipping")));
        ArrayList<Addresses> list = new ArrayList<>();
        for(Document doc : results){
            Addresses addr = new Addresses();
            addr.addr = doc.getString("addr");
            addr.addrtype = doc.getString("addrtype");
            addr.id = doc.getObjectId("_id");
            addr.userid = doc.getString("userid");
            addr.state = doc.getString("state");
            addr.zip = doc.getInteger("zip");
            list.add(addr);
        }
        return list;
    }

    public ArrayList<Addresses> getallbilling(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Addresses");
        FindIterable<Document> results = collection.find(and(eq("userid", userid), eq("addrtype", "billing")));
        ArrayList<Addresses> list = new ArrayList<>();
        for(Document doc : results){
            Addresses addr = new Addresses();
            addr.addr = doc.getString("addr");
            addr.addrtype = doc.getString("addrtype");
            addr.id = doc.getObjectId("_id");
            addr.userid = doc.getString("userid");
            addr.state = doc.getString("state");
            addr.zip = doc.getInteger("zip");
            list.add(addr);
        }
        return list;
    }

    public Addresses getone(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Addresses");
        Document result = collection.find(eq("_id",id)).first();
        Addresses addr = new Addresses();
        addr.addr = result.getString("addr");
        addr.addrtype = result.getString("addrtype");
        addr.id = result.getObjectId("_id");
        addr.userid = result.getString("userid");
        addr.state = result.getString("state");
        addr.zip = result.getInteger("zip");
        return addr;
    }
}
