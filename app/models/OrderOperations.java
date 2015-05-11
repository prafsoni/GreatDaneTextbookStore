package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

    UserOperations uo = new UserOperations();


         class a extends ArrayList<Integer> {}

        class b extends ArrayList<String> {}

        public boolean save(Orders order) {
            try {
                ArrayList<Integer> qlist = new ArrayList<>();
                ArrayList<String> blist = new ArrayList<>();
                Set<String> bs = order.Books.keySet();
                for (String val : bs) {
                    qlist.add(order.Books.get(val));
                    blist.add(val);
                }
                Document doc = new Document("Books", order.Books)
                        .append("orderdate", order.orderdate)
                        .append("price", order.price)
                        .append("quantity", order.quantity)
                        .append("status", order.status)
                        .append("userid", order.userid)
                        .append("sellerid", order.sellerid)
                        .append("qlist", qlist)
                        .append("blist", blist)
                        .append("Shippingid", order.Shippingid);
                com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
                MongoCollection<Document> collection = database.getCollection("Orders");
                collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
                return true;
            } catch (Exception ex) { // TODO catch proper exceptions
                System.out.println(ex);
                return false;
            }
        }

    public boolean delete2(String id) {
        try {
            com.mongodb.client.MongoDatabase database = getdatabase();
            com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
            ObjectId oid = new ObjectId(id);
            Document doc = collection.find(eq("_id", oid)).first();
            collection.deleteOne(doc);
            return true;
        }catch (Exception ex) { // TODO catch proper exceptions
            System.out.println(ex);
            return false;
        }
    }

        public Orders getone(String id) {
            com.mongodb.client.MongoDatabase database = getdatabase();
            com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
            Document doc = collection.find(eq("_id", id)).first();
            Orders order = new Orders();
            HashMap<String, Integer> bmap = new HashMap<>();
            ArrayList<Integer> qlist = new ArrayList<>();
            ArrayList<String> blist = new ArrayList<>();
            qlist = doc.get("qlist", a.class);
            blist = doc.get("blist", b.class);
            for (int i = 0; i < blist.size(); i++) {
                bmap.put(blist.get(i), qlist.get(i));
            }
            order.Books = bmap;
            order.id = doc.getObjectId("_id");
            order.orderdate = doc.getDate("orderdate");
            order.price = doc.getDouble("price");
            order.quantity = doc.getInteger("quantity");
            order.Shippingid = doc.getString("Shippingid");
            order.status = doc.getString("status");
            order.userid = doc.getString("userid");
            order.sellerid = doc.getString("sellerid");
            //order.sellerid = uo.getuname(order.sellerid);
            //order.userid = uo.getuname(order.userid);
            return order;
        }

        public ArrayList<Orders> getallplaced(String userid) {
            com.mongodb.client.MongoDatabase database = getdatabase();
            com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
            FindIterable<Document> docs = collection.find(eq("userid", userid));
            ArrayList<Orders> list = new ArrayList<>();
            for (Document doc : docs) {
                Orders order = new Orders();
                HashMap<String, Integer> bmap = new HashMap<>();
                ArrayList<Integer> qlist = new ArrayList<>();
                ArrayList<String> blist = new ArrayList<>();
                qlist = doc.get("qlist", a.class);
                blist = doc.get("blist", b.class);
                for (int i = 0; i < blist.size(); i++) {
                    bmap.put(blist.get(i), qlist.get(i));
                }
                order.Books = bmap;
                order.id = doc.getObjectId("_id");
                order.orderdate = doc.getDate("orderdate");
                order.price = doc.getDouble("price");
                order.quantity = doc.getInteger("quantity");
                order.Shippingid = doc.getString("Shippingid");
                order.status = doc.getString("status");
                order.userid = doc.getString("userid");
                order.sellerid = doc.getString("sellerid");

                //order.userid = uo.getuname(order.userid);
                list.add(order);
            }
            return list;
        }

        public ArrayList<Orders> getallreceived(String sellerid) {
            com.mongodb.client.MongoDatabase database = getdatabase();
            com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
            FindIterable<Document> docs = collection.find(eq("sellerid", sellerid));
            ArrayList<Orders> list = new ArrayList<>();
            for (Document doc : docs) {
                Orders order = new Orders();
                HashMap<String, Integer> bmap = new HashMap<>();
                ArrayList<Integer> qlist = new ArrayList<>();
                ArrayList<String> blist = new ArrayList<>();
                qlist = doc.get("qlist", a.class);
                blist = doc.get("blist", b.class);
                for (int i = 0; i < blist.size(); i++) {
                    bmap.put(blist.get(i), qlist.get(i));
                }
                order.Books = bmap;
                order.id = doc.getObjectId("_id");
                order.orderdate = doc.getDate("orderdate");
                order.price = doc.getDouble("price");
                order.quantity = doc.getInteger("quantity");
                order.Shippingid = doc.getString("Shippingid");
                order.status = doc.getString("status");
                order.userid = doc.getString("userid");
                order.sellerid = doc.getString("sellerid");

                //order.userid = uo.getuname(order.userid);
                list.add(order);
            }
            return list;
        }

        public ArrayList<Orders> getall() {
            com.mongodb.client.MongoDatabase database = getdatabase();
            com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Orders");
            FindIterable<Document> docs = collection.find();
            ArrayList<Orders> list = new ArrayList<>();
            for (Document doc : docs) {
                Orders order = new Orders();
                HashMap<String, Integer> bmap = new HashMap<>();
                ArrayList<Integer> qlist = new ArrayList<>();
                ArrayList<String> blist = new ArrayList<>();
                qlist = doc.get("qlist", a.class);
                blist = doc.get("blist", b.class);
                for (int i = 0; i < blist.size(); i++) {
                    bmap.put(blist.get(i), qlist.get(i));
                }
                order.Books = bmap;
                order.id = doc.getObjectId("_id");
                order.orderdate = doc.getDate("orderdate");
                order.price = doc.getDouble("price");
                order.quantity = doc.getInteger("quantity");
                order.Shippingid = doc.getString("Shippingid");
                order.status = doc.getString("status");
                order.userid = doc.getString("userid");
                order.sellerid = doc.getString("sellerid");

                //order.userid = uo.getuname(order.userid);
                list.add(order);
            }
            return list;
        }
}
