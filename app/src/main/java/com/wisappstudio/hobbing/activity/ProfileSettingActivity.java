package com.wisappstudio.hobbing.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.dialog.ChangeProfileDialog;
import com.wisappstudio.hobbing.dialog.ChangeProfileIntroduceDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_UPLOAD_IMAGE_URL;

public class ProfileSettingActivity extends Activity implements AdapterView.OnItemClickListener {
    static final String[] LIST_MENU = {"계정 정보 수정", "알림 설정", "활동 기록", "서비스", "로그아웃", "탈퇴"} ;
    final int CODE_GALLERY_REQUEST = 999;
    ImageView image, uploadImage;
    TextView tv_username, tv_introduce;
    String USER_ID;
    Bitmap bitmapImage;
    RequestQueue queue;
    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        activity = ProfileSettingActivity.this;

        ArrayAdapter Adapter = new ArrayAdapter(ProfileSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU);

        queue = Volley.newRequestQueue(getApplicationContext());
        USER_ID = getIntent().getStringExtra("user_id");
        image = (ImageView) findViewById(R.id.activity_profile_setting_image);
        uploadImage = (ImageView) findViewById(R.id.activity_profile_setting_change_image);
        tv_introduce = (TextView) findViewById(R.id.activity_profile_setting_introduce);

        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(Adapter) ;
        listview.setOnItemClickListener(this);


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        backToMyPage();

        clickChangeNickname();
        clickChangeIntroduce();
        clickChangeProfileImage();

        queue.add(loadUserProfile());
        loadUserProfileImage();
    }

    private void clickChangeProfileImage() {
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ProfileSettingActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );

            }
        });
    }

    private void loadUserProfileImage() {
        Glide.with(getApplicationContext())
                .load(PROFILE_IMAGE_DIRECTORY+USER_ID+".png") // 임시로 로드
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(image);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        ShapeDrawable uploadDrawable = new ShapeDrawable();

        shapeDrawable.getPaint().setColor(getApplicationContext().getColor(R.color.signature)); // 이미지 백그라운드 컬러
        uploadDrawable.getPaint().setColor(getApplicationContext().getColor(R.color.upload_image));
        shapeDrawable.setShape(new OvalShape());
        uploadDrawable.setShape(new OvalShape());

        uploadImage.setBackground(uploadDrawable);
        uploadImage.setClipToOutline(true);
        image.setBackground(shapeDrawable);
        image.setClipToOutline(true);
    }

     public void loadProfileDescription() {
        StringRequest profileRequest = new StringRequest(Request.Method.POST, PROFILE_READ_NICKNAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String TAG_JSON = "프로필";
                    String NICKNAME = "닉네임";
                    String INTRODUCE = "자기소개";
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                        JSONObject item = jsonArray.getJSONObject(0);
                        String nickname = item.getString(NICKNAME);
                        String introduce = item.getString(INTRODUCE);

                        tv_username.setText(nickname);
                        tv_introduce.setText(introduce);
                    } catch (JSONException e) { }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id", USER_ID);
                return params;
            }
        };
        queue.add(profileRequest);
    }

    private void clickChangeIntroduce() {
        ImageView changeIntroduce = (ImageView) findViewById(R.id.activity_profile_setting_change_introduce);

        tv_introduce.setText(getIntent().getStringExtra("introduce"));

        changeIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfileIntroduceDialog customDialog = new ChangeProfileIntroduceDialog(ProfileSettingActivity.this, USER_ID);
                customDialog.callFunction(null);
            }
        });
    }

    private void clickChangeNickname() {
        ImageView changeNickname = (ImageView) findViewById(R.id.activity_profile_setting_change_profile);
        changeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfileDialog customDialog = new ChangeProfileDialog(ProfileSettingActivity.this, USER_ID);
                customDialog.callFunction(null);
            }
        });
    }

    private void backToMyPage() {
        TextView back = (TextView) findViewById(R.id.activity_profile_setting_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private StringRequest loadUserProfile() {
        TextView userId = (TextView) findViewById(R.id.activity_profile_setting_userid);
        tv_username = (TextView) findViewById(R.id.activity_profile_setting_username);

        userId.setText(USER_ID);
        StringRequest nicknameRequest = new StringRequest(Request.Method.POST, PROFILE_READ_NICKNAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String TAG_JSON = "프로필";
                    String NICKNAME = "닉네임";
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                        JSONObject item = jsonArray.getJSONObject(0);
                        String nickname = item.getString(NICKNAME);

                        tv_username.setText(nickname);
                    } catch (JSONException e) { }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id", USER_ID);
                return params;
            }
        };
        return nicknameRequest;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0 : {
                Intent intent = new Intent(this, InfoChangeActivity.class);
                intent.putExtra("user_id", USER_ID);
                startActivity(intent);
                break;
            }
            case 1 : {
                Intent intent = new Intent(this, NotificationSettingActivity.class);
                startActivity(intent);
                break;
            }
            case 2 : {
                Intent intent = new Intent(this, UserLogActivity.class);
                startActivity(intent);
                break;
            }
            case 3 : {
                Intent intent = new Intent(this, ServiceActivity.class);
                startActivity(intent);
                break;
            }
            case 4 : {
                MainActivity mainActivity = (MainActivity) MainActivity.activity;
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                
                // 자동 로그인 초기화
                clearAutoLogin();

                finish();
                mainActivity.finish();
                break;
            }
            case 5 : {
                Intent intent = new Intent(this, DeleteAccountActivity.class);
                intent.putExtra("user_id", USER_ID);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    private void clearAutoLogin() {
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = auto.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "이미지 선택"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "겔러리 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();
            int degree = getExifOrientation(uriToPath(getApplicationContext(),filePath));
            Log.d("IMAGEROTATE", degree+"도, " +uriToPath(getApplicationContext(),filePath));
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmapImage = getRotatedBitmap(BitmapFactory.decodeStream(inputStream), degree);
                image.setImageBitmap(bitmapImage);

                StringRequest uploadImage = new StringRequest(Request.Method.POST, PROFILE_UPLOAD_IMAGE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VOLLEYLOG", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYLOG", error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("image", imageToString(bitmapImage));
                        params.put("user_id", getIntent().getStringExtra("user_id"));
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(uploadImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private synchronized static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        }
        catch (IOException e) {
            Log.d("Exif", "cannot exit");
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if(orientation != -1) {
                switch (orientation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }

        return degree;
    }

    public synchronized static Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != b2)
                {
                    bitmap.recycle();
                    bitmap = b2;
                }
            }
            catch (OutOfMemoryError e) {

            }
        }
        return bitmap;
    }

    private String uriToPath(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));

        cursor.close();
        return path;
    }
}
