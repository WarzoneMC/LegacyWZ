package com.minehut.warzone.util.parse;

import java.net.InetSocketAddress;

/**
 * Created by luke on 10/18/15.
 */
public class ParseUtils {

    public static String parseIP(InetSocketAddress socket) {
        String s = socket.toString();
        s = s.replace("/", "");
        s = s.split(":")[0];
        return s;
    }

    public static String parseIP(String hostname) {
        String s = hostname;
        s = s.replace("/", "");
        s = s.split(":")[0];
        return s;
    }
}
