package christmas.hotvideocallprank.christmassantavideocall.snapchat.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class StorageManager {
    private static SharedPreferences sharedpreferences;
    private static SharedPreferences.Editor editor;

    public StorageManager(Context context) {
        sharedpreferences = context.getSharedPreferences("VideoCallPrank", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public static void setVideoCallList(String jsonVideoCallList) {
        editor.putString("jsonVideoCallList", jsonVideoCallList);
        editor.commit();
    }

    public static String getVideoCallList() {
        return sharedpreferences.getString("jsonVideoCallList", "");
    }

    public static void setAdCount(int adCount) {
        editor.putInt("adCount", adCount);
        editor.commit();
    }

    public static int getAdCount() {
        return sharedpreferences.getInt("adCount", 1);
    }

    public static void setDay(int day) {
        editor.putInt("day", day);
        editor.commit();
    }

    public static int getDay() {
        return sharedpreferences.getInt("day", -1);
    }

    public static void setMonth(int month) {
        editor.putInt("month", month);
        editor.commit();
    }

    public static int getMonth() {
        return sharedpreferences.getInt("month", -1);
    }

//    public static boolean isShowBannerAd() {
//        int storedDay = StorageManager.getDay();
//        int storedMonth = StorageManager.getMonth();
//        Calendar calendar = Calendar.getInstance();
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//        if (storedDay == -1 && storedMonth == -1) {
//            StorageManager.setMonth(currentMonth);
//            StorageManager.setDay(currentDay);
//        }
//
//        if (StorageManager.getMonth() > 0 && currentMonth > StorageManager.getMonth()) {
//            return true;
//        } else if (currentDay > (StorageManager.getDay() + 2) && StorageManager.getMonth() > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

}

