package christmas.hotvideocallprank.christmassantavideocall.snapchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.ContactDetailsActivity;
import com.hotvideocallprank.christmassantavideocall.R;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.model.VideoAttrs;
import com.google.gson.Gson;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private List<VideoAttrs> videoAttrsList;
    private Context context;

    public ContactListAdapter(List<VideoAttrs> videoAttrsList, Context context) {
        this.videoAttrsList = videoAttrsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_call, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VideoAttrs videoAttrs = videoAttrsList.get(position);
        holder.name.setText(videoAttrs.getName());
        holder.number.setText(videoAttrs.getNumber());
        //Glide.with(context).load(Uri.parse(videoAttrs.getUri())).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.thumbNail);
        Glide.with(context).load(videoAttrs.getUri()).into(holder.thumbNail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactDetailsActivity.class);
                intent.putExtra("item", new Gson().toJson(videoAttrsList.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoAttrsList.size();
    }

    public void addVideoAttrs(VideoAttrs videoAttrs) {
        if (videoAttrsList != null) {
            videoAttrsList.add(videoAttrs);
            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbNail;
        public TextView name, number;
        public CardView listItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbNail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            listItem = itemView.findViewById(R.id.item);
        }
    }
}
