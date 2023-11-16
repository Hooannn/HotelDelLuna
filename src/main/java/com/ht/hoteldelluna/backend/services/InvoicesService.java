package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.models.Invoice;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

public class InvoicesService {
    private final Parser parser = new Parser();
    Connection dbConnection = Connection.shared;
    MongoCollection<Document> invoiceCollection = dbConnection.getDatabase().getCollection("invoices");
    MongoCollection<Document> roomCollection = dbConnection.getDatabase().getCollection("rooms");

    public List<Invoice> getInvoices() {
        List<Document> documents = new ArrayList<>();
        invoiceCollection.find().sort(new Document("checkOutTime", -1)).into(documents);
        documents.forEach(this::populateRoomDocument);
        return parser.fromDocuments(documents, Invoice.class);
    }

    public Invoice getInvoiceById(String invoiceId) {
        Document doc = invoiceCollection.find(new Document("_id", new ObjectId(invoiceId))).first();
        assert doc != null;
        populateRoomDocument(doc);
        return parser.fromDocument(doc, Invoice.class);
    }

    public InsertOneResult addInvoice(Invoice invoice, String roomId) {
        Document document = parser.toDocument(invoice);
        document.put("room", new ObjectId(roomId));
        return invoiceCollection.insertOne(document);
    }

    public InsertManyResult addMockInvoices() {
        List<Invoice> invoices = new ArrayList<>();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            invoices.add(new Invoice(dateFormat.parse("2023-11-01").toString(), new Date().toString(), 400000, "Ha Gia Huy 1", null));
            invoices.add(new Invoice(dateFormat.parse("2023-11-05").toString(), new Date().toString(), 300000, "Ha Gia Huy 2", null));
            invoices.add(new Invoice(dateFormat.parse("2023-11-11").toString(), new Date().toString(), 200000, "Ha Gia Huy 3", null));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Document> documents = parser.toDocuments(invoices);
        documents.forEach(document -> {
            document.put("room", new ObjectId("654df870eb51b55fc617a543"));
        });
        return invoiceCollection.insertMany(documents);
    }

    private void populateRoomDocument(Document document) {
        ObjectId roomId = document.getObjectId("room");
        Document room = roomCollection.find(new Document("_id", roomId)).first();
        document.put("room", room);
    }
}
