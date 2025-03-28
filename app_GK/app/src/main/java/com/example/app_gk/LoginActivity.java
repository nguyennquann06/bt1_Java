package com.example.app_gk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Kiểm tra và khởi tạo các view
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        if (editTextEmail == null || editTextPassword == null || buttonLogin == null) {
            Log.e(TAG, "One or more views are null. Check layout file.");
            Toast.makeText(this, "Lỗi giao diện. Vui lòng kiểm tra lại layout.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Khởi tạo DatabaseHelper
        try {
            databaseHelper = new DatabaseHelper(this);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing DatabaseHelper: " + e.getMessage());
            Toast.makeText(this, "Lỗi khởi tạo cơ sở dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText() != null ? editTextEmail.getText().toString().trim() : "";
            String password = editTextPassword.getText() != null ? editTextPassword.getText().toString().trim() : "";

            Log.d(TAG, "Email: " + email + ", Password: " + password);

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    User user = databaseHelper.getUserByEmail(email);
                    if (user != null) {
                        Log.d(TAG, "User found: " + user.getEmail() + ", Role: " + user.getRole());
                        if (user.getPassword().equals(password)) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_name", user.getName());
                            intent.putExtra("user_role", user.getRole());
                            intent.putExtra("user_email", user.getEmail());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "User not found for email: " + email);
                        Toast.makeText(this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error during login: " + e.getMessage());
                    Toast.makeText(this, "Lỗi đăng nhập: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}