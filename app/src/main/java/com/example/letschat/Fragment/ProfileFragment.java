package com.example.letschat.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.letschat.R;
import com.example.letschat.UIutils;
import com.example.letschat.User;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public  class  ProfileFragment extends Fragment{

   CircleImageView image_profile;
   TextView username;
   DatabaseReference databaseReference;
   FirebaseUser fUser;
   StorageReference storageReference;
   private  static final int Image_Request=1;
   private Uri imageUri;
   private StorageTask uploadTask;
   private CircleImageView change_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_profile, container, false);
       image_profile=view.findViewById(R.id.profile_Image);
       change_image=view.findViewById(R.id.change_image);
       username=view.findViewById(R.id.Username);
       storageReference= FirebaseStorage.getInstance().getReference("uploads");
       fUser= FirebaseAuth.getInstance().getCurrentUser();
       databaseReference= FirebaseDatabase.getInstance().getReference("User").child(fUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user=snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default"))
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                else {

                    final StorageReference filereference=storageReference.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
                 /*   filereference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(image_profile);
                                    databaseReference.child("imageurl").setValue("true");
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();

                        }
                    });*/
                    final long ONE_MEGABYTE = 1024 * 1024;
                    filereference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            image_profile.setImageBitmap(bmp);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference1=storageReference.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
              storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {

                     UIutils uIutils=new UIutils(getActivity());
                     uIutils.showPhoto(uri);
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {

                  }
              });
               }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
     //             openImage();
                  Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  startActivityForResult(openGalleryIntent,1000);

              }
          });
         return view;
    }

    private void uploadImagetoFirebase(Uri imageUri){

        final StorageReference filereference=storageReference.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        filereference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(image_profile);
                        databaseReference.child("imageurl").setValue("true");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();

            }
        });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageuri = data.getData();

                //   profileImageView.setImageURI(imageuri);
                uploadImagetoFirebase(imageuri);
            }
        }
    }


/*    private  void openImage(){

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Image_Request);

    }

    private String getFileExtension(Uri uri){

        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));

    }

    private void uploadImage(){
        final ProgressBar pb=new ProgressBar(getContext());

        if(imageUri!=null)
        {
            final StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask=filereference.getFile(imageUri);
            uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                       throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();
                        databaseReference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageurl",imageUri);
                        databaseReference.updateChildren(map);

                    }
                    else{

                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{
            Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Image_Request && resultCode==RESULT_OK && data!=null
        && data.getData()!=null){
            imageUri=data.getData();
            if(uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload In Progress",Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage();
        }

    }*/
}