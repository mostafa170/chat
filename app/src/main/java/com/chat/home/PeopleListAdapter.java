package com.chat.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.chat.R;
import com.chat.databinding.ItemChatUserBinding;
import com.chat.home.model.DataItem;
import com.chat.utils.UserPreferenceHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.ViewHolder> {

    List<DataItem> dataItems;
    private Context context;

    public PeopleListAdapter(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_user, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final DataItem dataItem = dataItems.get(position);

        if (dataItem.getDepartementsUserName().getId().equals(UserPreferenceHelper.getUser().getId())) {
            viewHolder.binding.name.setText(dataItem.getUser().getName());
            viewHolder.binding.details.setText(dataItem.getUser().getHrmsJobEnName());
            convertDateTime(dataItem.getCreatedAt(),viewHolder.binding.date,
                    viewHolder.binding.time);

//            byte[] decodedString = Base64.decode(x, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            viewHolder.binding.profileImage.setImageBitmap(decodedByte);

        } else {
            viewHolder.binding.name.setText(dataItem.getDepartementsUserName().getName());
            viewHolder.binding.details.setText(dataItem.getDepartementsUserName().getHrmsJobEnName());
            convertDateTime(dataItem.getCreatedAt(),viewHolder.binding.date,
                    viewHolder.binding.time);

//            byte[] decodedString = Base64.decode(x, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            viewHolder.binding.profileImage.setImageBitmap(decodedByte);

        }

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, dataItem);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (dataItems == null)
            return 0;
        return dataItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemChatUserBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bind();
        }


        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind();
            }
        }

    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, DataItem dataItem);
    }

    public void convertDateTime(String format, TextView date,TextView time) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm:ss");

        Date d = null;
        try {
            d = input.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        String formatted1 = outputTime.format(d);
        Log.i("DATE", "" + formatted+formatted1);
        date.setText(formatted);
        time.setText(formatted1);
    }

}
