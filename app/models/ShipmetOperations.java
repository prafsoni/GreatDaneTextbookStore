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
public class ShipmetOperations extends Model {
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

    public boolean save(Shipments shipment){
        try {
            Document doc = new Document("orderid", shipment.orderid)
                    .append("stype", shipment.stype)
                    .append("trackingid", shipment.trackingid)
                    .append("status", shipment.status);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Shipments");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){ // TODO catch proper exceptions
            System.out.println(ex);
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Shipments");
        Document doc = collection.find(eq("_id",id)).first();
        collection.deleteOne(doc);
    }

    public void updatestatus(String id, String status){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Shipments");
        Document doc = collection.find(eq("_id",id)).first();
        doc.put("status",status);
        collection.insertOne(doc);
    }

    public Shipments getone(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Shipments");
        Document doc = collection.find(eq("_id",id)).first();
        Shipments shipment = new Shipments();
        shipment.id = doc.getObjectId("_id");
        shipment.trackingid = doc.getString("trackingid");
        shipment.orderid = doc.getString("orderid");
        shipment.stype = doc.getString("stype");
        shipment.status = doc.getString("status");
        return shipment;
    }

    public ArrayList<Shipments> getall(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Shipments");
        FindIterable<Document> docs = collection.find();
        ArrayList<Shipments> list=new ArrayList<>();
        for (Document doc:docs){
            Shipments shipment = new Shipments();
            shipment.id = doc.getObjectId("_id");
            shipment.trackingid = doc.getString("amount");
            shipment.stype = doc.getString("bankid");
            shipment.orderid = doc.getString("orderid");
            shipment.status = doc.getString("status");
            list.add(shipment);
        }
        return list;
    }

    public ArrayList<Shipments> getallbyorderid(String orderid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Shipments");
        FindIterable<Document> docs = collection.find(eq("orderid",orderid));
        ArrayList<Shipments> list=new ArrayList<>();
        for (Document doc:docs){
            Shipments shipment = new Shipments();
            shipment.id = doc.getObjectId("_id");
            shipment.trackingid = doc.getString("amount");
            shipment.stype = doc.getString("bankid");
            shipment.orderid = doc.getString("orderid");
            shipment.status = doc.getString("status");
            list.add(shipment);
        }
        return list;
    }
}
