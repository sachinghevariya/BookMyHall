package psp.bookmyhall;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class fragment_profile extends Fragment {

    DatabaseReference database;
    FirebaseAuth auth;
    StorageReference mstorage;
    String uid;
    TextView mn,ct,nm,em;
    ImageView iv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();
        mstorage= FirebaseStorage.getInstance().getReference().child("Profile Pictures").child(uid);
        database= FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nm=view.findViewById(R.id.nm);
        ct=view.findViewById(R.id.ct);
        em=view.findViewById(R.id.em);
        mn=view.findViewById(R.id.mn);
        iv=view.findViewById(R.id.dp);

        viewprofile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_fragment_profile, container, false);
    }




    public void viewprofile(){

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child(uid).child("Name").getValue(String.class);
                String city=dataSnapshot.child(uid).child("City").getValue(String.class);
                String mobile_number=dataSnapshot.child(uid).child("Mobile Number").getValue(String.class);
                String email=dataSnapshot.child(uid).child("Email").getValue(String.class);

                Glide.with(fragment_profile.this).using(new FirebaseImageLoader()).load(mstorage).into(iv);
                nm.setText(name);
                em.setText(email);
                ct.setText(city+",Gujarat");
                mn.setText(mobile_number);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getimgg(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }






}