package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import play.db.ebean.Model;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

/**
 * Created by PKS on 4/30/15.
 */
public class ContactMsgOperations extends Model {
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

    public boolean save(ContactMsg contactMsg){
        try{
            Document doc = new Document("msg", contactMsg.msg)
                    .append("senderid",contactMsg.senderid)
                    .append("sdate",contactMsg.sdate)
                    .append("receiverid",contactMsg.receiverid)
                    .append("subject",contactMsg.subject);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Messages");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public ArrayList<ContactMsg> getall(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Messages");
        FindIterable<Document> results = collection.find(or(eq("userid", id), eq("senderid", id)));
        ArrayList<ContactMsg> list = new ArrayList<>();
        for(Document doc : results){
            ContactMsg contactMsg = new ContactMsg();
            contactMsg.msg = doc.getString("msg");
            contactMsg.sdate = doc.getDate("sdate");
            contactMsg.id = doc.getObjectId("_id");
            contactMsg.receiverid = doc.getString("receiverid");
            contactMsg.senderid = doc.getString("senderid");
            contactMsg.subject = doc.getString("subject");
            list.add(contactMsg);
        }
        return list;
    }

    public ArrayList<ContactMsg> getallsent(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Messages");
        FindIterable<Document> results = collection.find(eq("senderid", id));
        ArrayList<ContactMsg> list = new ArrayList<>();
        for(Document doc : results){
            ContactMsg contactMsg = new ContactMsg();
            contactMsg.msg = doc.getString("msg");
            contactMsg.sdate = doc.getDate("sdate");
            contactMsg.id = doc.getObjectId("_id");
            contactMsg.receiverid = doc.getString("receiverid");
            contactMsg.senderid = doc.getString("senderid");
            contactMsg.subject = doc.getString("subject");
            list.add(contactMsg);
        }
        return list;
    }

    public ArrayList<ContactMsg> getallreceived(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Messages");
        FindIterable<Document> results = collection.find(eq("receiverid", id));
        ArrayList<ContactMsg> list = new ArrayList<>();
        for(Document doc : results){
            ContactMsg contactMsg = new ContactMsg();
            contactMsg.msg = doc.getString("msg");
            contactMsg.sdate = doc.getDate("sdate");
            contactMsg.id = doc.getObjectId("_id");
            contactMsg.receiverid = doc.getString("receiverid");
            contactMsg.senderid = doc.getString("senderid");
            contactMsg.subject = doc.getString("subject");
            list.add(contactMsg);
        }
        return list;
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Messages");
        Document doc = collection.find(eq("_id",id)).first();
        collection.deleteOne(doc);
    }
}
