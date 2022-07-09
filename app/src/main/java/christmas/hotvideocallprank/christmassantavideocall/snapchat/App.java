package christmas.hotvideocallprank.christmassantavideocall.snapchat;

import android.app.Application;
import android.text.TextUtils;

import com.hotvideocallprank.christmassantavideocall.R;

import christmas.hotvideocallprank.christmassantavideocall.snapchat.ads.AdManager;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.model.VideoAttrs;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.util.StorageManager;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import org.json.JSONArray;

public class App extends Application {

    private StorageManager storageManager;

    @Override
    public void onCreate() {
        super.onCreate();
        storageManager = new StorageManager(this);
        initializeOneTimeDefaultVideoCallList();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdManager.loadInterstitialAds(getApplicationContext());
        AdManager.loadRewardedAds(getApplicationContext());
    }

    private void initializeOneTimeDefaultVideoCallList() {
        if (TextUtils.isEmpty(StorageManager.getVideoCallList())) {
            JSONArray jsonArray = new JSONArray();
            VideoAttrs videoAttrs = new VideoAttrs();
            videoAttrs.setName("Mr. SANTA CLAUS");
            videoAttrs.setNumber("123456789");
            videoAttrs.setVideoName("one");
            videoAttrs.setUri("android.resource://" + getPackageName() + "/" + R.raw.santacall);
            videoAttrs.setInternal(true);
            jsonArray.put(new Gson().toJson(videoAttrs));

            videoAttrs = new VideoAttrs();
            videoAttrs.setName("Your SANTA");
            videoAttrs.setNumber("123456789");
            videoAttrs.setVideoName("one");
            videoAttrs.setUri("android.resource://" + getPackageName() + "/" + R.raw.call2);
            videoAttrs.setInternal(true);
            jsonArray.put(new Gson().toJson(videoAttrs));

            storageManager.setVideoCallList(jsonArray.toString());
        }
    }
}
