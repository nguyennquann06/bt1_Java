package com.example.app_gk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    private static final String TABLE_PROJECTS = "projects";
    private static final String COLUMN_PROJECT_ID = "project_id";
    private static final String COLUMN_PROJECT_NAME = "project_name";
    private static final String COLUMN_PROJECT_DESCRIPTION = "description";

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_TASK_DESCRIPTION = "task_description";
    private static final String COLUMN_TASK_STATUS = "status";
    private static final String COLUMN_PROJECT_FK = "project_id";
    private static final String COLUMN_ASSIGNED_USER_ID = "assigned_user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(createUsersTableQuery);

        String createProjectsTableQuery = "CREATE TABLE " + TABLE_PROJECTS + " (" +
                COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PROJECT_NAME + " TEXT, " +
                COLUMN_PROJECT_DESCRIPTION + " TEXT)";
        db.execSQL(createProjectsTableQuery);

        String createTasksTableQuery = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_DESCRIPTION + " TEXT, " +
                COLUMN_TASK_STATUS + " TEXT, " +
                COLUMN_PROJECT_FK + " INTEGER, " +
                COLUMN_ASSIGNED_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_PROJECT_FK + ") REFERENCES " + TABLE_PROJECTS + "(" + COLUMN_PROJECT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_ASSIGNED_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createTasksTableQuery);

        insertDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertDefaultData(SQLiteDatabase db) {
        insertUser(db, "Nguyễn Văn A", "pmanager@company.com", "password1", "PM");
        insertUser(db, "Trần Văn B", "dev1@company.com", "password2", "Dev");
        insertUser(db, "Lê Thị C", "dev2@company.com", "password3", "Dev");
        insertUser(db, "Phạm Văn D", "tester@company.com", "password4", "Tester");

        insertProject(db, "App Bán Hàng Online", "Xây dựng ứng dụng bán hàng");
        insertProject(db, "Hệ thống e-Learning", "Website học trực tuyến");

        insertTask(db, "Thiết kế giao diện", "Thiết kế màn hình chính", "Chưa bắt đầu", 1, 2);
        insertTask(db, "Code API đăng nhập", "Xử lý login/register", "Đang thực hiện", 1, 3);
        insertTask(db, "Viết test đăng ký", "Kiểm thử form đăng ký", "Hoàn thành", 1, 4);
        insertTask(db, "Xây dựng giao diện khóa học", "Màn hình danh sách khóa học", "Chưa bắt đầu", 2, 2);
    }

    private void insertUser(SQLiteDatabase db, String name, String email, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        db.insert(TABLE_USERS, null, values);
    }

    private void insertProject(SQLiteDatabase db, String name, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROJECT_NAME, name);
        values.put(COLUMN_PROJECT_DESCRIPTION, description);
        db.insert(TABLE_PROJECTS, null, values);
    }

    private void insertTask(SQLiteDatabase db, String name, String description, String status, int projectId, int userId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, name);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_STATUS, status);
        values.put(COLUMN_PROJECT_FK, projectId);
        values.put(COLUMN_ASSIGNED_USER_ID, userId);
        db.insert(TABLE_TASKS, null, values);
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_ROLE)));
        }
        cursor.close();
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_ROLE)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public long addProject(String projectName, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROJECT_NAME, projectName);
        values.put(COLUMN_PROJECT_DESCRIPTION, description);
        long result = db.insert(TABLE_PROJECTS, null, values);
        Log.d("DatabaseHelper", "addProject result: " + result + " for project: " + projectName);
        return result;
    }

    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROJECTS, null);
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PROJECT_ID)));
                project.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_NAME)));
                project.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_DESCRIPTION)));
                projectList.add(project);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return projectList;
    }

    public long addTask(String taskName, String description, String status, int projectId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_STATUS, status);
        values.put(COLUMN_PROJECT_FK, projectId);
        values.put(COLUMN_ASSIGNED_USER_ID, userId);
        long result = db.insert(TABLE_TASKS, null, values);
        Log.d("DatabaseHelper", "addTask result: " + result + " for task: " + taskName);
        return result;
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_ASSIGNED_USER_ID + " = ?";
        Log.d("DatabaseHelper", "Querying tasks for user ID: " + userId);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_NAME)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_STATUS)));
                task.setProjectId(cursor.getInt(cursor.getColumnIndex(COLUMN_PROJECT_FK)));
                task.setAssignedUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_ASSIGNED_USER_ID)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("DatabaseHelper", "Found " + taskList.size() + " tasks for user ID: " + userId);
        return taskList;
    }

    // Thêm phương thức mới để lấy tất cả công việc
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS;
        Log.d("DatabaseHelper", "Querying all tasks");
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_NAME)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_STATUS)));
                task.setProjectId(cursor.getInt(cursor.getColumnIndex(COLUMN_PROJECT_FK)));
                task.setAssignedUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_ASSIGNED_USER_ID)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("DatabaseHelper", "Found " + taskList.size() + " tasks");
        return taskList;
    }

    public boolean updateTaskStatus(int taskId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_STATUS, status);
        int rowsAffected = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        return rowsAffected > 0;
    }

    public boolean updateTask(int taskId, String taskName, String description, int projectId, int assignedUserId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_PROJECT_FK, projectId);
        values.put(COLUMN_ASSIGNED_USER_ID, assignedUserId);
        values.put(COLUMN_TASK_STATUS, status);
        int rowsAffected = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        Log.d("DatabaseHelper", "updateTask: " + rowsAffected + " rows affected for task ID: " + taskId);
        return rowsAffected > 0;
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        Log.d("DatabaseHelper", "deleteTask: " + rowsAffected + " rows affected for task ID: " + taskId);
        return rowsAffected > 0;
    }
}