package com.chat.ChatThread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chat.ChatThread.model.DataItem;
import com.chat.R;
import com.chat.home.model.ImageModel.ImageProfileResponse;
import com.chat.network.DevartlinkAPI;
import com.chat.utils.UserPreferenceHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    List<DataItem> messages;
    private Context context;

    public ChatListAdapter(List<DataItem> messages) {
        this.messages = messages;
    }

    private final static int INCOMING = 1;
    private final static int OUTGOING = 2;

    @Override
    public int getItemViewType(int position) {
        DataItem message = messages.get(position);
        if (message.getUserId().equals(UserPreferenceHelper.getUser().getId()))
            return OUTGOING;
        return INCOMING;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == INCOMING) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item_message_incoming, parent, false);
        } else if (viewType == OUTGOING) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item_message_outgoing, parent, false);

        }
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final DataItem dataItem = messages.get(position);

        viewHolder.content.setText(dataItem.getMessage());
        convertDateTime(dataItem.getUpdatedAt(), viewHolder.time);
        if (!dataItem.getUserId().equals(UserPreferenceHelper.getUser().getId())){
            viewHolder.sender_name.setText(dataItem.getUserapi().getName());

            DevartlinkAPI.getApis().getImageProfile(dataItem.getUserapi().getId())
                    .enqueue(new Callback<ImageProfileResponse>() {
                        @Override
                        public void onResponse(Call<ImageProfileResponse> call, Response<ImageProfileResponse> response) {
                            if (response.isSuccessful()) {
                                String base64String = response.body().getImg();
                                String base64Image = base64String.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString
                                        , 0, decodedString.length);
                                viewHolder.img_sender.setImageBitmap(decodedByte);
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageProfileResponse> call, Throwable t) {
                            //   errorMessage.postValue(1);
                        }
                    });
        }
        if (dataItem.getUserId().equals(UserPreferenceHelper.getUser().getId())){
            if (dataItem.getSeen().equals("0")){
                viewHolder.seen.setImageResource(R.drawable.unseen);
            }else {
                viewHolder.seen.setImageResource(R.drawable.seen);
            }

            DevartlinkAPI.getApis().getImageProfile(dataItem.getUserapi().getId())
                    .enqueue(new Callback<ImageProfileResponse>() {
                        @Override
                        public void onResponse(Call<ImageProfileResponse> call, Response<ImageProfileResponse> response) {
                            if (response.isSuccessful()) {
                                String base64String = response.body().getImg();
                                String base64Image = base64String.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString
                                        , 0, decodedString.length);
                                viewHolder.img_user.setImageBitmap(decodedByte);
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageProfileResponse> call, Throwable t) {
                            //   errorMessage.postValue(1);
                        }
                    });
        }
    }


    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView content;
        public TextView time;
        public TextView sender_name;
        public ImageView img_user;
        public ImageView img_sender;
        public ImageView seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            sender_name = itemView.findViewById(R.id.sender_name);
            img_user = itemView.findViewById(R.id.img_user);
            img_sender = itemView.findViewById(R.id.img_sender);
            seen = itemView.findViewById(R.id.seen);
        }
    }

    public void convertDateTime(String format, TextView date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        Date d = null;
        try {
            d = input.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        date.setText(formatted);
    }

}
