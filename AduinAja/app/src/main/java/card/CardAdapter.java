package card;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aduinaja.aduinaja.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.aduinaja.application.MainApplication;

/**
 * Created by elmee on 05/11/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.DataObjectHolder> {
    //List<DataLaporan> mItems;
    private ArrayList<DataLaporan> mDataset;

    public CardAdapter(ArrayList<DataLaporan> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder viewHolder, int position) {
        //ImageLoader imageLoader = MainApplication.getInstance().getImageLoader();
        viewHolder.txtTitle.setText(mDataset.get(position).getTitle());
        viewHolder.txtWaktu.setText(mDataset.get(position).getWaktu());
        //viewHolder.imgPost.setImageUrl(MainApplication.urlGetImages + mDataset.get(position).getImg(), imageLoader);
        Picasso.with(mDataset.get(position).getContext())
                .load(mDataset.get(position).getImgAvatar())
                .resize(360,540)
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(mDataset.get(position).getContext(), R.drawable.pp))
                .into(viewHolder.imgAvatar);
        Picasso.with(mDataset.get(position).getContext())
                .load(MainApplication.urlGetImages + mDataset.get(position).getImgPost())
                .resize(360,540)
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(mDataset.get(position).getContext(), R.drawable.img_post))
                .into(viewHolder.imgPost);
        switch (mDataset.get(position).getStatus()){
            case "1":
                viewHolder.txtStatus.setText("Diterima");
                viewHolder.linear.setBackgroundColor(Color.parseColor("#FFDDB004"));
                break;
            case "2":
                viewHolder.txtStatus.setText("Ditindaklanjuti");
                viewHolder.linear.setBackgroundColor(Color.parseColor("#FF009ADA"));
                break;
            case "3":
                viewHolder.txtStatus.setText("Ditolak");
                viewHolder.linear.setBackgroundColor(Color.parseColor("#FFCF4038"));
                break;
        }
        viewHolder.txtStatus.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.txtNama.setText(mDataset.get(position).getNama());
        viewHolder.up_count.setText(mDataset.get(position).getUpVote());
        viewHolder.down_count.setText(mDataset.get(position).getDownVote());
        viewHolder.comment_count.setText(mDataset.get(position).getComments());

    }

    public void addItem(DataLaporan dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        public ImageView imgAvatar;
        public TextView txtNama;
        public ImageView imgPost;
        public TextView txtTitle;
        public TextView txtWaktu;
        public TextView txtStatus;
        public LinearLayout linear;
        public LinearLayout LUp;
        public LinearLayout LDown;
        public LinearLayout LComment;
        public TextView up_count;
        public TextView down_count;
        public TextView comment_count;


        public DataObjectHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView)itemView.findViewById(R.id.imgAvatar);
            txtNama = (TextView)itemView.findViewById(R.id.txtNama);
            txtWaktu = (TextView)itemView.findViewById(R.id.txtWaktu);
            imgPost = (ImageView)itemView.findViewById(R.id.imgPost);
            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtStatus = (TextView)itemView.findViewById(R.id.txtStatus);
            linear = (LinearLayout)itemView.findViewById(R.id.linear_status);
            LUp = (LinearLayout)itemView.findViewById(R.id.linear_up);
            LDown = (LinearLayout)itemView.findViewById(R.id.linear_down);
            LComment = (LinearLayout)itemView.findViewById(R.id.linear_comment);
            up_count = (TextView)itemView.findViewById(R.id.up_count);
            down_count = (TextView)itemView.findViewById(R.id.down_count);
            comment_count = (TextView)itemView.findViewById(R.id.comment_count);
            Log.i(MainApplication.TAG, "Adding Listener");
        }
    }

}
