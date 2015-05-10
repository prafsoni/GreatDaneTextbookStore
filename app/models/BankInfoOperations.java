package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import controllers.Bank;
import org.bson.Document;
import org.bson.types.ObjectId;
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
                    .append("bname", bankInfo.bname)
                    .append("routing", bankInfo.routing)
                    .append("flow", bankInfo.flow);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("BankInfo");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public void delete(String strid){
        System.out.println("bankid is: "+ strid);
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
        ObjectId id = new ObjectId(strid);
        Document result = collection.find(eq("_id",id)).first();
        collection.deleteOne(result);
    }

    public BankInfo getone(String strid){
        System.out.println("bankid is: "+ strid);
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
        ObjectId id = new ObjectId(strid);
        Document result = collection.find(eq("_id",id)).first();
        BankInfo bankInfo = new BankInfo();
        bankInfo.acc = result.getLong("acc");
        bankInfo.acctype = result.getString("acctype");
        bankInfo.bankid = result.getString("bankid");
        bankInfo.billaddr = result.getString("billaddr");
        bankInfo.flow = result.getInteger("flow");
        bankInfo.bname = result.getString("bname");
        bankInfo.routing = result.getLong("routing");
        bankInfo.userid = result.getString("userid");
        bankInfo.id = result.getObjectId("_id");
        return bankInfo;
    }

    public boolean update(BankInfo bankInfo){
        try{
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
            ObjectId id = new ObjectId(bankInfo.bankid);

            Document doc = new Document("_id",id)
                .append("bankid", bankInfo.bankid)
                .append("acctype", bankInfo.acctype)
                .append("acc", bankInfo.acc)
                .append("bname", bankInfo.bname)
                .append("userid", bankInfo.userid)
                .append("billaddr",bankInfo.billaddr)
                .append("routing", bankInfo.routing)
                .append("flow", bankInfo.flow);
            System.out.println("******************************************************" + "billing: " + bankInfo.billaddr);
            collection.updateOne(eq("_id", id), new Document("$set", new Document("billaddr", bankInfo.billaddr)));
            return true;
        }

        catch(Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    public ArrayList<BankInfo> getall(String userid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("BankInfo");
        FindIterable<Document> results = collection.find(eq("userid", userid));
        ArrayList<BankInfo> list = new ArrayList<>();
        for(Document result: results ){
            BankInfo bankInfo = new BankInfo();
            bankInfo.acc = result.getLong("acc");
            bankInfo.acctype = result.getString("acctype");
            bankInfo.bankid = result.getString("bankid");
            bankInfo.billaddr = result.getString("billaddr");
            bankInfo.flow = result.getInteger("flow");
            bankInfo.bname = result.getString("bname");
            bankInfo.routing = result.getLong("routing");
            bankInfo.userid = result.getString("userid");
            bankInfo.id = result.getObjectId("_id");
            list.add(bankInfo);
        }
        return list;
    }
}
