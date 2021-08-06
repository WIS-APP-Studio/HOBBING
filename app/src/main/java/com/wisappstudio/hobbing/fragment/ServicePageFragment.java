package com.wisappstudio.hobbing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hobbing.R;
import com.wisappstudio.hobbing.activity.InfoChangeActivity;
import com.wisappstudio.hobbing.activity.IntroActivity;
import com.wisappstudio.hobbing.activity.MainActivity;
import com.wisappstudio.hobbing.activity.ProfileSettingActivity;
import com.wisappstudio.hobbing.activity.TermsOfServiceActivity;
import com.wisappstudio.hobbing.activity.UserLogActivity;

public class ServicePageFragment extends Fragment implements AdapterView.OnItemClickListener {
    static final String[] LIST_MENU = {"도움말", "공지사항", "이용약관", "개인정보처리방침", "버전 정보", "문의"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.activity_service_page, container, false);

        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) view.findViewById(R.id.activity_service_page_list);
        listview.setAdapter(Adapter) ;
        listview.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0 : {
                break;
            }
            case 2 : {
                Intent intent = new Intent(view.getContext(), TermsOfServiceActivity.class);
                intent.putExtra("where", "Service");
                startActivity(intent);
                break;
            }
            case 3 : {
            }
        }
    }

}