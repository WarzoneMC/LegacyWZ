package com.minehut.warzone.util.parse;

import java.text.DecimalFormat;

/**
 * Created by luke on 10/19/15.
 */
public class TimeUtils {

    public static String formatToSeconds(double ticks) {
        DecimalFormat df = new DecimalFormat("0.0");
        double ticksPerSecond = 20;
        double d = (ticks / ticksPerSecond);

        return df.format(d);
    }

    public static String getDisplayTimeFromSeconds(int seconds) {
        if (seconds < 60) {
            return seconds + "";
        }
        else if (seconds < 60 * 60) {
            int minutes = seconds / 60;
            if (minutes == 1) {
                return minutes + " min";
            } else {
                return minutes + " mins";
            }
        }
        else {
            int hours = seconds / (60 * 60);
            if (hours == 1) {
                return hours + " hour";
            } else {
                return hours + " hours";
            }
        }
    }

    public static String formatToTimeFromMinutes(int i) {
        if (i == 1) {
            return i + " minute";
        } else if (i < 60) {
            return i + " minutes";
        } else if (i == 60) {
            return "1 hour";
        } else if (i % 60 == 0) {
            return (i / 60) + " hours";
        } else if (i > 60) {
            int hours = i / 60;
            int mins = i % 60;

            return hours + " hours and " + mins + " mins";
        } else {
            return "";
        }
    }

    public int formatToMinutesFromTime(String time) {
        if (time.contains("hours") && time.contains("mins")) {
            String[] split = time.replaceAll("[^A-Za-z0-9 ]","").split(" ");
            return (Integer.parseInt(split[0]) * 60) + (Integer.parseInt(split[1]));
        } else if (time.contains("hours")) {
            return Integer.parseInt(time.replace(" hours", ""));
        } else if (time.contains("minutes")) {
            return Integer.parseInt(time.replace(" minutes", ""));
        } else if (time.contains("hour")) {
            return 60;
        } else if (time.contains("minute")) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int timeStringToSeconds(String input) {
        if (input.equals("oo"))
            return (int) Double.POSITIVE_INFINITY;
        if (input.equals("-oo"))
            return (int) Double.NEGATIVE_INFINITY;
        int time = 0;
        String currentUnit = "";
        String current = "";
        boolean negative = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c) && !currentUnit.equals("")) {
                time += convert(Numbers.parseInt(current) * (negative ? -1 : 1), currentUnit);
                current = "";
                currentUnit = "";
            }
            if (c == '-') {
                negative = true;
            } else if (Character.isDigit(c)) {
                current += Numbers.parseInt(c + "");
            } else {
                currentUnit += c + "";
            }
        }
        time += convert(Numbers.parseInt(current) * (negative ? -1 : 1), currentUnit);
        return time;
    }

    private static int convert(int value, String unit) {
        switch (unit) {
            case "y":
                return value * 365 * 60 * 60 * 24;
            case "mo":
                return value * 31 * 60 * 60 * 24;
            case "d":
                return value * 60 * 60 * 24;
            case "h":
                return value * 60 * 60;
            case "m":
                return value * 60;
            case "s":
                return value;
        }
        return value;
    }

    //time is in seconds
    public static String formatTime(double time) {
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString;
    }

    public static String formatTimeWithMillis(double time) {
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        double millis = time - (hours * 3600) - (minutes * 60) - seconds;
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        String millisString = new DecimalFormat(".000").format(millis);
        millisString = millisString.substring(1);
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString + "." + millisString;
    }
}
