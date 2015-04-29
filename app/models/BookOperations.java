package models;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import org.bson.Document;
import play.db.ebean.Model;

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
                    //.append("description", book.description);
                    .append("year", book.year);
            com.mongodb.async.client.MongoDatabase database = getdatabaseasync();
            MongoCollection<Document> collection = database.getCollection("Books");
            collection.insertOne(doc, (Void result, final Throwable t) -> System.out.println("Inserted!"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}
