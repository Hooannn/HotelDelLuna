package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.enums.ReservationStatus;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Reservation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ReservationsService {
    private final Parser parser = new Parser();
    Connection dbConnection = Connection.shared;
    MongoCollection<Document> roomCollection = dbConnection.getDatabase().getCollection("rooms");
    MongoCollection<Document> reservationCollection = dbConnection.getDatabase().getCollection("reservations");

    public List<Reservation> getReservations() {
        List<Document> documents = new ArrayList<>();
        reservationCollection.find().into(documents);
        documents.forEach(this::populateReservationDocument);
        return parser.fromDocuments(documents, Reservation.class);
    }

    public List<Reservation> getOpeningReservations() {
        List<Document> documents = new ArrayList<>();
        reservationCollection.find(new Document("status", ReservationStatus.OPENING)).into(documents);
        documents.forEach(this::populateReservationDocument);
        return parser.fromDocuments(documents, Reservation.class);
    }
    public Reservation getReservationById(String reservationId) {
        Document doc = reservationCollection.find(new Document("_id", new ObjectId(reservationId))).first();
        assert doc != null;
        populateReservationDocument(doc);
        return parser.fromDocument(doc, Reservation.class);
    }

    public Reservation getReservationRoomId(String roomId) {
        Document doc = reservationCollection.find(new Document("room", new ObjectId(roomId))).first();
        assert doc != null;
        populateReservationDocument(doc);
        return parser.fromDocument(doc, Reservation.class);
    }

    public UpdateResult checkout(String reservationId, String roomId) {
        RoomsService roomsService = new RoomsService();
        UpdateResult res = reservationCollection.updateOne(new Document("_id", new ObjectId(reservationId)), new Document("$set", new Document("status", ReservationStatus.CLOSED)));
        roomsService.updateRoomStatus(roomId, RoomStatus.MAINTENANCE);
        return res;
    }

    public UpdateResult closeReservation(String reservationId, String roomId) {
        RoomsService roomsService = new RoomsService();
        UpdateResult res = reservationCollection.updateOne(new Document("_id", new ObjectId(reservationId)), new Document("$set", new Document("status", ReservationStatus.CLOSED)));
        roomsService.updateRoomStatus(roomId, RoomStatus.AVAILABLE);
        return res;
    }

    public UpdateResult updateNote(String reservationId, String updatedNote) {
        return reservationCollection.updateOne(new Document("_id", new ObjectId(reservationId)), new Document("$set", new Document("note", updatedNote)));
    }

    public InsertOneResult addReservation(Reservation reservation, String roomId) {
        RoomsService roomsService = new RoomsService();
        Document document = parser.toDocument(reservation);
        document.put("room", new ObjectId(roomId));
        roomsService.updateRoomStatus(roomId, RoomStatus.OCCUPIED);
        return reservationCollection.insertOne(document);
    }

    public DeleteResult deleteReservation(String reservationId) {
        return reservationCollection.deleteOne(new Document("_id", new ObjectId(reservationId)));
    }

    private void populateReservationDocument(Document document) {
        ObjectId roomId = document.getObjectId("room");
        Document room = roomCollection.find(new Document("_id", roomId)).first();
        document.put("room", room);
    }
}
