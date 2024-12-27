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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity5 extends AppCompatActivity {
    private Button button_home;
    private Button button_clear;
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
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("resultlog")) {
            String result = intent.getStringExtra("resultlog");
            if (result != null && !result.isEmpty()) {
                addResult(result); // 新增到清單並更新 UI
            }
        }

        // 設定按鈕事件
        setButtonClickListeners();
    }

    private void initializeUI() {
        resultListView = findViewById(R.id.resultListView);
        button_home = findViewById(R.id.button_home);
        button_clear = findViewById(R.id.button_clear);

        resultList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
        resultListView.setAdapter(adapter);
    }

    private void setButtonClickListeners() {
        button_home.setOnClickListener(v -> home());
        button_clear.setOnClickListener(v -> clearHistory());
    }

    private void home() {
        Intent intent = new Intent(MainActivity5.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> resultSet = prefs.getStringSet(RESULT_KEY, new LinkedHashSet<>());

        resultList.clear();
        resultList.addAll(resultSet);

        // 根據時間戳記排序，從新到舊排列
        Collections.sort(resultList, (a, b) -> {
            try {
                String timestampA = a.split(" - ")[0];
                String timestampB = b.split(" - ")[0];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date dateA = sdf.parse(timestampA);
                Date dateB = sdf.parse(timestampB);
                return dateB.compareTo(dateA);
            } catch (Exception e) {
                return 0;
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void addResult(String result) {
        // 添加時間戳記到結果
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String resultWithTimestamp = timestamp + " - " + result;

        // 確保清單不超過 10 筆
        if (resultList.size() >= MAX_RESULTS) {
            resultList.remove(0); // 移除最舊的一筆資料
        }

        resultList.add(resultWithTimestamp);

        // 根據時間戳記排序，從新到舊排列
        Collections.sort(resultList, (a, b) -> {
            try {
                String timestampA = a.split(" - ")[0];
                String timestampB = b.split(" - ")[0];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date dateA = sdf.parse(timestampA);
                Date dateB = sdf.parse(timestampB);
                return dateB.compareTo(dateA);
            } catch (Exception e) {
                return 0;
            }
        });

        adapter.notifyDataSetChanged();
        saveHistory();
    }


    private void clearHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(RESULT_KEY);
        editor.apply();

        resultList.clear();
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "所有記錄已清除", Toast.LENGTH_SHORT).show();
    }

    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> resultSet = new LinkedHashSet<>(resultList);
        editor.putStringSet(RESULT_KEY, resultSet);
        editor.apply();
    }
}
