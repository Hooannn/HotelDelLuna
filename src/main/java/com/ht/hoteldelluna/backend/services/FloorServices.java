package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.models.Floor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class FloorServices {
    private final Parser parser = new Parser();
    Connection dbConnection = Connection.shared;
    MongoCollection<Document> floorCollection = dbConnection.getDatabase().getCollection("floors");

    public List<Floor> getFloors() {
        List<Document> documents = new ArrayList<>();
        floorCollection.find().into(documents);
        return parser.fromDocuments(documents, Floor.class);
    }

    public Floor getFloorById(String floorId) {
        Document doc = floorCollection.find(new Document("_id", new ObjectId(floorId))).first();
        assert doc != null;
        return parser.fromDocument(doc, Floor.class);
    }

    public InsertOneResult addFloor(Floor floor) {
        Document document = parser.toDocument(floor);
        return floorCollection.insertOne(document);
    }

    public DeleteResult deleteFloor(String floorId) {
        return floorCollection.deleteOne(new Document("_id", new ObjectId(floorId)));
    }

    public InsertManyResult addMockFloors() {
        List<Floor> floors = new ArrayList<>();
        floors.add(new Floor(1));
        floors.add(new Floor(2));
        floors.add(new Floor(3));
        List<Document> documents = parser.toDocuments(floors);
        return floorCollection.insertMany(documents);
    }
}
