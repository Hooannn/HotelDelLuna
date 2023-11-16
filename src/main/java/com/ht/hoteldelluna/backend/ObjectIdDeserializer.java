package com.ht.hoteldelluna.backend;
import com.google.gson.*;
import org.bson.types.ObjectId;

public class ObjectIdDeserializer implements JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String oid = jsonObject.getAsJsonPrimitive("$oid").getAsString();
        return new ObjectId(oid);
    }
}