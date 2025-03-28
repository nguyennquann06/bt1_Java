package com.example.app_gk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateProjectActivity extends AppCompatActivity {

    private EditText editTextProjectName, editTextProjectDescription;
    private Button buttonSaveProject;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        editTextProjectName = findViewById(R.id.editTextProjectName);
        editTextProjectDescription = findViewById(R.id.editTextProjectDescription);
        buttonSaveProject = findViewById(R.id.buttonSaveProject);
        databaseHelper = new DatabaseHelper(this);

        buttonSaveProject.setOnClickListener(v -> {
            String projectName = editTextProjectName.getText().toString().trim();
            String description = editTextProjectDescription.getText().toString().trim();

            if (projectName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên dự án", Toast.LENGTH_SHORT).show();
            } else {
                long result = databaseHelper.addProject(projectName, description);
                if (result != -1) {
                    Toast.makeText(this, "Tạo dự án thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi tạo dự án", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}