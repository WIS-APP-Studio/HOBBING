package com.wisappstudio.hobbing.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
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

}
