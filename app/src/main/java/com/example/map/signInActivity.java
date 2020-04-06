package com.example.map;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.map.Model.Users;
import com.example.map.Prevalent.Prevalent;
import com.google.firebase.FirebaseApp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class signInActivity extends AppCompatActivity {

    EditText password, email;
    TextView clickHere;
    ProgressBar progressbar;

    Button LoginButton;
    ImageView already;

    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        clickHere = (TextView) findViewById(R.id.clickHere);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        LoginButton = (Button) findViewById(R.id.button2) ;

        FirebaseApp.initializeApp(this);



        Paper.init(this);




        String text="Don’t have an account? Swipe right to create a new account.";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsorange = new ForegroundColorSpan(getResources().getColor(R.color.slide2));

        ss.setSpan(fcsorange,38,59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickHere.setText(ss);



        String UserPhoneKey = Paper.book().read(Prevalent.UserEmailKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccessToAccount(UserPhoneKey, UserPasswordKey);
            }
        }

    }


    public void register(View view) {
        Intent intent = new Intent(signInActivity.this, signUpActivity.class);
        startActivity(intent);
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public void signIn(View view) {
        String em = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (em.isEmpty()) {
            email.setError("email is required");
            email.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            Toast.makeText(signInActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Pleas inter a valide email!");
            email.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(signInActivity.this, "Minimum length of password should be 6", Toast.LENGTH_SHORT).show();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        AllowAccessToAccount(em, pass);


    }

    private void AllowAccessToAccount(final String email, final String password)
    {

        FirebaseApp.initializeApp(this);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();





        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String emailCode =EncodeString(email);

                //--------------------------------------
                Paper.book().write(Prevalent.UserEmailKey, email);
                Paper.book().write(Prevalent.UserPasswordKey, password);
                //------------------------------------


                if (dataSnapshot.child(parentDbName).child(emailCode).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(emailCode).getValue(Users.class);

                    if (usersData.getEmail().equals(email))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                            Toast.makeText(signInActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);

                            Intent intent = new Intent(signInActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                            finish();
                            }

                        }
                        else
                        {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(signInActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                }
                else
                {
                    Toast.makeText(signInActivity.this, "Account with this " + email + " number do not exists.", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
