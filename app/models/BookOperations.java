package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.db.ebean.Model;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by PKS on 4/22/15.
 */
public class BookOperations extends Model {

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

    public Boolean addbook(Books book){
        try {
            Document doc = new Document("title", book.title)
                    .append("isbn", book.isbn)
                    .append("edition", book.edition)
                    .append("price", book.price)
                    .append("authors", book.authors)
                    .append("stock", book.stock)
                    .append("seller", book.seller)
                    .append("pic", book.picid)
                    .append("description", book.description)
                    .append("year", book.year)
                    .append("category", book.category);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Books");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public void delete(String id){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        Document result = collection.find(eq("_id",id)).first();
        collection.deleteOne(result);
    }

    public Books getone(String strid){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        ObjectId id = new ObjectId(strid);
        Document result = collection.find(eq("_id",id)).first();
        Books book = new Books();
        book.id = result.getObjectId("_id");
        book.authors = result.getString("authors");
        book.edition = result.getInteger("edition");
        book.isbn = result.getString("isbn");
        book.picid = result.getString("picid");
        book.price = result.getDouble("price");
        book.seller = result.getString("seller");
        book.stock = result.getInteger("stock");
        book.title = result.getString("title");
        book.description = result.getString("description");
        book.year = result.getInteger("year");
        book.category = result.getInteger("category");
        return book;
    }

    public ArrayList<Books> getcategory(String category){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        int c = Integer.parseInt(category);
        FindIterable<Document> results = collection.find(eq("category",c)).limit(300);
        ArrayList<Books> list = new ArrayList<>();
        for(Document result: results){
            Books book = new Books();
            book.id = result.getObjectId("_id");
            book.authors = result.getString("authors");
            book.edition = result.getInteger("edition");
            book.isbn = result.getString("isbn");
            book.picid = result.getString("picid");
            book.price = result.getDouble("price");
            book.seller = result.getString("seller");
            book.stock = result.getInteger("stock");
            book.title = result.getString("title");
            book.description = result.getString("description");
            book.year = result.getInteger("year");
            book.category = result.getInteger("category");
            list.add(book);
        }
        return list;
    }

    public void updatestock(String id,int reduceby){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        Document result = collection.find(eq("_id",id)).first();
        int stock = result.getInteger("stock");
        result.replace("stock",stock,stock-reduceby);
    }

    public ArrayList<Books> getall(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        FindIterable<Document> results = collection.find().limit(300);
        ArrayList<Books> list = new ArrayList<>();
        for(Document result: results){
            Books book = new Books();
            book.id = result.getObjectId("_id");
            book.authors = result.getString("authors");
            book.edition = result.getInteger("edition");
            book.isbn = result.getString("isbn");
            book.picid = result.getString("picid");
            book.price = result.getDouble("price");
            book.seller = result.getString("seller");
            book.stock = result.getInteger("stock");
            book.title = result.getString("title");
            book.description = result.getString("description");
            book.year = result.getInteger("year");
            book.category = result.getInteger("category");
            list.add(book);
        }
        return list;
    }
    public ArrayList<Books> getThree(){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        FindIterable<Document> results = collection.find().limit(3);
        ArrayList<Books> list = new ArrayList<>();
        for(Document result: results){
            Books book = new Books();
            book.id = result.getObjectId("_id");
            book.authors = result.getString("authors");
            book.edition = result.getInteger("edition");
            book.isbn = result.getString("isbn");
            book.picid = result.getString("picid");
            book.price = result.getDouble("price");
            book.seller = result.getString("seller");
            book.stock = result.getInteger("stock");
            book.title = result.getString("title");
            book.description = result.getString("description");
            book.year = result.getInteger("year");
            book.category = result.getInteger("category");
            list.add(book);
        }
        return list;
    }

    public ArrayList<Books> search(String text){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        Document doc = new Document("text","Books").append("search", text);
        Document result = database.runCommand(doc);
        ArrayList<Books> list = new ArrayList<>();
        //com.mongodb.BasicDBObject searchCmd = new com.mongodb.BasicDBObject();
        //searchCmd.put("text", "Books");
        //searchCmd.put("search", text);
        //Document result = database.runCommand(searchCmd);
        //ArrayList<Books> list = new ArrayList<>();

        //for(Document result: results) {
        Books book = new Books();
        book.id = result.getObjectId("_id");
        book.authors = result.getString("authors");
        book.edition = result.getInteger("edition");
        book.isbn = result.getString("isbn");
        book.picid = result.getString("picid");
        book.price = result.getDouble("price");
        book.seller = result.getString("seller");
        book.stock = result.getInteger("stock");
        book.title = result.getString("title");
        book.description = result.getString("description");
        book.year = result.getInteger("year");
        book.category = result.getInteger("category");
        list.add(book);
        //}
        return list;
    }

    public void update(Books book){
        com.mongodb.client.MongoDatabase database = getdatabase();
        com.mongodb.client.MongoCollection<Document> collection = database.getCollection("Books");
        Document doc = new Document("_id", book.id)
                .append("title",book.title)
                .append("isbn", book.isbn)
                .append("edition", book.edition)
                .append("price", book.price)
                .append("authors", book.authors)
                .append("stock", book.stock)
                .append("seller", book.seller)
                .append("pic", book.picid)
                .append("description", book.description)
                .append("year", book.year)
                .append("category", book.category);
        collection.updateOne(eq("_id", book.id), doc);
    }
}
