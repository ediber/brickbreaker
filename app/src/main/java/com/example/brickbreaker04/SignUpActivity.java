package com.example.brickbreaker04;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        db = FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.username);
        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        signupButton = findViewById(R.id.signup_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signupButton.setOnClickListener(v -> gatherDataAndSignUp());
    }

    private void gatherDataAndSignUp() {
        String username = usernameEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // You can now pass this data to your signup function
        // For demonstration purposes, we'll just show a Toast message
        Toast.makeText(this, "Signing up with:\nUsername: " + username + "\nName: " + name + "\nPhone: " + phone + "\nPassword: " + password, Toast.LENGTH_LONG).show();

        // Create a new user with the data
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("name", name);
        user.put("phone", phone);
        user.put("password", password);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
                    // Clear the input fields
                    usernameEditText.setText("");
                    nameEditText.setText("");
                    phoneEditText.setText("");
                    passwordEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error signing up user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}
