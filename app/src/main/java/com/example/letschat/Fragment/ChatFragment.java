package com.example.letschat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.Adapter.UserAdapter;
import com.example.letschat.Model.ChatList;
import com.example.letschat.Notifications.Token;
import com.example.letschat.R;
import com.example.letschat.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<ChatList>usersList;
    private List<User>mUsers;
    //private List<String> usersList;
    // private List<String> var;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          View view=inflater.inflate(R.layout.fragment_chat,container,false);
          recyclerView=view.findViewById(R.id.recycler_view);
          recyclerView.setHasFixedSize(true);
          recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//          DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
  //        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyler_divider));
    //      recyclerView.addItemDecoration(dividerItemDecoration);
          firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
          usersList=new ArrayList<>();
          databaseReference=FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {

                  usersList.clear();
                  for(DataSnapshot snapshot1:snapshot.getChildren()){
                      ChatList chatlist=snapshot1.getValue(ChatList.class);
                      usersList.add(chatlist);
                  }

                  chatList();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
    /*      databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {

                  usersList.clear();

                  for(DataSnapshot snapshot1:snapshot.getChildren())
                  {
                      Chat chat =snapshot1.getValue(Chat.class);
                      if(chat.getSender().equals(firebaseUser.getUid())){
                          usersList.add(chat.getReceiver());
                      }
                      if(chat.getReceiver().equals(firebaseUser.getUid()))
                          usersList.add(chat.getSender());

                  }
                  readChats();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
*/
         updateToken(FirebaseInstanceId.getInstance().getToken());
         return view;
    }

    private void updateToken(String token){

        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        db.child(firebaseUser.getUid()).setValue(token1);

    }
     private  void chatList(){
        mUsers=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);
                    for(ChatList chatlist: usersList){
                        if(user.getId().equals(chatlist.getId()))
                            mUsers.add(user);
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /*private void  readChats(){

        mUsers=new ArrayList<>();
        var=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUsers.clear();
                   var.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    User user=snapshot1.getValue(User.class);

                    for(String id:usersList)
                    {
                        if(user.getId().equals(id)){
                            if(var.size()!=0){

                                  if(!var.contains(id)){
                                      mUsers.add(user);
                                      var.add(id);
                                }
                            }
                            else {

                                mUsers.add(user);
                                var.add(user.getId());
                            }

                        }
                    }
                }

                userAdapter=new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

}