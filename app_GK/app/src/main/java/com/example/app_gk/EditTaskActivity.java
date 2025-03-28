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

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";
    private EditText editTextTaskName, editTextDescription;
    private Spinner spinnerProject, spinnerUser, spinnerStatus;
    private Button buttonUpdateTask, buttonDeleteTask;
    private DatabaseHelper databaseHelper;
    private int taskId;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerProject = findViewById(R.id.spinnerProject);
        spinnerUser = findViewById(R.id.spinnerUser);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonUpdateTask = findViewById(R.id.buttonUpdateTask);
        buttonDeleteTask = findViewById(R.id.buttonDeleteTask);

        databaseHelper = new DatabaseHelper(this);

        taskId = getIntent().getIntExtra("task_id", -1);
        String taskName = getIntent().getStringExtra("task_name");
        String taskDescription = getIntent().getStringExtra("task_description");
        int projectId = getIntent().getIntExtra("project_id", -1);
        int assignedUserId = getIntent().getIntExtra("assigned_user_id", -1);
        String taskStatus = getIntent().getStringExtra("task_status");
        userRole = getIntent().getStringExtra("user_role"); // Nhận userRole

        editTextTaskName.setText(taskName);
        editTextDescription.setText(taskDescription);

        loadProjects(projectId);
        loadUsers(assignedUserId);
        loadStatusOptions(taskStatus);

        // Nếu không phải PM, vô hiệu hóa các trường không được phép chỉnh sửa
        if (!"PM".equals(userRole)) {
            editTextTaskName.setEnabled(false);
            editTextDescription.setEnabled(false);
            spinnerProject.setEnabled(false);
            spinnerUser.setEnabled(false);
            buttonDeleteTask.setVisibility(View.GONE); // Ẩn nút Xóa
        }

        buttonUpdateTask.setOnClickListener(v -> {
            String updatedStatus = spinnerStatus.getSelectedItem().toString();

            if (!"PM".equals(userRole)) {
                // Chỉ cập nhật trạng thái cho nhân viên
                boolean result = databaseHelper.updateTaskStatus(taskId, updatedStatus);
                if (result) {
                    Toast.makeText(this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Cập nhật toàn bộ thông tin cho PM
                String updatedTaskName = editTextTaskName.getText().toString().trim();
                String updatedDescription = editTextDescription.getText().toString().trim();
                int updatedProjectId = (int) spinnerProject.getSelectedItemId() + 1;
                int updatedUserId = (int) spinnerUser.getSelectedItemId() + 1;

                if (updatedTaskName.isEmpty() || updatedDescription.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = databaseHelper.updateTask(taskId, updatedTaskName, updatedDescription, updatedProjectId, updatedUserId, updatedStatus);
                    if (result) {
                        Toast.makeText(this, "Cập nhật công việc thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Cập nhật công việc thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonDeleteTask.setOnClickListener(v -> {
            boolean result = databaseHelper.deleteTask(taskId);
            if (result) {
                Toast.makeText(this, "Xóa công việc thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Xóa công việc thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProjects(int selectedProjectId) {
        List<Project> projects = databaseHelper.getAllProjects();
        List<String> projectNames = new ArrayList<>();
        int selectedPosition = 0;
        for (int i = 0; i < projects.size(); i++) {
            projectNames.add(projects.get(i).getName());
            if (projects.get(i).getId() == selectedProjectId) {
                selectedPosition = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projectNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProject.setAdapter(adapter);
        spinnerProject.setSelection(selectedPosition);
    }

    private void loadUsers(int selectedUserId) {
        List<User> users = databaseHelper.getAllUsers();
        List<String> userNames = new ArrayList<>();
        int selectedPosition = 0;
        for (int i = 0; i < users.size(); i++) {
            userNames.add(users.get(i).getName());
            if (users.get(i).getId() == selectedUserId) {
                selectedPosition = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapter);
        spinnerUser.setSelection(selectedPosition);
    }

    private void loadStatusOptions(String selectedStatus) {
        String[] statusOptions = {"Chưa bắt đầu", "Đang thực hiện", "Hoàn thành"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        for (int i = 0; i < statusOptions.length; i++) {
            if (statusOptions[i].equals(selectedStatus)) {
                spinnerStatus.setSelection(i);
                break;
            }
        }
    }
}