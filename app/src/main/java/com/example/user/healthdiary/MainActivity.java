package com.example.user.healthdiary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private EditText nameTXT, emailTXT, phnTXT, prscrptnTXT, dateTXT;
    private Button saveBTN, viewtBTN;
    private SignInButton signIn;
    DatabaseReference visitDatabase;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visitDatabase = FirebaseDatabase.getInstance().getReference("Visit");

        nameTXT = findViewById(R.id.nameTXT);
        emailTXT = findViewById(R.id.emailTXT);
        phnTXT = findViewById(R.id.phnTXT);
        prscrptnTXT = findViewById(R.id.prscrptnTXT);
        dateTXT = findViewById(R.id.dateTXT);
        saveBTN = findViewById(R.id.saveBTN);
        viewtBTN = findViewById(R.id.viewtBTN);

        signIn = findViewById(R.id.signIn);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            setGooglePlusButtonText(signIn, "Sign Out");
        }

        viewtBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent i = new Intent(MainActivity.this, ViewSavedActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "You need to sign in", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    signIn();
                }
                else{
                    signOut();
                }
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    Toast.makeText(MainActivity.this, "You need to sign in to save", Toast.LENGTH_SHORT).show();
                }
                else {
                    addVisitInfo();
                }
            }
        });

    }

    private void addVisitInfo(){
        String name = nameTXT.getText().toString().trim();
        String email = emailTXT.getText().toString().trim();
        String phone = phnTXT.getText().toString().trim();
        String prescription = prscrptnTXT.getText().toString().trim();
        String date = dateTXT.getText().toString().trim();

        if (!TextUtils.isEmpty(name)){
            String id = visitDatabase.push().getKey();
            Visit visit = new Visit(id,name,email,phone,prescription,date);

            visitDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).setValue(visit);

            Toast.makeText(MainActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "Must fill up the field", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                    }
                });
        setGooglePlusButtonText(signIn, "Sign In");
    }

    private void signIn(){
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build())).build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                setGooglePlusButtonText(signIn, "Sign Out");
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
}
