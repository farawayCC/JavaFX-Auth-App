package sample.Utils;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sample.MyUser;

public class MyMongo {

    /**
     * Registers new user to DB
     * @param user user object
     * @return True if successfully. False if user is already registered
     */
    public static boolean registerNewUser(MyUser user) {
        try {
            MongoClient mongoClient = MongoClients.create(Strings.dbURI);
            MongoDatabase database = mongoClient.getDatabase(Strings.dbDBName);

            MongoCollection<Document> testCollection = database.getCollection(Strings.dbCollectionName);

            Document newCredDoc = new Document();
            newCredDoc.put(DBKeys.ID.toString(), user.getUsername());
            newCredDoc.put(DBKeys.PASS_TOKEN.toString(), user.hashPass());
            testCollection.insertOne(newCredDoc);

            mongoClient.close();
            return true;
        } catch (MongoWriteException exception) {
//            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if this user can login with this password
     * @param username username
     * @param pass password
     * @return true if successful, false if creds are wrong
     */
    public static boolean checkUserCreds(String username, String pass) {
        MongoClient mongoClient = MongoClients.create(Strings.dbURI);
        MongoDatabase database = mongoClient.getDatabase(Strings.dbDBName);
        MongoCollection<Document> myCollection = database.getCollection(Strings.dbCollectionName);
        Document credDocument = myCollection.find(Filters.eq(DBKeys.ID.toString(), username.toLowerCase())).first();
        if (credDocument!=null && credDocument.containsKey(DBKeys.PASS_TOKEN.toString())) {
            String passToken = (String) credDocument.get(DBKeys.PASS_TOKEN.toString());
            MyUser newUser = new MyUser(username, pass);
            return newUser.checkCredentials(passToken);
        } else return false;
    }


    private enum DBKeys {
        ID("_id"),
        PASS_TOKEN("passToken")
        ;

        private final String text;

        DBKeys(final String text) { this.text = text; }

        @Override
        public String toString() {
            return text;
        }
    }



}
