package com.example.final_project_test.History;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_test.MainActivity;
import com.example.final_project_test.R;
import com.example.final_project_test.utils.SharedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity5 extends AppCompatActivity {

    private ListView historyListView;
    private Button clearHistoryButton;
    private Button homeButton;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        historyListView = findViewById(R.id.historyListView);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        homeButton = findViewById(R.id.homeButton);

        clearHistoryButton.setOnClickListener(v -> clearHistory());
        homeButton.setOnClickListener(v -> returnToHome());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        List<String> historyList = SharedData.getInstance().getHistoryList();

        if (historyList == null || historyList.isEmpty()) {
            Toast.makeText(this, "沒有歷史紀錄可顯示", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Collections.sort(historyList, Comparator.comparingLong(this::extractTimestamp));
        } catch (Exception e) {
            Toast.makeText(this, "歷史紀錄解析錯誤", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // 移除時間戳記，只顯示運算結果部分
        List<String> displayList = new ArrayList<>();
        for (String record : historyList) {
            String[] parts = record.split("#", 2);
            if (parts.length == 2) {
                displayList.add(parts[1]); // 只添加結果部分
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        historyListView.setAdapter(adapter);
    }

    private long extractTimestamp(String record) {
        try {
            String[] parts = record.split("#");
            if (parts.length > 1) {
                return Long.parseLong(parts[0]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void clearHistory() {
        SharedData.getInstance().clearHistory();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "歷史紀錄已清除", Toast.LENGTH_SHORT).show();
    }

    private void returnToHome() {
        Intent intent = new Intent(MainActivity5.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
