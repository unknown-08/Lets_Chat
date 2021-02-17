package com.example.letschat;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Finduseractivity extends AppCompatActivity {

    DatabaseReference reference;
    private RecyclerView usersList;
    private  RecyclerView.Adapter usersAdapter;
    private RecyclerView.LayoutManager usersManager;
    ArrayList<Userobject> arrayList,contactList;
    ArrayList<String>vis;
    String phoneuser="";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finduseractivity);
        arrayList=new ArrayList<>();
        contactList=new ArrayList<>();
        vis=new ArrayList<>();
        getContactList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getContactList(){

        //String isoprefix=getCountryIso();
        Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null);
        while(phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          //   Log.i("Name",name);
            phone.replace(" ","");
            phone.replace("-","");
            phone.replace(")","");
            phone.replace("(","");

            if(String.valueOf(phone.charAt(0)).equals("+")){

            }
           else
            phone="+91"+phone;

            Log.i("Name",phoneuser);
            //Log.i("phoneNo",phone);
            //Log.i("user",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString());

            if(!vis.contains(phone) && !phone.equals(phoneuser)) {
                Userobject userobject = new Userobject("",name, phone);
                contactList.add(userobject);
                getUserDetails(userobject);
                vis.add(phone);
            }
        }

    }


     private void getUserDetails(final Userobject userobject) {

         DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");
         db.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                     User user = childsnapshot.getValue(User.class);
                     String phone = "", name = "";
                     name = user.getUsername();
                     phone = user.getPhoneNo();
                     if (phone.equals("+919839488023"))
                         Log.i("Find", phone);
                     if (phone.equals(userobject.getPhoneNo())) {

                         Log.i("Find", phone);
                         Userobject muser = new Userobject(childsnapshot.getKey(), name, phone);
                         arrayList.add(muser);
                         usersAdapter.notifyDataSetChanged();
                     }

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
     }
}
