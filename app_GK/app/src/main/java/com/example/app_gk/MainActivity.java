package com.example.app_gk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textViewUserInfo;
    private Button buttonCreateProject, buttonManageTasks;
    private ListView listViewAllTasks, listViewTasks; // Thêm listViewAllTasks
    private DatabaseHelper databaseHelper;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra và khởi tạo các view
        textViewUserInfo = findViewById(R.id.textViewUserInfo);
        buttonCreateProject = findViewById(R.id.buttonCreateProject);
        buttonManageTasks = findViewById(R.id.buttonManageTasks);
        listViewAllTasks = findViewById(R.id.listViewAllTasks); // Khởi tạo listViewAllTasks
        listViewTasks = findViewById(R.id.listViewTasks);

        if (textViewUserInfo == null || buttonCreateProject == null || buttonManageTasks == null || listViewTasks == null || listViewAllTasks == null) {
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

        String userName = getIntent().getStringExtra("user_name");
        String userRole = getIntent().getStringExtra("user_role");
        String userEmail = getIntent().getStringExtra("user_email");

        Log.d(TAG, "User email: " + userEmail);
        Log.d(TAG, "User name: " + userName);
        Log.d(TAG, "User role: " + userRole);

        if (userEmail == null) {
            Log.e(TAG, "User email is null. Redirecting to LoginActivity.");
            Toast.makeText(this, "Vui lòng đăng nhập trước", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        try {
            currentUser = databaseHelper.getUserByEmail(userEmail);
            if (currentUser == null) {
                Log.e(TAG, "User not found for email: " + userEmail);
                Toast.makeText(this, "Lỗi: Không tìm thấy người dùng", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            Log.d(TAG, "Current user ID: " + currentUser.getId());

            textViewUserInfo.setText("Tên: " + userName + "\nVai trò: " + userRole);

            if (userRole.equals("PM")) {
                buttonCreateProject.setVisibility(View.VISIBLE);
                buttonManageTasks.setVisibility(View.VISIBLE);
                listViewAllTasks.setVisibility(View.VISIBLE); // Hiển thị danh sách tất cả công việc

                buttonCreateProject.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, CreateProjectActivity.class);
                    startActivity(intent);
                });

                buttonManageTasks.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                    startActivity(intent);
                });

                loadAllTasksForPM(); // Tải danh sách tất cả công việc
            } else {
                listViewTasks.setVisibility(View.VISIBLE);
                loadTasksForUser(currentUser.getId());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in MainActivity: " + e.getMessage());
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadAllTasksForPM() {
        try {
            List<Task> tasks = databaseHelper.getAllTasks();
            List<String> taskDescriptions = new ArrayList<>();
            for (Task task : tasks) {
                taskDescriptions.add(task.getName() + " - " + task.getDescription() + " (" + task.getStatus() + ")");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskDescriptions);
            listViewAllTasks.setAdapter(adapter);

            listViewAllTasks.setOnItemClickListener((parent, view, position, id) -> {
                Task selectedTask = tasks.get(position);
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("task_id", selectedTask.getId());
                intent.putExtra("task_name", selectedTask.getName());
                intent.putExtra("task_description", selectedTask.getDescription());
                intent.putExtra("project_id", selectedTask.getProjectId());
                intent.putExtra("assigned_user_id", selectedTask.getAssignedUserId());
                intent.putExtra("task_status", selectedTask.getStatus());
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e(TAG, "Error loading all tasks: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi tải danh sách công việc: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadTasksForUser(int userId) {
        try {
            Log.d(TAG, "Loading tasks for user ID: " + userId);
            List<Task> tasks = databaseHelper.getTasksByUser(userId);
            Log.d(TAG, "Number of tasks: " + tasks.size());
            List<String> taskDescriptions = new ArrayList<>();
            for (Task task : tasks) {
                taskDescriptions.add(task.getName() + " - " + task.getDescription() + " (" + task.getStatus() + ")");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskDescriptions);
            listViewTasks.setAdapter(adapter);

            listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
                Task selectedTask = tasks.get(position);
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("task_id", selectedTask.getId());
                intent.putExtra("task_name", selectedTask.getName());
                intent.putExtra("task_description", selectedTask.getDescription());
                intent.putExtra("project_id", selectedTask.getProjectId());
                intent.putExtra("assigned_user_id", selectedTask.getAssignedUserId());
                intent.putExtra("task_status", selectedTask.getStatus());
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e(TAG, "Error loading tasks: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi tải danh sách công việc: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser != null) {
            if (currentUser.getRole().equals("PM")) {
                loadAllTasksForPM(); // Cập nhật danh sách công việc khi quay lại
            } else {
                loadTasksForUser(currentUser.getId());
            }
        }
    }
}