package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.databinding.ActivityEditProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    DatabaseReference reference;

    // Declare instance variables
    String Name;
    String Email;
    String Username;
    String Phone;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("users");
        ShowData();

        binding.editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateUserData()) {
                    Toast.makeText(EditProfileActivity.this, "Data Updated Saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Update user data in Firebase based on the username
    public boolean updateUserData() {
        final boolean[] isChanged = {false};

        // Get new data from input fields
        final String newName = binding.editName.getText().toString().trim();
        final String newEmail = binding.editEmail.getText().toString().trim();
        final String newPassword = binding.editLock.getText().toString().trim();
        final String newPhone = binding.editPhone.getText().toString().trim();

        // Create a query to find the user by username
        Query query = reference.orderByChild("username").equalTo(Username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        DatabaseReference userRef = userSnapshot.getRef();

                        boolean localChanged = false;

                        if (newName != null && !newName.equals(Name)) {
                            userRef.child("name").setValue(newName);
                            Name = newName;
                            localChanged = true;
                        }
                        if (newEmail != null && !newEmail.equals(Email)) {
                            userRef.child("email").setValue(newEmail);
                            Email = newEmail;
                            localChanged = true;
                        }
                        if (newPassword != null && !newPassword.equals(Password)) {
                            userRef.child("password").setValue(newPassword);
                            Password = newPassword;
                            localChanged = true;
                        }
                        if (newPhone != null && !newPhone.equals(Phone)) {
                            userRef.child("phone").setValue(newPhone);
                            Phone = newPhone;
                            localChanged = true;
                        }

                        isChanged[0] = localChanged;
                    }

                    if (isChanged[0]) {
                        Toast.makeText(EditProfileActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "No Changes", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return isChanged[0];
    }

    // Show user data from intent
    public void ShowData() {
        Intent intent = getIntent();
        if (intent != null) {
            Name = intent.getStringExtra("name");
            Email = intent.getStringExtra("email");
            Username = intent.getStringExtra("username");
            Phone = intent.getStringExtra("phone");
            Password = intent.getStringExtra("password");

            // Check for null and set values to the input fields
            binding.editName.setText(Name != null ? Name : "");
            binding.editEmail.setText(Email != null ? Email : "");
            binding.editUsername.setText(Username != null ? Username : "");
            binding.editPhone.setText(Phone != null ? Phone : "");
            binding.editLock.setText(Password != null ? Password : "");

        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
        }
    }
}
