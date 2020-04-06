package com.example.map;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class signUpActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, name;
    ProgressBar progressbar;
    TextView privacy;

    // for the calender dialog

    //TextView date;
    //DatePickerDialog.OnDateSetListener pop;

    RadioGroup gender;
    RadioButton man, woman;

    String gndr = "";
    Button button;
    //String dat="";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.pp);
        name = (EditText) findViewById(R.id.name);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        FirebaseApp.initializeApp(this);



        privacy = (TextView) findViewById(R.id.privacy);


        String text="By creating an account, you agree to our Terms of Service and Privacy Policy.";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsorange = new ForegroundColorSpan(getResources().getColor(R.color.slide2));
        ForegroundColorSpan fcsorange2 = new ForegroundColorSpan(getResources().getColor(R.color.slide2));

        ss.setSpan(fcsorange,41,58, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsorange2,62,77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacy.setText(ss);



    }

    public void already(View view){
        Intent intent = new Intent(signUpActivity.this, signInActivity.class);
        startActivity(intent);
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }


    public void signUp(View view) {

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = this.name.getText().toString().trim();


        if (email.isEmpty()) {
            editTextEmail.setError("email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            this.name.setError("field is empty");
            this.name.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Pleas inter a valide email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        FirebaseApp.initializeApp(this);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                String emailCode =EncodeString(email);

                if (!(dataSnapshot.child("Users").child(emailCode).exists())) {



                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    userdataMap.put("email", email);

                    RootRef.child("Users").child(emailCode).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressbar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();

                                        Intent intent = new Intent(signUpActivity.this, HomeActivity.class);
                                        intent.putExtra("data",gndr+" "+name);
                                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        //loadingBar.dismiss();
                                        Toast.makeText(signUpActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } /*else {
                                progressbar.setVisibility(View.GONE);
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "You are already registred with this email", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signUp.this, signIn.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signUp.this, signIn.class);
                                    startActivity(intent);
                                }
                            }*/


                else {
                    Toast.makeText(signUpActivity.this, "This email "+email  +"already exists.", Toast.LENGTH_SHORT).show();
                    //loadingBar.dismiss();
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(signUpActivity.this, "Please try again using another email.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(signUpActivity.this, signInActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}