package com.wisappstudio.hobbing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hobbing.R;
import com.wisappstudio.hobbing.activity.ProfileSettingActivity;

public class ServicePageFragment extends Fragment {
    static final String[] LIST_MENU = {"내 정보", "알림 설정", "활동 기록", "로그아웃", "탈퇴"} ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.activity_profile_setting, null);

        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) rootView.findViewById(R.id.listview1) ;
        listview.setAdapter(Adapter) ;

        return inflater.inflate(R.layout.activity_profile_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}