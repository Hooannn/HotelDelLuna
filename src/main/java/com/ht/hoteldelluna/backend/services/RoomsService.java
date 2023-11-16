package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Room;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class RoomsService {
    private final Parser parser = new Parser();
    Connection dbConnection = Connection.shared;
    MongoCollection<Document> roomCollection = dbConnection.getDatabase().getCollection("rooms");
    MongoCollection<Document> roomTypeCollection = dbConnection.getDatabase().getCollection("room_types");
    MongoCollection<Document> floorCollection = dbConnection.getDatabase().getCollection("floors");

    public List<Room> getRooms() {
        List<Document> documents = new ArrayList<>();
        roomCollection.find().into(documents);
        documents.forEach(this::populateRoomDocument);
        System.out.println(documents);
        return parser.fromDocuments(documents, Room.class);
    }
    public Room getRoomById(String roomId) {
        Document doc = roomCollection.find(new Document("_id", new ObjectId(roomId))).first();
        assert doc != null;
        populateRoomDocument(doc);
        return parser.fromDocument(doc, Room.class);
    }

    public InsertOneResult addRoom(Room room, String roomTypeId, String floorId) {
        Document document = parser.toDocument(room);
        document.put("floor", new ObjectId(floorId));
        document.put("type", new ObjectId(roomTypeId));
        return roomCollection.insertOne(document);
    }

    public DeleteResult deleteRoom(String roomId) {
        return roomCollection.deleteOne(new Document("_id", new ObjectId(roomId)));
    }

    public UpdateResult updateRoomStatus(String roomId, RoomStatus status) {
        return roomCollection.updateOne(new Document("_id", new ObjectId(roomId)), new Document("$set", new Document("status", status)));
    }

    public InsertManyResult addMockRooms() {
        List<Room> firstFloorRooms = new ArrayList<>();
        firstFloorRooms.add(new Room("A002", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A003", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A004", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A005", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A006", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A007", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A008", 200000, RoomStatus.AVAILABLE));
        firstFloorRooms.add(new Room("A009", 200000, RoomStatus.AVAILABLE));
        List<Room> secondFloorRooms = new ArrayList<>();
        secondFloorRooms.add(new Room("B002", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B003", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B004", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B005", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B006", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B007", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B008", 200000, RoomStatus.AVAILABLE));
        secondFloorRooms.add(new Room("B009", 200000, RoomStatus.AVAILABLE));
        List<Room> thirdFloorRooms = new ArrayList<>();
        thirdFloorRooms.add(new Room("C002", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C003", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C004", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C005", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C006", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C007", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C008", 200000, RoomStatus.AVAILABLE));
        thirdFloorRooms.add(new Room("C009", 200000, RoomStatus.AVAILABLE));

        List<Document> firstDocuments = parser.toDocuments(firstFloorRooms);
        List<Document> secondDocuments = parser.toDocuments(secondFloorRooms);
        List<Document> thirdDocuments = parser.toDocuments(thirdFloorRooms);

        firstDocuments.forEach(document -> {
            document.put("floor", new ObjectId("654c39a39223a16d7f5bbe61"));
            document.put("type", new ObjectId("654df75ece6981358479f11e"));
        });

        secondDocuments.forEach(document -> {
            document.put("floor", new ObjectId("654c38a7f9dc152c6575314e"));
            document.put("type", new ObjectId("654df75ece6981358479f11d"));
        });

        thirdDocuments.forEach(document -> {
            document.put("floor", new ObjectId("654c38a7f9dc152c6575314f"));
            document.put("type", new ObjectId("654df75ece6981358479f11f"));
        });

        List<Document> documents = new ArrayList<>();
        documents.addAll(firstDocuments);
        documents.addAll(secondDocuments);
        documents.addAll(thirdDocuments);

        return roomCollection.insertMany(documents);
    }

    private void populateRoomDocument(Document document) {
        ObjectId floorId = document.getObjectId("floor");
        ObjectId roomTypeId = document.getObjectId("type");
        Document floor = floorCollection.find(new Document("_id", floorId)).first();
        Document roomType = roomTypeCollection.find(new Document("_id", roomTypeId)).first();
        document.put("floor", floor);
        document.put("type", roomType);
    }
}
