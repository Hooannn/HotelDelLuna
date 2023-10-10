package com.ht.hoteldelluna.models;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

public class DataManager {
    public static DataManager shared = new DataManager();
    private MongoClient client;
    private MongoDatabase database;
    private String connectionString = "mongodb+srv://hoanthui123:hoandaica123@default.evhwqpv.mongodb.net/?retryWrites=true&w=majority";

    ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .serverApi(serverApi)
            .build();

    public DataManager() {
        try {
            this.client = MongoClients.create(settings);
            this.database = this.client.getDatabase("Default");
            database.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");

            initializeCollections();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public MongoClient getClient() {
        return this.client;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    private void initializeCollections() {
        try {
            MongoCollection<Document> textCollection = this.database.getCollection("text");
            if (textCollection == null) {
                this.database.createCollection("text");
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public void addSampleDocument(String text) {
        MongoCollection<Document> textCollection = this.database.getCollection("text");
        Document doc1 = new Document("value", text);
        InsertOneResult result = textCollection.insertOne(doc1);
        System.out.println("Inserted a document with the following id: "
                + result.getInsertedId().asObjectId().getValue());
    }

    public List<Text> fetchSampleDocuments() {
        MongoCollection<Document> textCollection = this.database.getCollection("text");
        List<Text> results = new ArrayList<Text>();
        textCollection.find().forEach(document -> {
            Text text = new Text(document.getObjectId("_id").toString(), document.getString("value"));
            results.add(text);
        });

        return results;
    }
}
