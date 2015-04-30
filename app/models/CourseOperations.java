package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import org.bson.Document;
import play.db.ebean.Model;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

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
    class a extends ArrayList<String>{}

    public boolean save(Courses courses){
        try{
            Document doc = new Document("cname", courses.cname)
                    .append("departments",courses.departments);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Courses");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Courses");
        Document doc = collection.find(eq("_id",id)).first();
        collection.deleteOne(doc);
    }

    public void adddept(String id, String deptid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Courses");
        Document doc = collection.find(eq("_id",id)).first();
        ArrayList<String> depts = doc.get("departments",a.class);
        depts.add(deptid);
        //doc.append("departments",deptid);
        doc.put("departments",depts);
        collection.insertOne(doc);
    }

    public void deletedept(String id, String deptid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Courses");
        Document doc = collection.find(eq("_id",id)).first();
        ArrayList<String> depts = doc.get("departments",a.class);
        depts.remove(deptid);
        //doc.append("departments",deptid);
        doc.put("departments",depts);
        collection.insertOne(doc);
    }

}
