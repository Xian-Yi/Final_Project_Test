package com.example.final_project_test.calculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_test.History.MainActivity5;
import com.example.final_project_test.MainActivity;
import com.example.final_project_test.R;
import com.example.final_project_test.UnitConverter.MainActivity3;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity4 extends AppCompatActivity {
    private EditText inputEditText;
    private TextView resultTextView;

    private Button button_home;
    private Button button_plus, button_subtract, button_multiply, button_divided, button_equals,
            button_left_parentheses, button_right_parentheses, button_square, button_sqrt;
    private Button button0, button1, button2, button3, button4, button5, button6,button7,
            button8, button9, button_delete, button_dot,button_left, button_right, button_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // 初始化UI元件
        initializeUIElements();

        // 設定按鈕點擊事件
        setButtonClickListeners();

        // 設定'='按鈕的點擊事件
        button_equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = inputEditText.getText().toString().trim();

                // 檢查使用者是否輸入了數學表達式
                if (expression.isEmpty()) {
                    Toast.makeText(MainActivity4.this, "請輸入數學表達式", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 計算結果
                String resultlog = evaluateExpression();

                // 檢查計算結果是否有效
                if ("0".equals(resultlog) || resultlog.contains("運算錯誤")) {
                    Toast.makeText(MainActivity4.this, "計算失敗，請檢查輸入", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 顯示結果在當前畫面
                resultTextView.setText(resultlog);

                // 如果需要將結果保存到歷史記錄（可選）
                saveToHistory(resultlog);
            }
        });
    }


    // 初始化UI元件
    private void initializeUIElements() {
        inputEditText = findViewById(R.id.inputEditText);
        resultTextView = findViewById(R.id.resultTextView);

        button_home = findViewById(R.id.button_home);
        button_plus = findViewById(R.id.button_plus);
        button_subtract = findViewById(R.id.button_subtract);
        button_multiply = findViewById(R.id.button_multiply);
        button_divided = findViewById(R.id.button_divided);
        button_equals = findViewById(R.id.button_equals);

        button_left_parentheses = findViewById(R.id.button_left_parentheses);
        button_right_parentheses = findViewById(R.id.button_right_parentheses);
        button_square = findViewById(R.id.button_square);
        button_sqrt = findViewById(R.id.button_sqrt);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button_dot = findViewById(R.id.button_dot);
        button_return = findViewById(R.id.button_return);
        button_left = findViewById(R.id.button_left);
        button_right = findViewById(R.id.button_right);
        button_delete = findViewById(R.id.button_delete);
    }

    // 設置按鈕的 OnClickListener
    private void setButtonClickListeners() {
        // 數字按鈕 (0-9) 與點號
        // 定義數字按鈕的陣列，對應於 XML 中的按鈕元件
        Button[] numberButtons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button_dot};
        // 定義每個按鈕對應的輸入值 (數字或小數點)
        String[] numberValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};

        // 為每個數字按鈕設定點擊事件
        for (int i = 0; i < numberButtons.length; i++) {
            String value = numberValues[i]; // 取得對應的值
            numberButtons[i].setOnClickListener(v -> updateTextView(value)); // 將按下的數字或小數點插入到輸入框中
        }

        // 運算符按鈕 (+, -, ×, ÷, (, ), ²,√)
        Button[] operatorButtons = {button_plus, button_subtract, button_multiply, button_divided,
                button_left_parentheses, button_right_parentheses, button_square, button_sqrt};
        String[] operatorValues = {"+", "-", "×", "÷", "(", ")", "²", "√"};

        for (int i = 0; i < operatorButtons.length; i++) {
            String operator = operatorValues[i]; // 取得對應的運算符
            operatorButtons[i].setOnClickListener(v -> updateTextView(operator)); // 將按下的運算符號插入到輸入框中
        }

        /* 特殊按鈕 */

