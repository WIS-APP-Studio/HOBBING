package com.wisappstudio.hobbing.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_UPDATE_INTRODUCE_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_UPDATE_NICKNAME_URL;

public class ChangeProfileIntroduceDialog extends AppCompatActivity {
    private Context context;
    private String userID;

    public ChangeProfileIntroduceDialog(Context context, String userID) {
        this.userID = userID;
        this.context = context;
    }

    public void callFunction(final TextView main_label) {
        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_profile_introduce_change);
        dlg.show();

        final ImageView image = (ImageView) dlg.findViewById(R.id.dialog_profile_introduce_image);
        final TextView okButton = (TextView) dlg.findViewById(R.id.dialog_profile_introduce_OK_Button);

        RequestQueue queue = Volley.newRequestQueue(dlg.getContext());

        Glide.with(dlg.getContext())
                .load(IMAGE_DIRECTORY_URL+userID+".png") // 임시로 로드
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(image);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv_introduce = (TextView) dlg.findViewById(R.id.dialog_profile_introduce_description);
                String introduce = tv_introduce.getText().toString();

                if(! introduce.equals("")) {
                    // 게시물 내부 내용 로드
                    StringRequest changeNickname = new StringRequest(Request.Method.POST, PROFILE_UPDATE_INTRODUCE_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("VOLLEYLOG", response);
                            Toast.makeText(context,"자기소개를 변경했습니다.", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("DialogERR", error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", userID);
                            params.put("introduce", introduce);
                            return params;
                        }
                    };

                    queue.add(changeNickname);
                } else {
                    Toast.makeText(context, "닉네임을 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}