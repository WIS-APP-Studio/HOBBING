package com.wisappstudio.hobbing.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;

public class ChangeProfileDialog {
    private Context context;
    private String userID;

    public ChangeProfileDialog(Context context, String userID) {
        this.userID = userID;
        this.context = context;
    }

    public void callFunction(final TextView main_label) {

        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_profile_change);
        dlg.show();

        final ImageView image = (ImageView) dlg.findViewById(R.id.dialog_profile_image);
        final TextView okButton = (TextView) dlg.findViewById(R.id.dialog_profile_OK_Button);

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
                Toast.makeText(context,"변경 되었습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
    }
}
