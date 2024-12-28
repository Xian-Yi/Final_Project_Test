package com.example.final_project_test.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_test.MainActivity;
import com.example.final_project_test.R;
import com.example.final_project_test.utils.SharedData;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity4 extends AppCompatActivity {

    private EditText inputEditText;
    private TextView resultTextView;

    private Button button_home;
    private Button button_plus, button_subtract, button_multiply, button_divided, button_equals,
            button_left_parentheses, button_right_parentheses, button_square, button_sqrt;
    private Button button0, button1, button2, button3, button4, button5, button6, button7,
            button8, button9, button_delete, button_dot, button_left, button_right, button_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // 初始化 UI 元件
        initializeUIElements();
        setButtonClickListeners();

        button_equals.setOnClickListener(v -> {
            String expression = inputEditText.getText().toString().trim();

            if (expression.isEmpty()) {
                Toast.makeText(MainActivity4.this, "請輸入數學表達式", Toast.LENGTH_SHORT).show();
                return;
            }

            String resultlog = evaluateExpression();

            if ("0".equals(resultlog) || resultlog.contains("運算錯誤")) {
                Toast.makeText(MainActivity4.this, "計算失敗，請檢查輸入", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(resultlog);
            saveToHistory(resultlog);
        });
    }

    // 初始化 UI 元件
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
        Button[] numberButtons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button_dot};
        String[] numberValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};

        for (int i = 0; i < numberButtons.length; i++) {
            String value = numberValues[i];
            numberButtons[i].setOnClickListener(v -> updateTextView(value));
        }

        Button[] operatorButtons = {button_plus, button_subtract, button_multiply, button_divided,
                button_left_parentheses, button_right_parentheses, button_square, button_sqrt};
        String[] operatorValues = {"+", "-", "×", "÷", "(", ")", "²", "√"};

        for (int i = 0; i < operatorButtons.length; i++) {
            String operator = operatorValues[i];
            operatorButtons[i].setOnClickListener(v -> updateTextView(operator));
        }

        button_return.setOnClickListener(v -> deleteOneCharacter());
        button_delete.setOnClickListener(v -> clearAllText());
        button_left.setOnClickListener(v -> moveCursorLeft());
        button_right.setOnClickListener(v -> moveCursorRight());
        button_home.setOnClickListener(v -> home());
    }

    private void updateTextView(String input) {
        inputEditText.requestFocus();
        String currentText = inputEditText.getText().toString();
        int cursorPosition = inputEditText.getSelectionStart();
        String newText = currentText.substring(0, cursorPosition) + input + currentText.substring(cursorPosition);
        inputEditText.setText(newText);
        inputEditText.setSelection(cursorPosition + input.length());
    }

    private void deleteOneCharacter() {
        String currentText = inputEditText.getText().toString();
        int cursorPosition = inputEditText.getSelectionStart();
        if (cursorPosition > 0) {
            currentText = currentText.substring(0, cursorPosition - 1) + currentText.substring(cursorPosition);
            inputEditText.setText(currentText);
            inputEditText.setSelection(cursorPosition - 1);
        }
    }

    private void clearAllText() {
        inputEditText.setText("");
        resultTextView.setText("");
    }

    private void moveCursorLeft() {
        int cursorPosition = inputEditText.getSelectionStart();
        if (cursorPosition > 0) {
            inputEditText.setSelection(cursorPosition - 1);
        }
    }

    private void moveCursorRight() {
        int cursorPosition = inputEditText.getSelectionStart();
        int textLength = inputEditText.getText().length();
        if (cursorPosition < textLength) {
            inputEditText.setSelection(cursorPosition + 1);
        }
    }

    private void home() {
        Intent intent = new Intent(MainActivity4.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveToHistory(String result) {
        long timestamp = System.currentTimeMillis();
        String historyEntry = timestamp + "#" + result;
        SharedData.getInstance().getHistoryList().add(historyEntry);
        Toast.makeText(this, "結果已保存到歷史記錄", Toast.LENGTH_SHORT).show();
    }

    private String evaluateExpression() {
        try {
            String expression = inputEditText.getText().toString().trim();
            expression = expression.replace("×", "*").replace("÷", "/")
                    .replace("²", "^2").replace("√", "sqrt");

            double result = new ExpressionBuilder(expression).build().evaluate();
            return expression + "=" + result;
        } catch (Exception e) {
            Toast.makeText(this, "運算錯誤", Toast.LENGTH_SHORT).show();
            return "0";
        }
    }
}
