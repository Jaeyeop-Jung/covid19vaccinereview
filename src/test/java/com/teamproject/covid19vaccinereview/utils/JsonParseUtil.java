package com.teamproject.covid19vaccinereview.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonParseUtil {

    public String getJsonValue(ResponseBodyExtractionOptions body, String key){
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(body.asString());
        JsonObject jsonObject = (JsonObject) parse;

        return jsonObject.get(key).getAsString();
    }
    
    public List<String> getJsonArrayValue(ResponseBodyExtractionOptions body, String key){
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(body.asString());
        JsonObject jsonObject = (JsonObject) parse;
        JsonArray asJsonArray = jsonObject.getAsJsonArray(key);

        List<String> response = new ArrayList<>();

        for (JsonElement jsonElement : asJsonArray) {
            response.add(jsonElement.getAsString());
        }

        
        return response;
    }

    public List<String> getChildrenCommentIdByJsonArray(ResponseBodyExtractionOptions body){
        List<String> response = new ArrayList<>();
        
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(body.asString());
        JsonArray asJsonArray = parse.getAsJsonArray();

        for (JsonElement jsonElement : asJsonArray) {
            response.add(jsonElement.getAsJsonObject().get("id").getAsString());
            for (JsonElement child : jsonElement.getAsJsonObject().getAsJsonArray("children")) {
                response.add(child.getAsJsonObject().get("id").getAsString());
            }
        }

        return response;
    }

    public Map<String, Boolean> getCommentLikeByJsonArray(ResponseBodyExtractionOptions body){
        Map<String, Boolean> response = new HashMap<>();

        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(body.asString());
        JsonArray asJsonArray = parse.getAsJsonArray();

        for (JsonElement jsonElement : asJsonArray) {
            response.put(jsonElement.getAsJsonObject().get("id").getAsString(), jsonElement.getAsJsonObject().get("thisUserLike").getAsBoolean());
        }

        return response;
    }
}
