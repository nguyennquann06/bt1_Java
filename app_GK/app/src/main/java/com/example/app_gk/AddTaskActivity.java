package com.example.app_gk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
    private EditText editTextTaskName; // Xóa editTextDescription
    private Spinner spinnerProject, spinnerUser, spinnerStatus;
    private Button buttonAddTask;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        // Xóa dòng liên quan đến editTextDescription
        spinnerProject = findViewById(R.id.spinnerProject);
        spinnerUser = findViewById(R.id.spinnerUser);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        databaseHelper = new DatabaseHelper(this);

        loadProjects();
        loadUsers();
        loadStatusOptions();

        buttonAddTask.setOnClickListener(v -> {
            String taskName = editTextTaskName.getText().toString().trim();
            // Xóa description vì không còn editTextDescription
            int projectId = (int) spinnerProject.getSelectedItemId() + 1;
            int assignedUserId = (int) spinnerUser.getSelectedItemId() + 1;
            String status = spinnerStatus.getSelectedItem().toString();

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên công việc", Toast.LENGTH_SHORT).show();
            } else {
                // Truyền một giá trị mặc định cho description (ví dụ: rỗng)
                long result = databaseHelper.addTask(taskName, "", status, projectId, assignedUserId);
                if (result != -1) {
                    Toast.makeText(this, "Thêm công việc thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm công việc thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProjects() {
        List<Project> projects = databaseHelper.getAllProjects();
        List<String> projectNames = new ArrayList<>();
        for (Project project : projects) {
            projectNames.add(project.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProject.setAdapter(adapter);
    }

    private void loadUsers() {
        List<User> users = databaseHelper.getAllUsers();
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapter);
    }

    private void loadStatusOptions() {
        String[] statusOptions = {"Chưa bắt đầu", "Đang thực hiện", "Hoàn thành"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }
}