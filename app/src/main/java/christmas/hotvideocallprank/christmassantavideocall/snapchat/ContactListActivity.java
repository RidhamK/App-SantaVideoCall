package christmas.hotvideocallprank.christmassantavideocall.snapchat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.hotvideocallprank.christmassantavideocall.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import christmas.hotvideocallprank.christmassantavideocall.snapchat.adapter.ContactListAdapter;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.ads.AdManager;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.model.VideoAttrs;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.util.StorageManager;

public class ContactListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private final int REQUEST_GALLERY_VIDEO = 100;
    public static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showBannerAds();
        createView();
        setAdapter();
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    private void showBannerAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void createView() {
        recyclerView = findViewById(R.id.recycler_view);
        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }

    private String videoPathString;
    private TextView videoPath;
    private Uri videoPathUri;

    private void browseVideo() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_VIDEO) {
                if (data != null) {
                    videoPathUri = data.getData();
                    videoPathString = getPath(videoPathUri);
                    setPathToDialog(videoPathString);
                    Log.d("path", videoPathString);
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private ContactListAdapter adapter;

    private void setAdapter() {
        try {
            List<VideoAttrs> videoAttrsList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(StorageManager.getVideoCallList());

            for (int i = 0; i < jsonArray.length(); i++) {
                videoAttrsList.add(new Gson().fromJson(jsonArray.get(i).toString(), VideoAttrs.class));
            }

            adapter = new ContactListAdapter(videoAttrsList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
        }
    }

    private void showCustomDialog() {
        AdManager.showInterstitialAds(getApplicationContext());
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_video_dialog);

        videoPath = dialog.findViewById(R.id.video_path);
        Button browse = dialog.findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseVideo();
            }
        });

        final EditText contactName = dialog.findViewById(R.id.contact_name);
        final EditText contactNumber = dialog.findViewById(R.id.contact_number);

        Button ok = dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(videoPathString)) {
                    Toast.makeText(getApplicationContext(), "Please choose video first.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(contactName.getText().toString()) || TextUtils.isEmpty(contactNumber.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter contact Name and Number.", Toast.LENGTH_SHORT).show();
                } else {
                    if (videoPathUri != null) {
                        VideoAttrs videoAttrs = new VideoAttrs();
                        videoAttrs.setUri(videoPathUri.toString());
                        videoAttrs.setName(contactName.getText().toString());
                        videoAttrs.setNumber(contactNumber.getText().toString());
                        videoAttrs.setInternal(false);
                        videoAttrs.setVideoPath(videoPathString);

                        try {
                            JSONArray jsonArray = new JSONArray(StorageManager.getVideoCallList());
                            videoAttrs.setVideoName("" + jsonArray.length());
                            jsonArray.put(new Gson().toJson(videoAttrs));
                            StorageManager.setVideoCallList(jsonArray.toString());

                        } catch (Exception e) {
                            Log.d("e", "ee");
                        }

                        if (adapter != null) {
                            adapter.addVideoAttrs(videoAttrs);
                        }
                    }
                    dialog.dismiss();
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

    private void setPathToDialog(String path) {
        videoPath.setText(path);
    }

    public void checkPermission() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
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

    // This function is called when user accept or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // Checking whether user granted the permission or not.
//        for (int i = 0; i < permissions.length; i++) {
//            if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//            } else {
//                String deniedPermission = "";
//                if (requestCode == 100)
//                    deniedPermission = "Camera";
//                else if (requestCode == 101)
//                    deniedPermission = "Read external storage";
//
//                Toast.makeText(MainActivity.this, deniedPermission + " Permission Denied", Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
