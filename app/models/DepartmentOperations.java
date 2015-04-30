package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import play.db.ebean.Model;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

/**
 * Created by PKS on 4/28/15.
 */
public class DepartmentOperations extends Model {
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

    public boolean save(Departments departments){
        try{
            Document doc = new Document("cname", departments.dname);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Departments");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Departments");
        Document doc = collection.find(eq("_id",id)).first();
        collection.deleteOne(doc);
    }

    public Departments getone(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Departments");
        Document doc = collection.find(eq("_id",id)).first();
        Departments departments = new Departments();
        departments.id = doc.getObjectId("_id");
        departments.dname = doc.getString("dname");
        return departments;
    }

    public ArrayList<Departments> getall(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Departments");
        FindIterable<Document> docs = collection.find();
        ArrayList<Departments> departmentses = new ArrayList<>();
        for(Document doc : docs) {
            Departments departments = new Departments();
            departments.id = doc.getObjectId("_id");
            departments.dname = doc.getString("dname");
            departmentses.add(departments);
        }
        return departmentses;
    }
}
