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
public class BankInfoOperations extends Model {
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

    public boolean save(BankInfo bankInfo){
        try{
            Document doc = new Document("bankid",bankInfo.bankid)
                    .append("acctype",bankInfo.acctype)
                    .append("acc",bankInfo.acc)
                    .append("userid",bankInfo.userid)
                    .append("billaddr",bankInfo.billaddr)
                    .append("routing",bankInfo.routing)
                    .append("flow",bankInfo.flow);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("BankInfo");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
        Document result = collection.find(eq("_id",id)).first();
        collection.deleteOne(result);
    }

    public void Update(BankInfo bankInfo){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
        Document doc = new Document("bankid",bankInfo.bankid)
                .append("acctype",bankInfo.acctype)
                .append("acc",bankInfo.acc)
                .append("userid",bankInfo.userid)
                .append("billaddr",bankInfo.billaddr)
                .append("routing", bankInfo.routing)
                .append("flow", bankInfo.flow);
        collection.updateOne(eq("_id", bankInfo.id), doc);
    }

    public ArrayList<BankInfo> getall(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
        FindIterable<Document> results = collection.find(eq("userid", userid));
        ArrayList<BankInfo> list = new ArrayList<>();
        for(Document result: results ){
            BankInfo bankInfo = new BankInfo();
            bankInfo.acc = result.getInteger("acc");
            bankInfo.acctype = result.getString("acctype");
            bankInfo.bankid = result.getString("bankid");
            bankInfo.billaddr = result.getString("billaddr");
            bankInfo.flow = result.getInteger("flow");
            bankInfo.routing = result.getInteger("routing");
            bankInfo.userid = result.getString("userid");
            bankInfo.id = result.getObjectId("_id");
            list.add(bankInfo);
        }
        return list;
    }
}
