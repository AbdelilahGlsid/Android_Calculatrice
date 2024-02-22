package com.example.calculatrice;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Stack;
import java.lang.Math;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity2 extends AppCompatActivity {
    EditText editTextResult;
    TextView textView;
    Button btnAdd, btnSub, btnMulti, btnDiv, btnEqual, btnDot, btnModulo, btn_AC;
    Button btnPar1, btnPar2, btnFactorial, btnLn, btnLog, btnRacine, btnPuissance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editTextResult = findViewById(R.id.editTextResult);
        textView = findViewById(R.id.textView);

        btnAdd = findViewById(R.id.btn_add);
        btnSub = findViewById(R.id.btn_sub);
        btnMulti = findViewById(R.id.btn_mult);
        btnDiv = findViewById(R.id.btn_div);
        btnEqual = findViewById(R.id.btn_equal);
        btnDot = findViewById(R.id.btn_dot);
        btnModulo = findViewById(R.id.btn_modulo);

        btnPar1 = findViewById(R.id.btn_par1);
        btnPar2 = findViewById(R.id.btn_par2);
        btnFactorial = findViewById(R.id.btn_factorial);
        btnLn = findViewById(R.id.btn_ln);
        btnLog = findViewById(R.id.btn_log);
        btnRacine = findViewById(R.id.btn_racine);
        btnPuissance = findViewById(R.id.btn_puissance);

        btn_AC = findViewById(R.id.btn_AC);
        //btn_del = findViewById(R.id.btn_del);

        initializeButtons();
        OperationButtons();
    }

    private void initializeButtons() { //pour initialiser les bottons de 0 à 9
        int[] buttonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

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

        /*btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = editTextResult.getText().toString();
                if (!(currentText == null || currentText.isEmpty())) {
                    editTextResult.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });*/

        btn_AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText("");
                textView.setText("");
            }
        });

        btnPar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText(editTextResult.getText() + "(");
            }
        });

        btnPar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText(editTextResult.getText() + ")");
            }
        });

        btnFactorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('!');
            }
        });

        btnLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText(editTextResult.getText() + "ln(");
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText(editTextResult.getText() + "log(");
            }
        });

        btnRacine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('√');
            }
        });

        btnPuissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation('^');
            }
        });
    }

    private void Operation(char o) {
        if (editTextResult.getText().toString().isEmpty()) {
            String text = textView.getText().toString();
            int length = text.length();

            if (length > 0) {
                char lastChar = text.charAt(length - 1);
                if (lastChar == '*' || lastChar == '+' || lastChar == '-' || lastChar == '/' || lastChar == '%' || lastChar == 's' || lastChar == 'n' || lastChar == 'g' || lastChar == 't') {
                    text = text.substring(0, length - 1) + o;
                    textView.setText(text);
                } else {
                    textView.setText(textView.getText().toString() + o);
                }
            }
        } else if (!editTextResult.getText().toString().isEmpty() || (textView.getText().toString().isEmpty() && (o == '-'))) {
            textView.setText(textView.getText().toString() + editTextResult.getText().toString() + o);
            editTextResult.setText(null);
        }
    }

    private void equalOperation() {
        if (editTextResult.getText().toString().isEmpty()) {
            return;
        }
        textView.setText(textView.getText() + editTextResult.getText().toString());
        String equation = textView.getText().toString();
        double result = calculateEquation(equation);
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

                while (i + 1 < equation.length() && (Character.isDigit(equation.charAt(i + 1)) || equation.charAt(i + 1) == '.')) {
                    sb.append(equation.charAt(++i));
                }
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^') {

                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            } else if (c == 's' || c == 'c' || c == 't' || c == 'l' || c == 'q' || c == '!') {
                if (c == '!') {
                    double number = numbers.pop();
                    numbers.push((double)factorial((int) number));
                } else if (c == 's') {
                    double number = numbers.pop();
                    numbers.push(Math.sin(Math.toRadians(number)));
                } else if (c == 'c') {
                    double number = numbers.pop();
                    numbers.push(Math.cos(Math.toRadians(number)));
                } else if (c == 't') {
                    double number = numbers.pop();
                    numbers.push(Math.tan(Math.toRadians(number)));
                } else if (c == 'l') {
                    double number = numbers.pop();
                    numbers.push(Math.log(number));
                } else if (c == 'q') {
                    double number = numbers.pop();
                    numbers.push(Math.sqrt(number));
                }
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        return (op1 == '*' || op1 == '/' || op1 == '%' || op1 == '^') && (op2 == '+' || op2 == '-');
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
            case '^':
                return Math.pow(a, b);
        }
        return 0;
    }

    private static int factorial(int n) {
        if (n == 0) return 1;
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // ************************


    // switch to the scientific calculator
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
