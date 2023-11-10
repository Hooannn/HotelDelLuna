package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.models.RoomType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeServices {
    private final Parser parser = new Parser();
    Connection dbConnection = Connection.shared;
    MongoCollection<Document> roomTypeCollection = dbConnection.getDatabase().getCollection("room_types");

    public List<RoomType> getRoomTypes() {
        List<Document> documents = new ArrayList<>();
        roomTypeCollection.find().into(documents);
        return parser.fromDocuments(documents, RoomType.class);
    }

    public RoomType getRoomTypeById(String roomTypeId) {
        Document doc = roomTypeCollection.find(new Document("_id", new ObjectId(roomTypeId))).first();
        assert doc != null;
        return parser.fromDocument(doc, RoomType.class);
    }

    public InsertOneResult addRoomType(RoomType roomType) {
        Document document = parser.toDocument(roomType);
        return roomTypeCollection.insertOne(document);
    }

    public DeleteResult deleteRoomType(String roomTypeId) {
        return roomTypeCollection.deleteOne(new Document("_id", new ObjectId(roomTypeId)));
    }

    public InsertManyResult addMockRoomTypes() {
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(new RoomType("VIP"));
        roomTypes.add(new RoomType("Thường"));
        roomTypes.add(new RoomType("Super Vip"));
        List<Document> documents = parser.toDocuments(roomTypes);
        return roomTypeCollection.insertMany(documents);
    }
}
