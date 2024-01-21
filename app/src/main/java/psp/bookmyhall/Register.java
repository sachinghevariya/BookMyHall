package psp.bookmyhall;



import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
        boolean flag=false;
        int a[]={1,1,1,1,1};
        AutoCompleteTextView city;
        EditText username,email,password,mobile;
        private FirebaseAuth auth;
        private ProgressDialog progressDialog;
        private DatabaseReference databaseReference;
        String[] cities;
        ArrayAdapter<String> adapter;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
        Animation shake ;
        ImageView iv;
        StorageReference storageReference;
        Uri uri;

    public void log(View view) {


        if ((TextUtils.isEmpty(username.getText().toString())) || username.getText().toString().length() <= 2) {
            username.startAnimation(shake);
            username.setError("Enter Valid Name");
            a[0] = 0;
        } else {
            a[0] = 1;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.startAnimation(shake);
            email.setError("Enter Valid Email");
            a[1] = 0;
        } else {
            a[1] = 1;
        }
        if ((TextUtils.isEmpty(password.getText().toString())) || password.getText().toString().length() <= 4) {
            password.startAnimation(shake);
            password.setError("Password Should have Atleast 5 Characters");
            a[2] = 0;
        } else {
            a[2] = 1;
        }
        if ((TextUtils.isEmpty(mobile.getText().toString())) || mobile.getText().toString().length() != 10) {
            mobile.startAnimation(shake);
            mobile.setError("Enter Valid Mobile Number");
            a[3] = 0;
        } else {
            a[3] = 1;
        }
        if (TextUtils.isEmpty(city.getText().toString())) {
            city.startAnimation(shake);
            city.setError("Enter Valid City");
            a[4] = 0;
        } else {
            a[4] = 1;
        }



        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        if (a[0] == 1 && a[1] == 1 && a[2] == 1 && a[3] == 1 && a[4] == 1) {

                Savedata();

        }

    }


    public void Savedata(){
        progressDialog.setMessage("Registering");
        progressDialog.show();
        final String uname=username.getText().toString().trim();
        final String eml=email.getText().toString().trim();
        final String pwd=password.getText().toString().trim();
        final String ct=city.getText().toString().trim();
        final String mn=mobile.getText().toString().trim();
        progressDialog.setMessage("Verifying Mobile");
        progressDialog.setCancelable(false);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {

                    /*childdb.child("Name").setValue(uname);
                    childdb.child("Email").setValue(eml);
                    childdb.child("Password").setValue(pwd);
                    childdb.child("City").setValue(ct);
                    childdb.child("Mobile Number").setValue(mn);*/
                    cuser(uname,eml,pwd,ct,mn);


                }


                @Override
                public void onVerificationFailed(FirebaseException e) {
                    mobile.startAnimation(shake);
                    mobile.setError("Enter Current Mobile Number");
                    a[3] = 0;
                    progressDialog.dismiss();
                }


                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    progressDialog.setMessage("Waiting To Receive Code");

                }

            };

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+mn, 25, TimeUnit.SECONDS, this, mCallbacks);




        }



        public void cuser(final String uname,final String eml, final String pwd,final String ct,final String mn){
            auth.createUserWithEmailAndPassword(eml, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        signinn(uname,eml, pwd,ct,mn);
                    } else {
                        Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }
            });




        }

        public void signinn(final String uname, final String eml, final String pwd, final String ct, final String mn){
            auth.signInWithEmailAndPassword(eml, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        DatabaseReference childdb=databaseReference.child("Users").child(auth.getCurrentUser().getUid());
                        progressDialog.setMessage("Creating User");
                        childdb.child("Name").setValue(uname);
                        childdb.child("Email").setValue(eml);
                        childdb.child("Password").setValue(pwd);
                        childdb.child("City").setValue(ct);
                        childdb.child("Mobile Number").setValue(mn);
                        StorageReference cstorage=storageReference.child(auth.getCurrentUser().getUid());
                        cstorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });

                        startActivity(new Intent(Register.this,home.class));
                    }
                }
            });



        }



        public void getimg(View view){

            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,2);
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2 && resultCode==RESULT_OK){

            uri=data.getData();
            iv.setImageURI(uri);

        }


        }

    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        mobile = (EditText)findViewById(R.id.mobile);
        city=(AutoCompleteTextView)findViewById(R.id.city);
        auth = FirebaseAuth.getInstance();
        iv=(ImageView) findViewById(R.id.iv);

        cities = getResources().getStringArray(R.array.cities);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        city.setAdapter(adapter);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        storageReference= FirebaseStorage.getInstance().getReference().child("Profile Pictures");


    }
        }