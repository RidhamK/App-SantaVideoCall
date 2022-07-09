package christmas.hotvideocallprank.christmassantavideocall.snapchat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hotvideocallprank.christmassantavideocall.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import christmas.hotvideocallprank.christmassantavideocall.snapchat.ads.AFLayout;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.ads.AdManager;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.model.VideoAttrs;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.util.ForegroundService;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.util.StorageManager;

public class ContactDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name, phoneNo;
    private ImageView thumbNail;
    private ImageButton editPhone, editName;
    private VideoAttrs videoAttrs;
    private Button callNow, callIn5Sec, callIn10Sec, callIn1Min, callIn5Min, setCallBackTime;
    private StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        String item = getIntent().getExtras().getString("item");
        videoAttrs = new Gson().fromJson(item, VideoAttrs.class);
        storageManager = new StorageManager(this);
        createView();
        setValue();
        showBannerAds();
        setUpAFAd();
        if (StorageManager.getAdCount() % 3 == 0) {
            StorageManager.setAdCount(1);
            AdManager.showInterstitialAds(getApplicationContext());
        } else {
            StorageManager.setAdCount(StorageManager.getAdCount() + 1);
        }
    }

    private void createView() {
        name = findViewById(R.id.name);
        phoneNo = findViewById(R.id.phone_no);
        thumbNail = findViewById(R.id.thumbnail);
        callNow = findViewById(R.id.call_now);
        callIn5Sec = findViewById(R.id.sec_5_btn);
        callIn10Sec = findViewById(R.id.sec_10_btn);
        callIn1Min = findViewById(R.id.min_1_btn);
        callIn5Min = findViewById(R.id.min_5_btn);
        setCallBackTime = findViewById(R.id.set_time);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone_no);
        editName.setOnClickListener(this);
        editPhone.setOnClickListener(this);
        callIn5Sec.setOnClickListener(this);
        callIn10Sec.setOnClickListener(this);
        callIn1Min.setOnClickListener(this);
        callIn5Min.setOnClickListener(this);
        setCallBackTime.setOnClickListener(this);

        callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermissions(ContactDetailsActivity.this, ContactListActivity.PERMISSIONS)) {
                    Intent intent = new Intent(ContactDetailsActivity.this, VideoCallActivity.class);
                    intent.putExtra("item", new Gson().toJson(videoAttrs));
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(ContactDetailsActivity.this, ContactListActivity.PERMISSIONS, 1);
                }
            }
        });
    }

    private void showBannerAds() {
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }

    private void setUpAFAd() {
        final String packageId = "funnytom.talkingcat.birthdaywishing.talkingtomvideocall";
        AFLayout afLayout = findViewById(R.id.af_container);
        afLayout.install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageId)));
                }
            }
        });
        afLayout.afParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageId)));
                }
            }
        });
        afLayout.getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + "4K+Video+Wallpaper,+Live+Wallpaper,+Apps+and+Games")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + "4K+Video+Wallpaper,+Live+Wallpaper,+Apps+and+Games")));
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void setValue() {
        name.setText(videoAttrs.getName());
        phoneNo.setText(videoAttrs.getNumber());
        //Glide.with(getApplicationContext()).load(Uri.parse(videoAttrs.getUri())).diskCacheStrategy(DiskCacheStrategy.NONE).into(thumbNail);
        Glide.with(getApplicationContext()).load(videoAttrs.getUri()).into(thumbNail);
    }

    private void updateNameNo() {
        name.setText(videoAttrs.getName());
        phoneNo.setText(videoAttrs.getNumber());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNameNo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // for each permission check if the user granted/denied them
        // you may want to group the rationale in a single dialog,
        // this is just an example
        String permissionsText = "";
        boolean showRationale = false;
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        if (permissions[i].equals(Manifest.permission.CAMERA)) {
                            permissionsText += ", Camera permission";
                        } else if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            permissionsText += ", Read external storage permission";
                        }
                    }
                }
            }
        }

        if (!showRationale) {
            Toast.makeText(getApplicationContext(), "You have denied" + permissionsText + ". Please enable it from Settings to access this feature.", Toast.LENGTH_LONG).show();
        }
    }

    public void startService(int secs) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        serviceIntent.putExtra("item", new Gson().toJson(videoAttrs));
        serviceIntent.putExtra("time", secs);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onClick(View view) {
        if (view == editName || view == editPhone) {
            showCustomDialog();
        } else if (view == callIn5Sec) {
            startService(5);
        } else if (view == callIn10Sec) {
            startService(10);
        } else if (view == callIn1Min) {
            startService(60);
        } else if (view == callIn5Min) {
            startService(300);
        }
    }

    private Data createData(int seconds) {
        return new Data.Builder().putString("item", new Gson().toJson(videoAttrs)).putInt("time", seconds).build();
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_name_no);

        final EditText contactName = dialog.findViewById(R.id.contact_name);
        final EditText contactNumber = dialog.findViewById(R.id.contact_number);
        contactName.setText(videoAttrs.getName());
        contactNumber.setText(videoAttrs.getNumber());
        Button save = dialog.findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<VideoAttrs> videoAttrsList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(StorageManager.getVideoCallList());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        videoAttrsList.add(new Gson().fromJson(jsonArray.get(i).toString(), VideoAttrs.class));
                    }

                    for (int i = 0; i < videoAttrsList.size(); i++) {
                        if (videoAttrsList.get(i).getVideoName().equals(videoAttrs.getVideoName())) {
                            videoAttrsList.get(i).setName(contactName.getText().toString());
                            videoAttrsList.get(i).setNumber(contactNumber.getText().toString());
                            videoAttrs.setName(contactName.getText().toString());
                            videoAttrs.setNumber(contactNumber.getText().toString());
                            break;
                        }
                    }

                    jsonArray = new JSONArray();
                    for (int i = 0; i < videoAttrsList.size(); i++) {
                        jsonArray.put(new Gson().toJson(videoAttrsList.get(i)));
                    }

                    storageManager.setVideoCallList(jsonArray.toString());
                    updateNameNo();
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.d("d", "dd");
                }
            }
        });

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
