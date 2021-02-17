package com.example.letschat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView profileImageView;
    StorageReference storageReference;
    TextView nametextView,phoneNotextView;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nametextView=findViewById(R.id.Nameedittext);
        phoneNotextView=findViewById(R.id.Phonenoedittext);
        profileImageView=findViewById(R.id.profileimageview);
        auth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.letschat.User user=snapshot.getValue(com.example.letschat.User.class);
                nametextView.setText(user.getUsername());
                Log.i("Name",user.getUsername());
                Log.i("URL",user.getImageURL());

                if(user.getImageURL().equals("default")){

                    profileImageView.setImageResource(R.mipmap.ic_launcher);
                    //   profileImageView.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(com.example.letschat.ProfileActivity.this).load(user.getImageURL()).into(profileImageView);
                }
                phoneNotextView.setText(user.getPhoneNo());

                Log.i("PhoneNo",user.getPhoneNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