//        // 等於按鈕(運算)
//        button_equals.setOnClickListener(v -> evaluateExpression());
        // 刪除單一字元按鈕
        button_return.setOnClickListener(v -> deleteOneCharacter());
        // 刪除全部字元按鈕
        button_delete.setOnClickListener(v -> clearAllText());
        // 左移游標按鈕
        button_left.setOnClickListener(v -> moveCursorLeft());
        // 右移游標按鈕
        button_right.setOnClickListener(v -> moveCursorRight());
        // 返回主畫面按鈕
        button_home.setOnClickListener(v -> home());
    }

    // 返回主畫面
    private void home(){
        Intent intent = new Intent(MainActivity4.this, MainActivity.class);
        startActivity(intent); // 啟動主畫面 Activity
        finish(); // 結束當前畫面 (MainActivity4)，防止返回時仍顯示該畫面
    }

    // 更新 EditText 中的字元
    private void updateTextView(String input) {
        // 確保游標顯示在 EditText 中
        inputEditText.requestFocus();

        // 取得當前文字和游標位置
        String currentText = inputEditText.getText().toString();
        int cursorPosition = inputEditText.getSelectionStart();

        // 在游標所在位置插入新的字元
        String newText = currentText.substring(0, cursorPosition) + input + currentText.substring(cursorPosition);
        inputEditText.setText(newText); // 更新 EditText 的內容

        // 避免連續輸入多個小數點
        if (".".equals(input) && currentText.endsWith(".")) {
            return;
        }

        // 避免開頭輸入非法運算符
        if (currentText.isEmpty() && ("+".equals(input) || "-".equals(input) || "*".equals(input) || "/".equals(input))) {
            return;
        }

        // 設置游標位置到新插入字元的後方，確保使用者能繼續輸入
        inputEditText.setSelection(cursorPosition + input.length());
    }

    // 刪除單一字元
    private void deleteOneCharacter() {
        // 取得目前的內容
        String currentText = inputEditText.getText().toString();

        // 取得游標位置
        int cursorPosition = inputEditText.getSelectionStart();

        // 確保游標不在最前面
        if (cursorPosition > 0) {
            // 刪除游標前一個字元，並更新文字
            currentText = currentText.substring(0, cursorPosition - 1) + currentText.substring(cursorPosition);

            // 設定更新後的內容至 EditText
            inputEditText.setText(currentText);

            // 設置游標停留在刪除後的位置 (即游標往前移動一格)
            inputEditText.setSelection(cursorPosition - 1);
        }
    }

    // 刪除全部字元
    private void clearAllText() {
        // 清空 EditText 的內容
        inputEditText.setText("");
        resultTextView.setText("");
    }

    // 左移游標
    private void moveCursorLeft() {
        // 取得目前游標位置
        int cursorPosition = inputEditText.getSelectionStart();

        // 確保游標不會超出左邊界 (游標不能小於 0)
        if (cursorPosition > 0) {
            inputEditText.setSelection(cursorPosition - 1); // 將游標向左移動
        }
    }

    // 右移游標
    private void moveCursorRight() {
        // 取得目前游標位置
        int cursorPosition = inputEditText.getSelectionStart();

        //  // 取得 EditText 中文字的總長度
        int textLength = inputEditText.getText().length();

        // 確保游標不會超出右邊界 (游標不能大於文字長度)
        if (cursorPosition < textLength) {
            inputEditText.setSelection(cursorPosition + 1); // 將游標向右移動
        }
    }

    // 評估並計算表達式
    private String evaluateExpression() {
        try {
            // 取得 EditText 中的數學表達式並去除前後空白
            String expression = inputEditText.getText().toString().trim();

            // 替換運算符 "×" 和 "÷" 為 "*" 和 "/"，以便與 Java 的運算符相容
            expression = expression.replace("×", "*").replace("÷", "/")
                    .replace("²", "^2").replace("√", "sqrt");

            // 使用 evaluateMathExpression 方法計算結果
            double result = evaluateMathExpression(expression);
            if (result == (int) result) {
                resultTextView.setText(String.valueOf((int) result));

            } else {
                // 顯示計算結果於 resultText
                resultTextView.setText(String.valueOf(result));
            }
            return expression + "=" +  result;

        } catch (Exception e) {
            // 當有錯誤發生時顯示錯誤訊息
            Toast.makeText(this, "運算錯誤", Toast.LENGTH_SHORT).show();
            return "0";
        }
    }

    //使用 exp4j 解析並計算數學表達式
    private double evaluateMathExpression(String expression) throws Exception {
        Expression e = new ExpressionBuilder(expression).build();
        return e.evaluate(); // 返回計算結果
    }

    private void saveToHistory(String result) {
        SharedPreferences prefs = getSharedPreferences("ResultHistoryPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> resultSet = prefs.getStringSet("result_list", new LinkedHashSet<>());
        resultSet.add(result); // 添加新結果

        editor.putStringSet("result_list", resultSet);
        editor.apply();

        Toast.makeText(this, "結果已保存到歷史記錄", Toast.LENGTH_SHORT).show();
    }


}