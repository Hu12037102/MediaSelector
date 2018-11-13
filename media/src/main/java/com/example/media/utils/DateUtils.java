package com.example.media.utils;

public class DateUtils {
    public static String videoDuration(long videoDuration) {
        StringBuilder sb = new StringBuilder();
        if (videoDuration >= 1000) {
            int second = (int) (videoDuration / 1000);
            if (second / 60 >= 1) {
                int minute = second / 60;
                int remainderSecond = second % 60;
                sb.append(minute).append(remainderSecond >= 10 ? ":" + remainderSecond : ":0" + remainderSecond);
            } else {
                if (second >= 10) {
                    sb.append("0:").append(second);
                } else {
                    sb.append("0:0").append(second);
                }
            }

        } else {
            sb.append("0:01");
        }
        return sb.toString();

    }
}
