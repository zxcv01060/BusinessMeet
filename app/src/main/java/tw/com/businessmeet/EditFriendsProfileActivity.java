package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class EditFriendsProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private EditProfileFragment editProfileFragment;
    private EditMemoFragment editMemoFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_profile_edit);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.friends_profile_topAppBar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios_24px); //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(EditFriendsProfileActivity.this, FriendsIntroductionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("friendId", getIntent().getStringExtra("friendId"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        case R.id.menu_search:
//        startActivity(new Intent(getApplicationContext()
//                , SearchActivity.class));
//        overridePendingTransition(0, 0);
//        return true;

        //tab
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.friends_profile_tabs);

        editProfileFragment = new EditProfileFragment();
        editMemoFragment = new EditMemoFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(editProfileFragment, "好友資訊");
        viewPagerAdapter.addFragment(editMemoFragment, "備註");
        viewPager.setAdapter(viewPagerAdapter);
    }

    //tab
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentsTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentsTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }
    }
}