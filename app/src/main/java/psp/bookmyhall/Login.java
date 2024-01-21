package psp.bookmyhall;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    EditText email,pwd;

    public void register(View view){
        Intent intent=new Intent(Login.this,Register.class);
        startActivity(intent);
    }


    public void login(View view){
        String em=email.getText().toString();
        String pw=pwd.getText().toString();
        auth.signInWithEmailAndPassword(em,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i=new Intent(Login.this,home.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "Invalid UserID or Password", Toast.LENGTH_SHORT).show();

                }
            }
        });



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.editText);
        pwd=(EditText)findViewById(R.id.editText2);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    Intent i=new Intent(Login.this,home.class);
                    startActivity(i);
                    finish();
                }
}
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);

    }
}
