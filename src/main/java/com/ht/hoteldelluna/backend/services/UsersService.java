package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.User;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UsersService {
    private final Parser parser = new Parser();
    Connection dbConnection = Connection.shared;
    MongoCollection<Document> userCollection = dbConnection.getDatabase().getCollection("users");

    public User getUserByUsername(String username) throws Exception {
        Document doc = userCollection.find(new Document("username",username)).first();
       if (doc == null) throw new Exception("Tài khoản không tồn tại!");
        return parser.fromDocument(doc, User.class);
    }
}
