package com.chat.ChatThread;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.chat.ChatThread.model.ChatListResponse;
import com.chat.ChatThread.model.sendMessages.SendMessagesResponse;
import com.chat.R;
import com.chat.databinding.ActivityChatThreadBinding;
import com.chat.filesUpload.VolleyFileObj;
import com.chat.main.model.searchPeople.PeopleItem;
import com.chat.utils.UserPreferenceHelper;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatThreadActivity extends AppCompatActivity {
    String peopleItem;
    String idUser;
    ActivityChatThreadBinding binding;
    private ChatListViewModel viewModel;
    ChatListAdapter adapter;
    Pusher pusher;
    Channel channel;
    public static final int CAMERA_REQUEST = 2011;
    List<VolleyFileObj> volleyFileObjs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_thread);
        viewModel = new ViewModelProvider(this).get(ChatListViewModel.class);
        init();
        if (getIntent().hasExtra("peopleItem")) {
            peopleItem = getIntent().getStringExtra("peopleItem");
            viewModel.getChatList(peopleItem);
        } else if (getIntent().hasExtra("people_id")) {
            idUser = getIntent().getStringExtra("people_id");
            viewModel.getChatList(idUser);
        }
        pusher();
        onClickListener();
    }

    private void init() {
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setLayoutAnimation(animation);
        adapter = new ChatListAdapter(null);
        binding.recyclerView.setAdapter(adapter);
    }

    private void pusher() {
        PusherOptions options = new PusherOptions()
                .setCluster("ap4");
        pusher = new Pusher("cc31df9923d00ef5b9c9", options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}");
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! code ($code), " + "message ($message), exception($e)");
            }
        }, ConnectionState.ALL);
        channel = pusher.subscribe("chatify");
        channel.bind("messaging", new ChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {

            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.i("Pusher", "Received event with data: $event");
                if (getIntent().hasExtra("peopleItem")) {
                    peopleItem = getIntent().getStringExtra("peopleItem");
                    viewModel.getChatList(peopleItem);
                } else if (getIntent().hasExtra("people_id")) {
                    idUser = getIntent().getStringExtra("people_id");
                    viewModel.getChatList(idUser);
                }
            }
        });
    }

    private void onClickListener() {
        binding.sendIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.message.getText().toString())) {
                    binding.message.setError("please enter message");
                } else {
                    submit();
                }
            }
        });
        viewModel.getErrorMessage().observe(this, integer -> {
            if (integer == 1) {
                Toast.makeText(ChatThreadActivity.this, "error in response data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChatThreadActivity.this, "error in Network", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getPeapleResponseMutableLiveData().observe(this, new Observer<ChatListResponse>() {
            @Override
            public void onChanged(ChatListResponse chatListResponse) {
                binding.progressBar.setVisibility(View.GONE);
                adapter = new ChatListAdapter(chatListResponse.getData());
                binding.recyclerView.setAdapter(adapter);
            }
        });
        viewModel.getSendMessagesResponseMutableLiveData().observe(this, new Observer<SendMessagesResponse>() {
            @Override
            public void onChanged(SendMessagesResponse sendMessagesResponse) {
                binding.message.setText("");
                binding.sendIMG.setImageResource(R.drawable.ic_attach_file);
                volleyFileObjs.clear();
            }
        });
    }

    public void selectImage() {
        final CharSequence[] options = {getString(R.string.labal_from_camera)
                , getString(R.string.labal_from_library_imgs), getString(R.string.labal_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatThreadActivity.this);
        builder.setTitle(getString(R.string.labal_choose_method_img));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.labal_from_camera))) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 0);
                } else if (options[item].equals(getString(R.string.labal_from_library_imgs))) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals(getString(R.string.labal_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //camera
        if (requestCode == 0) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(this, photo);
            binding.sendIMG.setImageBitmap(photo);
            volleyFileObjs.add(new VolleyFileObj("file",
                    getRealPathFromURICamera(tempUri),
                    1001));
            //gallery
        } else if (requestCode == 2) {
            //The array list has the image paths of the selected images
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            binding.sendIMG.setImageBitmap(bitmap);

            volleyFileObjs.add(new VolleyFileObj("file",
                    picturePath,
                    1001));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext
                .getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURICamera(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void submit() {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody message = RequestBody.create(MediaType.parse("multipart/form-data"), binding.message.getText().toString());
        map.put("message", message);
        Log.e("message", binding.message.getText().toString());

        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), UserPreferenceHelper.getUser().getId());
        map.put("user_id", id);
        Log.e("idxxx", UserPreferenceHelper.getUser().getId());

        if (idUser != null) {
            RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), idUser);
            map.put("id", userId);
            Log.e("user_idxxxx", idUser);
        } else if (peopleItem != null) {
            RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), peopleItem);
            map.put("id", userId);
            Log.e("user_id", peopleItem);
        }
        if (volleyFileObjs.size() == 0) {
            MultipartBody.Part part = null;
            viewModel.sendMessages(part, map);
        } else {
            RequestBody sendMGSReqBody = RequestBody.create(volleyFileObjs.get(0).getFile()
                    , MediaType.parse("image/*"));
            MultipartBody.Part part = MultipartBody.Part
                    .createFormData(volleyFileObjs.get(0).getParamName(),
                            volleyFileObjs.get(0).getFile().getName()
                            , sendMGSReqBody);
            viewModel.sendMessages(part, map);
        }
    }
}