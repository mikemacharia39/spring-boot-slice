package com.mikehenry.springbootslice.util;

import java.util.concurrent.TimeUnit;

public class Benchmark {
    /**
     * @param processStartTime Processing start time
     * @return String formatted time
     */
    public static String getTAT(double processStartTime) {

        double processExecutionTime = (System.currentTimeMillis()
                - processStartTime);
        return " | TAT: " + getFriendlyTAT(processExecutionTime);
    }

    /**
     * Get formatted time
     * @param timeTaken current system time
     * @return String formatted time
     */
    public static String getFriendlyTAT(double timeTaken) {
        String result = "";

        long minutes = (long) (timeTaken / (1000 * 60));
        if (minutes < 1) {
            return (timeTaken / 1000) + " sec";
        }
        long day = TimeUnit.MINUTES.toDays(minutes);
        long hours = TimeUnit.MINUTES.toHours(minutes) - (day * 24);
        long minute = TimeUnit.MINUTES.toMinutes(minutes) - (TimeUnit.MINUTES.toHours(minutes) * 60);

        //subtract all full minutes and get the secs remaining
        double sec = (timeTaken - (minutes * 1000 * 60)) / 1000;

        if (day > 0) {
            result += day + " day";
            result += (day == 1) ? " " : "s ";
        }

        if (hours != 0) {
            result += hours + " hr";
            result += (hours == 1) ? " " : "s ";
        }

        if (minute != 0) {
            result += minute + " min";
            result += (minute == 1) ? " " : "s ";

        }

        if (sec != 0) {
            result += sec + " sec";
            result += (sec == 1) ? "" : "s";

        }
        return result;

    }
}
