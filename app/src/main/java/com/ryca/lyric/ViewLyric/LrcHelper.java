package com.ryca.lyric.ViewLyric;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcHelper {

    private static final String CHARSET = "utf-8";
    private static final String LINE_REGEX = "((\\[\\d{2}:\\d{2}\\.\\d{2}])+)(.*)";
    private static final String TIME_REGEX = "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]";

    public static List<Lrc> parseLrcFromAssets(Context context, String fileName) {
        try {
            return parseInputStream(context.getResources().getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Lrc> parseLrcFromFile(File file) {
        try {
            return parseInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Lrc> parseInputStream(InputStream inputStream) {
        List<Lrc> lrcs = new ArrayList<>();
        InputStreamReader isr = null;
        BufferedReader br = null;
        int i = 1;
        try {
            isr = new InputStreamReader(inputStream, CHARSET);
            br = new BufferedReader(isr);
            String line;
            long lrcTime = 0;
            try {
                while ((line = br.readLine()) != null) {
//                    if (i > 8)
//                        line = line + br.readLine();
                    List<Lrc> lrcList = parseLrc(line, lrcTime);
                    if (lrcList != null && lrcList.size() != 0) {
                        lrcTime = lrcList.get(0).getTime();
                        lrcs.addAll(lrcList);
                    }
                    ++i;
                }
            } catch (Exception ignored) {
                Log.e(">> MSG: ", ignored.getMessage() + " ");
            }
//            sortLrcs(lrcs);
            return sortLrcs(lrcs);
        } catch (UnsupportedEncodingException ignored) {
            Log.e(">> LRC2: ", ignored.getMessage() + " ");
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(">> LRC3: ", e1.getMessage() + " ");
            }
        }
        return lrcs;
    }

    private static List<Lrc> sortLrcs(List<Lrc> lrcs) {
        Log.e(">> LRC-SORT: ", lrcs.size() + " ");
        List<Lrc> lrcList = new ArrayList<>();
        for (int i = 3; i < lrcs.size(); i += 2) {
            Lrc lrc = new Lrc();
            lrc.setText(lrcs.get(i).getText() + "\n" + lrcs.get(i + 1).getText());
            lrc.setTime(lrcs.get(i).getTime());
            lrcList.add(lrc);

        }
        Collections.sort(lrcList, (o1, o2) -> (int) (o1.getTime() - o2.getTime()));
        return lrcList;
    }

    private static List<Lrc> parseLrc(String lrcLine, long lrcTime) {
        if (lrcLine.trim().isEmpty()) {
            return null;
        }
        List<Lrc> lrcs = new ArrayList<>();
        Matcher matcher = Pattern.compile(LINE_REGEX).matcher(lrcLine);
        if (!matcher.matches()) {
            Lrc lrc = new Lrc();
            lrc.setTime(lrcTime);
            lrc.setText(lrcLine);
            lrcs.add(lrc);
            return lrcs;
        }
        String time = matcher.group(1);
        String content = matcher.group(3);
        Matcher timeMatcher = Pattern.compile(TIME_REGEX).matcher(time);

        while (timeMatcher.find()) {
            String min = timeMatcher.group(1);
            String sec = timeMatcher.group(2);
            String mil = timeMatcher.group(3);
            Lrc lrc = new Lrc();
            if (content != null && content.length() != 0) {
                lrc.setTime(Long.parseLong(min) * 60 * 1000 + Long.parseLong(sec) * 1000
                        + Long.parseLong(mil) * 10);
                lrc.setText(content);
                lrcs.add(lrc);
            }
        }

        return lrcs;
    }

    public static String formatTime(long time) {
        int min = (int) (time / 60000);
        int sec = (int) (time / 1000 % 60);
        return adjustFormat(min) + ":" + adjustFormat(sec);
    }

    private static String adjustFormat(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return time + "";
    }
}