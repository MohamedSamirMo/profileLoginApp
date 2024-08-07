package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    String UserName;
    String Password;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateUserName() || !ValidatePassword()) {
                    return;
                } else {
                    CheckUser();
                }
            }
        });

        binding.textNotMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean ValidateUserName() {
        String validate = binding.signInUsername.getText().toString();
        if (validate.isEmpty()) {
            binding.signInUsername.setError("UserName Cannot be Empty");
            return false;
        } else {
            binding.signInUsername.setError(null);
            return true;
        }
    }

    public Boolean ValidatePassword() {
        String validated = binding.signInLock.getText().toString();
        if (validated.isEmpty()) {
            binding.signInLock.setError("Password Cannot be Empty");
            return false;
        } else {
            binding.signInLock.setError(null);
            return true;
        }
    }

    public void CheckUser() {
        UserName = binding.signInUsername.getText().toString().trim();
        Password = binding.signInLock.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query CheckUserDatabase = reference.orderByChild("username").equalTo(UserName);

        CheckUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDatabase = userSnapshot.child("password").getValue(String.class);

                    if (passwordFromDatabase.equals(Password)) {
                        binding.signInUsername.setError(null);

                        //pass data Using Intent
                        String NameDB = userSnapshot.child("name").getValue(String.class);
                        String EmailDB=userSnapshot.child("email").getValue(String.class);
                        String UsernameDB=userSnapshot.child("username").getValue(String.class);
                        String PhoneDB=userSnapshot.child("phone").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.putExtra("name",NameDB);
                        intent.putExtra("email",EmailDB);
                        intent.putExtra("username",UsernameDB);
                        intent.putExtra("password",passwordFromDatabase);
                        intent.putExtra("phone",PhoneDB);
                        startActivity(intent);


                    } else {
                        binding.signInLock.setError("Invalid Password");
                        binding.signInLock.requestFocus();
                    }
                } else {
                    binding.signInUsername.setError("User Does Not Exist");
                    binding.signInUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}