package com.amotte.mobiuqac;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
    private List<Evenement> evenementList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    FirebaseStorage storage;
    StorageReference storageReference;
    public MyRecyclerViewAdapter(Context context, List<Evenement> evenementList) {
        this.evenementList = evenementList;
        this.mContext = context;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_event, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Evenement evenement = evenementList.get(i);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (!TextUtils.isEmpty(evenement.getThumbnail())) {
            StorageReference ref = storageReference.child("images/"+ evenement.getThumbnail());
            GlideApp.with(mContext)
                    .load(ref)
                    .into(customViewHolder.imageView);
        }

        customViewHolder.title.setText(Html.fromHtml(evenement.getTitle()));
        Date d= evenement.getDate();
        customViewHolder.date.setText(new SimpleDateFormat("d MMMM - HH:mm").format(d));


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(evenement);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
        customViewHolder.title.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != evenementList ? evenementList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected TextView date;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
            this.date = view.findViewById(R.id.date);

        }
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}