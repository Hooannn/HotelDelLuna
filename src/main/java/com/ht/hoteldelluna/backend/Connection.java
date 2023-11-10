package com.ht.hoteldelluna.backend;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
public class Connection {
    public final static Connection shared = new Connection();
    private MongoClient client;
    private MongoDatabase database;
    private final String connectionString = "mongodb+srv://hoanthui123:hoandaica123@default.evhwqpv.mongodb.net/?retryWrites=true&w=majority";
    ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .serverApi(serverApi)
            .build();

    private Connection() {
        try {
            client = MongoClients.create(settings);
            database = client.getDatabase("Default");
            database.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");

            bootstrapIndexes();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    private void bootstrapIndexes() {
        // Create all indexes here
        MongoCollection<Document> floorCollection = getDatabase().getCollection("floors");
        MongoCollection<Document> userCollection = getDatabase().getCollection("users");
        MongoCollection<Document> roomTypeCollection = getDatabase().getCollection("room_types");
        MongoCollection<Document> roomCollection = getDatabase().getCollection("rooms");
        // Make field 'num' in 'floors' unique
        floorCollection.createIndex(Indexes.ascending("num"), new IndexOptions().unique(true));
        // Make field 'username' in 'users' unique
        userCollection.createIndex(Indexes.ascending("username"), new IndexOptions().unique(true));
        // Make field 'name' in 'room_types' unique
        roomTypeCollection.createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));
        // Make field 'name' in 'rooms' unique
        roomCollection.createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
