package com.wisappstudio.hobbing.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hobbing.R;

public class ChangeProfileDialog {
    private Context context;

    public ChangeProfileDialog(Context context) {
        this.context = context;
    }

    public void callFunction(final TextView main_label) {

        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_profile_change);
        dlg.show();

        final TextView okButton = (TextView) dlg.findViewById(R.id.dialog_profile_OK_Button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"변경 되었습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
    }
}
