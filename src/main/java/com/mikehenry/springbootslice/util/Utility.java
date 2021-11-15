package com.mikehenry.springbootslice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public String getCurrentTime() {
        Date showDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(showDate);
    }
}
