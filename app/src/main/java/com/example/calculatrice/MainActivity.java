package com.example.calculatrice;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    EditText editTextResult;
    TextView textView;
    Button btnAdd, btnSub, btnMulti, btnDiv, btnEqual, btnDot, btnModulo, btn_AC, btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextResult = findViewById(R.id.editTextResult);
        textView = findViewById(R.id.textView);

        btnAdd = findViewById(R.id.btn_add);
        btnSub = findViewById(R.id.btn_sub);
        btnMulti = findViewById(R.id.btn_mult);
        btnDiv = findViewById(R.id.btn_div);
        btnEqual = findViewById(R.id.btn_equal);
        btnDot = findViewById(R.id.btn_dot);
        btnModulo = findViewById(R.id.btn_modulo);

        btn_AC = findViewById(R.id.btn_AC);
        btn_del = findViewById(R.id.btn_del);

        initializeButtons();
        OperationButtons();
    }

    private void initializeButtons() { //pour initialiser les bottons de 0 à 9
        int[] buttonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn00};

        for (int btnId : buttonIds) {
            Button button = findViewById(btnId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button clickedButton = (Button) v;
                    String buttonText = clickedButton.getText().toString();
                    editTextResult.setText(editTextResult.getText() + buttonText);
                }
            });
        }
    }

    private void OperationButtons() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('+');
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('-');
            }
        });

        btnMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('*');
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('/');
            }
        });

        btnModulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('%');
            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equalOperation();
            }
        });

        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = editTextResult.getText().toString();

                if (!currentText.contains(".")) {
                    editTextResult.setText(currentText + ".");
                }
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = editTextResult.getText().toString();
                if (!(currentText == null || currentText.isEmpty())) {
                    editTextResult.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        btn_AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText("");
                textView.setText("");
            }
        });
    }

    private void Operation(char o) {
        if (editTextResult.getText().toString().isEmpty()) {
            String text = textView.getText().toString();
            int length = text.length();

            if (length > 0) {
                char lastChar = text.charAt(length - 1);
                if (lastChar == '*' || lastChar == '+' || lastChar == '-' || lastChar == '/' || lastChar == '%') {
                    text = text.substring(0, length - 1) + o;
                    textView.setText(text);
                }
                else{
                    textView.setText(textView.getText().toString()+ o);
                }
            }
        }
        else if(!editTextResult.getText().toString().isEmpty() || (textView.getText().toString().isEmpty() && (o == '-'))){
            textView.setText(textView.getText().toString() + editTextResult.getText().toString() + o);
            editTextResult.setText(null);
        }
    }

    private void equalOperation() {
        if (editTextResult.getText().toString().isEmpty()) {
            return;
        }
        textView.setText(textView.getText() + editTextResult.getText().toString());
        double result = calculateEquation(textView.getText().toString());
        editTextResult.setText(String.valueOf(result));
        textView.setText("");

    }

    // ************************
    public static double calculateEquation(String equation) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                // Gather entire number
                while (i + 1 < equation.length() && (Character.isDigit(equation.charAt(i + 1)) || equation.charAt(i + 1) == '.')) {
                    sb.append(equation.charAt(++i));
                }
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                // Process higher precedence operators
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        // Process remaining operators
        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        // The result should be the only number left on the stack
        return numbers.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        return (op1 == '*' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-');
    }

    private static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case '%':
                return a % b;
        }
        return 0;
    }
    // ************************


    // switch to the scientific calculator
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        }
        finish();
    }

}
