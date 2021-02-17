package com.example.letschat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.MessageActivity;
import com.example.letschat.Model.Chat;
import com.example.letschat.R;
import com.example.letschat.UIutils;
import com.example.letschat.User;
import com.example.letschat.showImageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private  boolean isChat;
    private  StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String theLastMessage;
    private static int VIEW_TYPE_ITEM=0;
    private static int VIEW_TYPE_DIVIDER=1;

    public UserAdapter(Context mContext,List<User> mUsers,boolean isChat)
    {
        this.mContext=mContext;
        this.mUsers=mUsers;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(mContext).inflate(
                viewType==VIEW_TYPE_ITEM?R.layout.users_fragment:R.layout.recyclerview_divider,parent,false);
        return new ViewHolder(layoutView,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if(holder.mViewType==VIEW_TYPE_ITEM) {
            final User user = mUsers.get(position/2);
            holder.username.setText(user.getUsername());

            if (user.getImageURL().equals("default"))
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            else {
                storageReference = FirebaseStorage.getInstance().getReference("uploads");
                final StorageReference filereference = storageReference.child("users/" + user.getId() + "/profile.jpg");
                final long ONE_MEGABYTE = 1024 * 1024;
                filereference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.profile_image.setImageBitmap(bmp);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        //Toast.makeText(mContext, "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (isChat) {

                lastMessage(user.getId(), holder.last_msg);

                if (user.getStatus().equals("online")) {
                    holder.img_on.setVisibility(View.VISIBLE);
                    holder.img_off.setVisibility(View.GONE);
                } else {
                    holder.img_off.setVisibility(View.VISIBLE);
                    holder.img_on.setVisibility(View.GONE);

                }
            } else {
                holder.last_msg.setVisibility(View.GONE);
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("userid", user.getId());
                    mContext.startActivity(intent);

                }
            });

            holder.profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storageReference = FirebaseStorage.getInstance().getReference("uploads");
                    final StorageReference filereference = storageReference.child("users/" + user.getId() + "/profile.jpg");

                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UIutils uIutils=new UIutils(mContext);
                            uIutils.showPhotoContext(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Intent intent=new Intent(mContext, showImageActivity.class);
                            mContext.startActivity(intent);
                        }
                    });

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size()*2;
    }

    @Override
    public int getItemViewType(int position) {

        if(position%2==0)
            return VIEW_TYPE_ITEM;
        return VIEW_TYPE_DIVIDER;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        private int mViewType;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            mViewType=viewType;
            if(mViewType==VIEW_TYPE_ITEM) {
                username = itemView.findViewById(R.id.user_name);
                profile_image = itemView.findViewById(R.id.user_image);
                img_on = itemView.findViewById(R.id.img_on);
                img_off = itemView.findViewById(R.id.img_off);
                last_msg = itemView.findViewById(R.id.last_msg);
            }

        }
    }
    private void lastMessage(final String userid, final TextView last_msg){
      theLastMessage="default";

      final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
      DatabaseReference db=FirebaseDatabase.getInstance().getReference("Chats");

      db.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {

              for(DataSnapshot snapshot1:snapshot.getChildren()){
                  Chat chat=snapshot1.getValue(Chat.class);
                  if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid)
                  && chat.getSender().equals(firebaseUser.getUid()))
                      theLastMessage=chat.getMessage();
              }
              switch (theLastMessage){
                  case "default":
                      last_msg.setText("No message");
                      break;
                  default:
                       last_msg.setText(theLastMessage);
                       break;
              }

              theLastMessage="default";

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }
}
