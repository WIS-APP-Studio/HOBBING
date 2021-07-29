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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hobbing.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WritePostActivity extends AppCompatActivity {
    final int CODE_GALLERY_REQUEST = 999;

    ImageView select;
    ImageView image1, image2, image3;
    boolean selected_image1, selected_image2, selected_image3;
    Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

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
                Toast.makeText(WritePostActivity.this, "게시물 업로드", Toast.LENGTH_SHORT).show();
                finish();
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
                bitmap = BitmapFactory.decodeStream(inputStream);
                select.setImageBitmap(bitmap);
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
        return  encodedImage;
    }
}
