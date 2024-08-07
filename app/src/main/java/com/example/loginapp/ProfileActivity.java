package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginapp.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ShowUserData();
        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassUserData();
            }
        });
    }
    public void ShowUserData(){
        Intent intent=getIntent();
        String NameUser=intent.getStringExtra("name");
        String EmailUser=intent.getStringExtra("email");
        String UsernameUser=intent.getStringExtra("username");
        String PhoneUser=intent.getStringExtra("phone");
        String PasswordUser=intent.getStringExtra("password");


        binding.titleName.setText(NameUser);
        binding.titleUsername.setText(UsernameUser);
        binding.profileName.setText(NameUser);
        binding.profileEmail.setText(EmailUser);
        binding.profileUsername.setText(UsernameUser);
        binding.profilePhone.setText(PhoneUser);
        binding.profilePassword.setText(PasswordUser);

    }
    public void PassUserData(){
        String UsernameUser=binding.profileUsername.getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query CheckUserDatabase=reference.orderByChild("username").equalTo(UsernameUser);
        CheckUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    DataSnapshot userSnapshot=snapshot.getChildren().iterator().next();
                    String nameFromDatabase=userSnapshot.child("name").getValue(String.class);
                    String emailFromDatabase=userSnapshot.child("email").getValue(String.class);
                    String usernameFromDatabase=userSnapshot.child("username").getValue(String.class);
                    String phoneFromDatabase=userSnapshot.child("phone").getValue(String.class);
                    String passwordFromDatabase=userSnapshot.child("password").getValue(String.class);
                    Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);


                    intent.putExtra("name",nameFromDatabase);
                    intent.putExtra("email",emailFromDatabase);
                    intent.putExtra("username",usernameFromDatabase);
                    intent.putExtra("phone",phoneFromDatabase);
                    intent.putExtra("password",passwordFromDatabase);
                    startActivity(intent);


            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}