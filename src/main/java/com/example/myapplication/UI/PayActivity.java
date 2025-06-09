package com.example.myapplication.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class PayActivity extends AppCompatActivity {

    private EditText mtvPrice;
    private TextView mtvName;
    private RadioGroup mrdChoice;
    private RadioButton mrbWeiXin, mrbPay;
    private Button mPay;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhifu); // 确保布局文件名和你的一致

        initViews();
        initEvents();
        loadOrderInfo(); // 可选：从上一个页面传来的订单数据
    }

    private void initViews() {
        mtvPrice = findViewById(R.id.mtvPrice);
        mtvName = findViewById(R.id.mtvName);
        title = findViewById(R.id.title);
        mrdChoice = findViewById(R.id.mrdChoice);
        mrbWeiXin = findViewById(R.id.mrbWeiXin);
        mrbPay = findViewById(R.id.mrbPay);

        mPay = findViewById(R.id.mPay);
    }

    private void initEvents() {
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price = mtvPrice.getText().toString().trim();
                String name = mtvName.getText().toString().trim();

                String payMethod = mrbWeiXin.isChecked() ? "微信支付" :
                        mrbPay.isChecked() ? "支付宝支付" : "未知";

                if (price.isEmpty()) {
                    Toast.makeText(PayActivity.this, "请输入订单金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 这里只是模拟订单提交逻辑
                String orderSummary = "商品名称：" + name + "\n"
                        + "金额：" + price + "\n"
                        + "支付方式：" + payMethod + "\n";

                new AlertDialog.Builder(PayActivity.this)
                        .setTitle("订单确认")
                        .setMessage(orderSummary)
                        .setPositiveButton("提交订单", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(PayActivity.this, "订单已提交！", Toast.LENGTH_SHORT).show();
                                finish(); // 可选：关闭当前页面
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    // 示例：加载数据填入页面（可选）
    private void loadOrderInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("product_name");
            String defaultPrice = intent.getStringExtra("price");
            if (productName != null) mtvName.setText(productName);
            if (defaultPrice != null) mtvPrice.setText(defaultPrice);
        }
    }
}

