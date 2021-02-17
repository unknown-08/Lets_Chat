package com.example.letschat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.letschat.Fragment.ChatFragment;
import com.example.letschat.Fragment.ProfileFragment;
import com.example.letschat.Fragment.UsersFragment;
import com.example.letschat.Model.Chat;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsefeedActivity extends AppCompatActivity {

    private RecyclerView chatList;
    private  RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager chatManager;
    FirebaseAuth auth;
    CircleImageView profileImageView;
    TextView username;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usefeed);
        Toolbar toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("UserFeed");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        final TabLayout tabLayout=findViewById(R.id.tabLayout);
        final ViewPager viewPager=findViewById(R.id.view_pager);
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ViewPageAdapter viewPageAdapter=new ViewPageAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                    int unread=0;

                for(DataSnapshot snapshot1:snapshot.getChildren()){

                    Chat chat=snapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                if(unread==0)
                    viewPageAdapter.addFragment(new ChatFragment(),"Chats");
                else
                    viewPageAdapter.addFragment(new ChatFragment(),"("+unread+") Chats");
                    viewPageAdapter.addFragment(new UsersFragment(),"Users");
                    viewPageAdapter.addFragment(new ProfileFragment(),"Profile");
                    viewPager.setAdapter(viewPageAdapter);
                    tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

                    firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

       /* userfindbUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Finduseractivity.class));
            }
        });*/
       // getPermissions();

        //initializeRecycleView();
      //  getUserChatList();

    }
 class ViewPageAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment>fragments;
        private ArrayList<String>titles;

     public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
         super(fm, behavior);
         this.fragments=new ArrayList<>();
         this.titles=new ArrayList<>();
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

     public void addFragment(Fragment fragment,String title) {
         fragments.add(fragment);
         titles.add(title);
     }

     @Nullable
     @Override
     public CharSequence getPageTitle(int position){
         return titles.get(position);
     }
 }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.logout){
            auth= FirebaseAuth.getInstance();
            auth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        if(item.getItemId()==R.id.profile){

            startActivity(new Intent(getApplicationContext(), com.example.letschat.ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return true;
    }

    private  void status(String status){

        reference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
