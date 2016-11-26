package com.minehut.warzone.util;

import com.mongodb.DBObject;
import org.json.simple.JSONObject;

import java.util.UUID;

/**
 * Created by luke on 6/18/16.
 */
public class JsonUtil {

    public static JSONObject getPlayerJson(String name, String uuid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("uuid", uuid);

        return jsonObject;
    }

    public static JSONObject getPlayerJson(String name, UUID uuid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("uuid", uuid.toString());

        return jsonObject;
    }
}
