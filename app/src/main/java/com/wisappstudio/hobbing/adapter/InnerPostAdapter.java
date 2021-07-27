package com.wisappstudio.hobbing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.InnerPostData;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.POST_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;
/*
 어댑터의 onCreateViewHolder()와 onBindViewHolder()
 메서드를 통해 각각 생성 및 바인딩(데이터 표시)되어 화면에 표시
 */

public class InnerPostAdapter extends RecyclerView.Adapter<InnerPostAdapter.ViewHolder> {

    ArrayList<InnerPostData> sample;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView = itemView.findViewById(R.id.list_inner_image_image);
        }
    }

    public InnerPostAdapter(ArrayList<InnerPostData> list){
        this.sample = list;
    }

    @Override
    public InnerPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_inner_image, parent, false);
        InnerPostAdapter.ViewHolder vh = new InnerPostAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public int getItemCount() {
        return sample.size();
    }

    @Override
    public void onBindViewHolder(InnerPostAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(POST_IMAGE_DIRECTORY
                        + sample.get(position).getNumber()+"/"
                        + sample.get(position).getImage())
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(holder.imageView);
    }
}