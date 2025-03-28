package com.example.app_gk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageTasksActivity extends AppCompatActivity {

    private Button buttonAddTask;
    private ListView listViewAllTasks;
    private DatabaseHelper databaseHelper;
    private List<Task> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);

        buttonAddTask = findViewById(R.id.buttonAddTask);
        listViewAllTasks = findViewById(R.id.listViewAllTasks);
        databaseHelper = new DatabaseHelper(this);

        buttonAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(ManageTasksActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        loadAllTasks();
    }

    private void loadAllTasks() {
        allTasks = new ArrayList<>();
        List<User> users = databaseHelper.getAllUsers();
        for (User user : users) {
            List<Task> userTasks = databaseHelper.getTasksByUser(user.getId());
            allTasks.addAll(userTasks);
        }

        List<String> taskDescriptions = new ArrayList<>();
        for (Task task : allTasks) {
            taskDescriptions.add(task.getName() + " - " + task.getDescription() + " (" + task.getStatus() + ")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskDescriptions);
        listViewAllTasks.setAdapter(adapter);

        listViewAllTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task selectedTask = allTasks.get(position);
            Intent intent = new Intent(ManageTasksActivity.this, EditTaskActivity.class);
            intent.putExtra("task_id", selectedTask.getId());
            intent.putExtra("task_name", selectedTask.getName());
            intent.putExtra("task_description", selectedTask.getDescription());
            intent.putExtra("task_status", selectedTask.getStatus());
            intent.putExtra("project_id", selectedTask.getProjectId());
            intent.putExtra("assigned_user_id", selectedTask.getAssignedUserId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllTasks();
    }
}