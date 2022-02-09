package com.teamproject.covid19vaccinereview.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.springframework.stereotype.Component;

@Component
public class JsonParseUtil {

    public String getJsonValue(ResponseBodyExtractionOptions body, String key){
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(body.asString());
        JsonObject jsonObject = (JsonObject) parse;

        return jsonObject.get(key).getAsString();
    }
}
