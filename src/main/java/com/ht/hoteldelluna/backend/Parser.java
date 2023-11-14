package com.ht.hoteldelluna.backend;

import com.google.gson.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.List;

public class Parser {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer())
            .create();
    public Parser() {}

    public <T> List<T> fromDocuments(List<Document> documents, Class<T> classOfT) {
        return documents.stream().map(document -> gson.fromJson(document.toJson(), classOfT)).toList();
    }

    public <T> T fromDocument(Document document, Class<T> classOfT) {
        return gson.fromJson(document.toJson(), classOfT);
    }
    public <T> Document toDocument(T model) {
        return Document.parse(gson.toJson(model));
    }

    public <T> List<Document> toDocuments(List<T> models) {
        return models.stream().map((model) -> Document.parse(gson.toJson(model))).toList();
    }
}
