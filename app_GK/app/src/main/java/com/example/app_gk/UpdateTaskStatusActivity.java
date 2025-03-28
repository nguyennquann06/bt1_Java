package com.example.app_gk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateTaskStatusActivity extends AppCompatActivity {

    private TextView textViewTaskInfo;
    private Spinner spinnerStatus;
    private Button buttonUpdateStatus;
    private DatabaseHelper databaseHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task_status);

        textViewTaskInfo = findViewById(R.id.textViewTaskInfo);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonUpdateStatus = findViewById(R.id.buttonUpdateStatus);
        databaseHelper = new DatabaseHelper(this);

        // Lấy thông tin công việc từ Intent
        taskId = getIntent().getIntExtra("task_id", -1);
        String taskName = getIntent().getStringExtra("task_name");
        String taskDescription = getIntent().getStringExtra("task_description");
        String taskStatus = getIntent().getStringExtra("task_status");

        textViewTaskInfo.setText("Tên: " + taskName + "\nMô tả: " + taskDescription);

        // Load danh sách trạng thái
        String[] statuses = {"Chưa bắt đầu", "Đang thực hiện", "Hoàn thành"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        spinnerStatus.setAdapter(statusAdapter);
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(taskStatus)) {
                spinnerStatus.setSelection(i);
                break;
            }
        }

        buttonUpdateStatus.setOnClickListener(v -> {
            String newStatus = spinnerStatus.getSelectedItem().toString();
            databaseHelper.updateTaskStatus(taskId, newStatus);
            Toast.makeText(this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}