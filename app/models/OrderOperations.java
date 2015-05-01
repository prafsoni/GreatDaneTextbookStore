package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by PKS on 4/27/15.
 */
public class OrderOperations extends Model {
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

    public class a extends HashMap<String,Integer>{}
    public boolean save(Orders order){
        try {
            Document doc = new Document("Books", order.Books)
                    .append("orderdate", order.orderdate)
                    .append("price", order.price)
                    .append("quantity", order.quantity)
                    .append("status", order.status)
                    .append("userid",order.userid)
                    .append("sellerid",order.sellerid)
                    .append("Shippingid", order.Shippingid);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Orders");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){ // TODO catch proper exceptions
            System.out.println(ex);
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
        Document doc = collection.find(eq("_id",id)).first();
        collection.deleteOne(doc);
    }

    public Orders getone(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
        Document doc = collection.find(eq("_id",id)).first();
        Orders order = new Orders();
        order.Books = doc.get("Books",a.class);
        order.id = doc.getObjectId("_id");
        order.orderdate = doc.getDate("orderdate");
        order.price = doc.getDouble("price");
        order.quantity = doc.getInteger("quantity");
        order.Shippingid = doc.getString("Shippingid");
        order.status = doc.getString("status");
        order.userid = doc.getString("userid");
        order.sellerid = doc.getString("sellerid");
        return order;
    }

    public ArrayList<Orders> getallplaced(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
        FindIterable<Document> docs = collection.find(eq("userid", userid));
        ArrayList<Orders> list = new ArrayList<>();
        for(Document doc : docs) {
            Orders order = new Orders();
            order.Books = doc.get("Books", a.class);
            order.id = doc.getObjectId("_id");
            order.orderdate = doc.getDate("orderdate");
            order.price = doc.getDouble("price");
            order.quantity = doc.getInteger("quantity");
            order.Shippingid = doc.getString("Shippingid");
            order.status = doc.getString("status");
            order.userid = doc.getString("userid");
            order.sellerid = doc.getString("sellerid");
            list.add(order);
        }
        return list;
    }

    public ArrayList<Orders> getallreceived(String sellerid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
        FindIterable<Document> docs = collection.find(eq("sellerid", sellerid));
        ArrayList<Orders> list = new ArrayList<>();
        for(Document doc : docs) {
            Orders order = new Orders();
            order.Books = doc.get("Books", a.class);
            order.id = doc.getObjectId("_id");
            order.orderdate = doc.getDate("orderdate");
            order.price = doc.getDouble("price");
            order.quantity = doc.getInteger("quantity");
            order.Shippingid = doc.getString("Shippingid");
            order.status = doc.getString("status");
            order.userid = doc.getString("userid");
            order.sellerid = doc.getString("sellerid");
            list.add(order);
        }
        return list;
    }

    public boolean update(Orders order){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
        Document doc = collection.find(eq("_id", order.id)).first();
        Document newdoc = new Document("Books", order.Books)
                .append("orderdate", order.orderdate)
                .append("price", order.price)
                .append("quantity", order.quantity)
                .append("status", order.status)
                .append("userid",order.userid)
                .append("sellerid", order.sellerid)
                .append("Shippingid", order.Shippingid);
        UpdateResult result = collection.updateOne(doc, newdoc);
        if(result.wasAcknowledged()){
            return true;
        }
        else {
            return false;
        }
    }
}
