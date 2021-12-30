package com.chat.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.chat.ChatThread.ChatThreadActivity;
import com.chat.R;
import com.chat.TR.TRFragment;
import com.chat.databinding.ActivityMainBinding;
import com.chat.groups.GroupsFragment;
import com.chat.home.HomeFragment;
import com.chat.main.model.searchPeople.SearchPeapleResponse;
import com.chat.main.model.user.UserResponse;
import com.chat.main.model.userID.UserIDResponse;
import com.chat.utils.UserPreferenceHelper;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainActivityViewModel viewModel;
    FragmentPagerItemAdapter adapter;

    private List<String> people = new ArrayList<>();
    private ArrayAdapter<String> adapterPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        viewModel.getpeapleSearchList();
        viewModel.getUserModel("Mostafa Kamel K","Q2WXxk","sadsadasd");
        prepareTabsLayout();
        onClickListener();
    }

    private void onClickListener() {
        viewModel.getErrorMessage().observe(this, integer -> {
            if (integer == 1) {
                Toast.makeText(MainActivity.this, "error in response data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "error in Network", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getPeapleResponseMutableLiveData().observe(this, new Observer<SearchPeapleResponse>() {
            @Override
            public void onChanged(SearchPeapleResponse searchPeapleResponse) {
                adapterPeople = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, people);
                binding.tvPeopleSearch.setAdapter(adapterPeople);
                try {
                    for (int i = 0; i < searchPeapleResponse.getData().size(); i++) {
                        String name = searchPeapleResponse.getData().get(i).getName();
                        people.add(name);
                    }
                    adapterPeople.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                binding.tvPeopleSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (people.contains(String.valueOf(charSequence))) {
                            int index = people.indexOf(String.valueOf(charSequence));
                            viewModel.getUserID(searchPeapleResponse.getData().get(index).getId());
                        } else {
                            Toast.makeText(MainActivity.this
                                    , "please choose right name", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });
        viewModel.getUserResponseMutableLiveData().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                UserPreferenceHelper.saveUserProfile(userResponse);
            }
        });
        viewModel.getUserIDResponseMutableLiveData().observe(this, new Observer<UserIDResponse>() {
            @Override
            public void onChanged(UserIDResponse userIDResponse) {
                Intent intent= new Intent(MainActivity.this, ChatThreadActivity.class);
                intent.putExtra("peopleItem",userIDResponse.getId());
                startActivity(intent);
            }
        });
    }

    public void prepareTabsLayout() {
        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.title_home, HomeFragment.class)
                        .add(R.string.title_groups, GroupsFragment.class)
                        .add(R.string.title_t_r, TRFragment.class)
                        .create());


        binding.viewpager.setAdapter(adapter);
        binding.viewpagertab.setViewPager(binding.viewpager);


        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.viewpager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }
}