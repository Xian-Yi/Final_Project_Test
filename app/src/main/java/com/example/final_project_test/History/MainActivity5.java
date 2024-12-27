package com.example.final_project_test.History;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_test.MainActivity;
import com.example.final_project_test.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity5 extends AppCompatActivity {
    private Button button_home;
    private ListView resultListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> resultList;

    private static final int MAX_RESULTS = 10;
    private static final String PREFS_NAME = "ResultHistoryPrefs";
    private static final String RESULT_KEY = "result_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // 初始化 UI
        initializeUI();
        loadHistory(); // 載入儲存的歷史紀錄

        // 接收來自 MainActivity4 的資料
        String result = getIntent().getStringExtra("expression");

        if (result != null && !result.isEmpty()) {
            addResult(result); // 新增到清單並更新 UI
        }

        // 設定按鈕事件
        setButtonClickListeners();
    }

    private void initializeUI() {
        resultListView = findViewById(R.id.resultListView);
        button_home = findViewById(R.id.button_home);

        resultList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
        resultListView.setAdapter(adapter);
    }

    private void setButtonClickListeners() {
        button_home.setOnClickListener(v -> home());
    }

    private void home() {
        Intent intent = new Intent(MainActivity5.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addResult(String result) {
        // 確保清單不超過 10 筆
        if (resultList.size() >= MAX_RESULTS) {
            resultList.remove(0); // 移除最舊的一筆資料
        }

        resultList.add(result);
        adapter.notifyDataSetChanged(); // 更新 ListView

        saveHistory(); // 儲存到 SharedPreferences

        Toast.makeText(this, "結果已新增到記錄中", Toast.LENGTH_SHORT).show();
    }

    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> resultSet = new HashSet<>(resultList);
        editor.putStringSet(RESULT_KEY, resultSet);
        editor.apply();
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> resultSet = prefs.getStringSet(RESULT_KEY, new HashSet<>());

        resultList.clear();
        resultList.addAll(resultSet);

        // 確保顯示順序正確
        resultList.sort(String::compareTo);
        adapter.notifyDataSetChanged();
    }
}
