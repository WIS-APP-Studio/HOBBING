package com.wisappstudio.hobbing.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbing.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.WRITE_POST_IMAGE_UPLOAD_URL;

public class WritePostActivity extends AppCompatActivity {
    final int CODE_GALLERY_REQUEST = 999;

    String userId;

    ImageView select;
    ImageView image1, image2, image3;
    boolean selected_image1, selected_image2, selected_image3;
    Bitmap bitmap1, bitmap2, bitmap3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");

        // 게시물 이미지 선택
        image1 = findViewById(R.id.activity_write_post_image1);
        image2 = findViewById(R.id.activity_write_post_image2);
        image3 = findViewById(R.id.activity_write_post_image3);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        WritePostActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
                select = image1;
                selected_image1 = true;
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        WritePostActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
                select = image2;
                selected_image2 = true;
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        WritePostActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
                select = image3;
                selected_image3 = true;
            }
        });
        // 게시물 이미지 선택

        // 게시물 작성 및 취소
        TextView upload = (TextView) findViewById(R.id.activity_write_post_upload);
        TextView cancel = (TextView) findViewById(R.id.activity_write_post_cancel);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // post image to server
                StringRequest stringRequest = new StringRequest(Request.Method.POST, WRITE_POST_IMAGE_UPLOAD_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WRITEPOST", response);
                        Toast.makeText(WritePostActivity.this, "게시물 업로드 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WRITEPOST", error.getMessage());
                        Toast.makeText(getApplicationContext(), "ERROR : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        // 게시물 요소 변수
                        EditText title = findViewById(R.id.activity_write_post_title);
                        EditText description = findViewById(R.id.activity_write_post_description);
                        EditText category = findViewById(R.id.activity_write_post_category);
                        EditText hashtag = findViewById(R.id.activity_write_post_hashtag);

                        params.put("writer", userId);
                        params.put("title", String.valueOf(title.getText()));
                        params.put("description", String.valueOf(description.getText()));
                        params.put("category", String.valueOf(category.getText()));
                        params.put("hashtag", String.valueOf(hashtag.getText()));
                        // 게시물 요소 변수

                        if(selected_image1 == true) {
                            params.put("image1", imageToString(bitmap1));
                        }
                        if(selected_image2 == true) {
                            params.put("image2", imageToString(bitmap2));
                        }
                        if(selected_image3 == true) {
                            params.put("image3", imageToString(bitmap3));
                        }

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(WritePostActivity.this);
                requestQueue.add(stringRequest);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 게시물 작성 및 취소
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시물 작성 취소");
        builder.setMessage("작성중이던 게시물을 취소하시겠습니까?\r\n취소 시 작성된 내용은 저장되지 않습니다.");
        builder.setPositiveButton("작성 취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"작성을 취소했습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.setNegativeButton("계속 쓰기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }
                });

        builder.show();
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

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);

                if(select == image1) {
                    bitmap1 = BitmapFactory.decodeStream(inputStream);
                    select.setImageBitmap(bitmap1);
                }
                if(select == image2) {
                    bitmap2 = BitmapFactory.decodeStream(inputStream);
                    select.setImageBitmap(bitmap2);
                }
                if(select == image3) {
                    bitmap3 = BitmapFactory.decodeStream(inputStream);
                    select.setImageBitmap(bitmap3);
                }
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
}
