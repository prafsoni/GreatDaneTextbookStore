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
public class PaymentOperations extends Model {
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

    public boolean save(Payments payment){
        try {
            Document doc = new Document("amount", payment.amount)
                    .append("bankid", payment.bankid)
                    .append("orderid", payment.orderid)
                    .append("pdate", payment.pdate)
                    .append("status", payment.status);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Payments");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){ // TODO catch proper exceptions
            System.out.println(ex);
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Payments");
        Document doc = collection.find(eq("_id",id)).first();
        collection.deleteOne(doc);
    }

    public void updatestatus(String id, String status){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Payments");
        Document doc = collection.find(eq("_id",id)).first();
        doc.put("status",status);
        collection.insertOne(doc);
    }

    public Payments getone(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Payments");
        Document doc = collection.find(eq("_id",id)).first();
        Payments payment = new Payments();
        payment.id = doc.getString("_id");
        payment.amount = doc.getDouble("amount");
        payment.bankid = doc.getString("bankid");
        payment.orderid = doc.getString("orderid");
        payment.pdate = doc.getDate("pdate");
        payment.status = doc.getString("status");
        return payment;
    }

    public ArrayList<Payments> getall(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Payments");
        FindIterable<Document> docs = collection.find();
        ArrayList<Payments> list=new ArrayList<>();
        for (Document doc:docs){
            Payments payment = new Payments();
            payment.id = doc.getString("_id");
            payment.amount = doc.getDouble("amount");
            payment.bankid = doc.getString("bankid");
            payment.orderid = doc.getString("orderid");
            payment.pdate = doc.getDate("pdate");
            payment.status = doc.getString("status");
            list.add(payment);
        }
        return list;
    }

    public ArrayList<Payments> getallbyorder(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Payments");
        FindIterable<Document> docs = collection.find(eq("orderid", id));
        ArrayList<Payments> list=new ArrayList<>();
        for (Document doc:docs){
            Payments payment = new Payments();
            payment.id = doc.getString("_id");
            payment.amount = doc.getDouble("amount");
            payment.bankid = doc.getString("bankid");
            payment.orderid = doc.getString("orderid");
            payment.pdate = doc.getDate("pdate");
            payment.status = doc.getString("status");
            list.add(payment);
        }
        return list;
    }
}
