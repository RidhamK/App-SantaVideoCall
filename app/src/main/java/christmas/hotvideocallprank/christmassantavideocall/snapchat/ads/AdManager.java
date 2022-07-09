package christmas.hotvideocallprank.christmassantavideocall.snapchat.ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {
    public static String BANNER_ADS = "ca-app-pub-2639168308638517/2308711449";
    public static String INTERSTITIAL_ADS = "ca-app-pub-2639168308638517/6496913058";
    public static String REWARDED_ADS = "ca-app-pub-2639168308638517/5898147252";

    public final static String TEST_INTERSTITIAL_ADS = "ca-app-pub-3940256099942544/1033173712";
    public final static String TEST_REWARDED_ADS = "ca-app-pub-3940256099942544/5224354917";

    private static InterstitialAd mInterstitialAd;
    private static RewardedAd rewardedAd;

    static {
//        INTERSTITIAL_ADS = TEST_INTERSTITIAL_ADS;
//        REWARDED_ADS = TEST_REWARDED_ADS;
    }

    public static void loadInterstitialAds(final Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(INTERSTITIAL_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                // Code to be executed when the interstitial ad is closed.
            }
        });
    }

    public static void showInterstitialAds(Context context) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public static void loadRewardedAds(Context context) {
        rewardedAd = new RewardedAd(context, REWARDED_ADS);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    public static void showRewardedAds(Activity activity, final Context context) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    loadRewardedAds(context);
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                }
            };
            rewardedAd.show(activity, adCallback);
        }
    }
}
