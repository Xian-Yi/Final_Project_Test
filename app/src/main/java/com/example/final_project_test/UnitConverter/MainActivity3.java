package com.example.final_project_test.UnitConverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.final_project_test.MainActivity;
import com.example.final_project_test.R;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity3 extends AppCompatActivity {
    private EditText inputEditText;
    private TextView resultTextView;
    private Spinner categorySpinner;
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private Button convertButton, button_home;
    private Button button_plus, button_subtract, button_multiply, button_divided, button_equals;
    private Button button0, button1, button2, button3, button4, button5, button6,button7,
            button8, button9, button_delete, button_dot,button_left, button_right, button_return;

    // 單位類別
    private String[] unitCategories = {"長度", "質量", "溫度", "能量", "面積", "體積", "角度"};

    // 長度單位
    private String[] lengthUnits = {"公尺(m)", "公里(km)", "公分(cm)", "毫米(mm)", "英吋(in)", "英尺(ft)", "碼(yd)"};

    // 重量單位
    private String[] weightUnits = {"公斤(kg)", "公噸(ton)", "克(g)", "磅(lb)", "盎司(oz)"};

    // 溫度單位
    private String[] temperatureUnits = {"攝氏(℃)", "華氏(℉)", "克氏(K)"};

    // 能量單位
    private String[] energyUnits = {"千焦(kJ)", "千卡(kcal)", "英熱單位(BTU)", "千瓦時(kWh)", "馬力時(hp·h)"};

    // 面積單位
    private String[] areaUnits = {"平方公尺(m²)", "平方公分(cm²)", "平方公釐(mm²)", "平方碼(yd²)", "平方英尺(ft²)", "平方英吋(in²)"};

    // 體積單位
    private String[] volumeUnits = {"立方公尺(m³)", "公秉(bbl)", "立方碼(yd³)", "立方英吋(in³)", "公升(L)", "加侖(gal)"};

    // 角度單位
    private String[] angleUnits = {"度(°)", "弧度(rad)", "弧分(′)", "弧秒(″)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // 初始化UI元件
        initializeUIElements();

        // 設定按鈕點擊事件
        setButtonClickListeners();

        // 設置類別下拉選單
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 將 categoryAdapter 設定為 categorySpinner 的資料來源
        categorySpinner.setAdapter(categoryAdapter);

        // 類別選擇監聽器，當使用者選擇不同的單位類別時，更新單位選擇器
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 使用者有做選擇時
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position); // 更新單位選擇器的選項
            }

            // 使用者沒有做選擇時
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 設定轉換按鈕的點擊事件
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();  // 當點擊轉換按鈕時，呼叫轉換方法
            }
        });

        // 預設初始化，設定初始的單位選項
        updateUnitSpinners(0);  // 根據索引 0（預設選項）更新單位選擇器
    }


    // 初始化UI元件
    private void initializeUIElements() {
        inputEditText = findViewById(R.id.inputEditText);
        resultTextView = findViewById(R.id.resultTextView);
        categorySpinner = findViewById(R.id.categorySpinner);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
        convertButton = findViewById(R.id.convertButton);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);

        button_home = findViewById(R.id.button_home);
        button_plus = findViewById(R.id.button_plus);
        button_subtract = findViewById(R.id.button_subtract);
        button_multiply = findViewById(R.id.button_multiply);
        button_divided = findViewById(R.id.button_divided);
        button_equals = findViewById(R.id.button_equals);
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

        // 運算符按鈕 (+, -, ×, ÷)
        Button[] operatorButtons = {button_plus, button_subtract, button_multiply, button_divided};
        String[] operatorValues = {"+", "-", "×", "÷"};

        for (int i = 0; i < operatorButtons.length; i++) {
            String operator = operatorValues[i]; // 取得對應的運算符
            operatorButtons[i].setOnClickListener(v -> updateTextView(operator)); // 將按下的運算符號插入到輸入框中
        }

        /* 特殊按鈕 */

        // 等於按鈕(運算)
        button_equals.setOnClickListener(v -> evaluateExpression());
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
        Intent intent = new Intent(MainActivity3.this, MainActivity.class);
        startActivity(intent); // 啟動主畫面 Activity
        finish(); // 結束當前畫面 (MainActivity2)，防止返回時仍顯示該畫面
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
    private void evaluateExpression() {
        try {
            // 取得 EditText 中的數學表達式並去除前後空白
            String expression = inputEditText.getText().toString().trim();

            // 替換運算符 "×" 和 "÷" 為 "*" 和 "/"，以便與 Java 的運算符相容
            expression = expression.replace("×", "*").replace("÷", "/");

            // 使用 evaluateMathExpression 方法計算結果
            double result = evaluateMathExpression(expression);

            // 顯示計算結果於 EditText
            inputEditText.setText(String.valueOf(result));
        } catch (Exception e) {
            // 當有錯誤發生時顯示錯誤訊息
            Toast.makeText(this, "運算錯誤", Toast.LENGTH_SHORT).show();
        }
    }

    //使用 exp4j 解析並計算數學表達式
    private double evaluateMathExpression(String expression) throws Exception {
        Expression e = new ExpressionBuilder(expression).build();
        return e.evaluate(); // 返回計算結果
    }

    // 根據選擇的類別更新單位下拉選單
    private void updateUnitSpinners(int categoryPosition) {
        ArrayAdapter<String> fromUnitAdapter;
        ArrayAdapter<String> toUnitAdapter;

        switch (categoryPosition) {
            case 0: // 長度
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lengthUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lengthUnits);
                break;
            case 1: // 重量
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightUnits);
                break;
            case 2: // 溫度
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, temperatureUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, temperatureUnits);
                break;
            case 3: // 能量
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, energyUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, energyUnits);
                break;
            case 4: // 面積
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areaUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areaUnits);
                break;
            case 5: // 體積
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, volumeUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, volumeUnits);
                break;
            case 6: // 角度
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, angleUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, angleUnits);
                break;
            default:
                return;
        }

        fromUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(fromUnitAdapter);
        toUnitSpinner.setAdapter(toUnitAdapter);
    }

    // 單位轉換邏輯
    private void convertUnits() {
        String inputText = inputEditText.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, "請輸入數值", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "請輸入有效的數值", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryPosition = categorySpinner.getSelectedItemPosition();
        int fromUnitPosition = fromUnitSpinner.getSelectedItemPosition();
        int toUnitPosition = toUnitSpinner.getSelectedItemPosition();

        double result = 0;

        switch (categoryPosition) {
            case 0: // 長度轉換
                result = convertLength(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 1: // 重量轉換
                result = convertWeight(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 2: // 溫度轉換
                result = convertTemperature(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 3: // 能量轉換
                result = convertEnergy(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 4: // 面積轉換
                result = convertArea(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 5: // 體積轉換
                result = convertVolume(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 6: // 角度轉換
                result = convertAngle(inputValue, fromUnitPosition, toUnitPosition);
                break;
        }

        // 顯示結果
        resultTextView.setText(String.format("%.4f", result));
    }
    // 長度轉換（以公尺為基準）
    private double convertLength(double value, int fromUnit, int toUnit) {
        double[] lengthConversionRates = {1, 1000, 0.01, 0.001, 0.0254, 0.3048, 0.9144};
        return value * lengthConversionRates[fromUnit] / lengthConversionRates[toUnit];
    }

    // 重量轉換（以公斤為基準）
    private double convertWeight(double value, int fromUnit, int toUnit) {
        double[] weightConversionRates = {1, 1000, 0.001, 0.45359237, 0.0283495};
        return value * weightConversionRates[fromUnit] / weightConversionRates[toUnit];
    }

    // 溫度轉換
    private double convertTemperature(double value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;

        // 先轉換成攝氏
        double celsius = value;
        switch (fromUnit) {
            case 0: // 攝氏
                celsius = value;
                break;
            case 1: // 華氏
                celsius = (value - 32) * 5 / 9;
                break;
            case 2: // 克氏
                celsius = value - 273.15;
                break;
        }

        // 從攝氏轉換到目標單位
        switch (toUnit) {
            case 0: // 攝氏
                return celsius;
            case 1: // 華氏
                return celsius * 9 / 5 + 32;
            case 2: // 克氏
                return celsius + 273.15;
        }

        return value;
    }

    // 能量轉換（以千焦為基準）
    private double convertEnergy(double value, int fromUnit, int toUnit) {
        double[] energyConversionRates = {1, 0.24, 0.95, 3.6, 3.6 * 1.34102};
        return value * energyConversionRates[fromUnit] / energyConversionRates[toUnit];
    }

    // 面積轉換（以平方公尺為基準）
    private double convertArea(double value, int fromUnit, int toUnit) {
        double[] areaConversionRates = {1, 10000, 1000000, 1.195990046, 10.7639104, 1550};
        return value * areaConversionRates[fromUnit] / areaConversionRates[toUnit];
    }

    // 體積轉換（以立方公尺為基準）
    private double convertVolume(double value, int fromUnit, int toUnit) {
        double[] volumeConversionRates = {1, 0.158987, 1.308, 61023.74, 1000, 264.172};
        return value * volumeConversionRates[fromUnit] / volumeConversionRates[toUnit];
    }

    // 角度轉換（360度 = 2π弧度 = 21600弧分 = 1296000弧秒）
    private double convertAngle(double value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;

        double radians = value;
        switch (fromUnit) {
            case 0: // 度
                radians = value * Math.PI / 180;
                break;
            case 1: // 弧度
                radians = value;
                break;
            case 2: // 弧分
                radians = value * Math.PI / 10800;
                break;
            case 3: // 弧秒
                radians = value * Math.PI / 648000;
                break;
        }

        switch (toUnit) {
            case 0: // 度
                return radians * 180 / Math.PI;
            case 1: // 弧度
                return radians;
            case 2: // 弧分
                return radians * 10800 / Math.PI;
            case 3: // 弧秒
                return radians * 648000 / Math.PI;
        }

        return value;
    }
}
