package com.minehut.warzone.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.regex.Pattern;

/**
 * Created by luke on 3/15/16.
 */
public class MongoUtils {
    public static DBObject getIgnoreCaseQuery(String key, String value) {
        DBObject query = new BasicDBObject(key, Pattern.compile("^" + value.toLowerCase() + "$", Pattern.CASE_INSENSITIVE));
        return query;
    }
}
