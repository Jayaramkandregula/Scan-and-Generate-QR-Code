package jrd.jayaram12.scanandgenerateqr_code;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button generate_QR_btn,scan_QR_btn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d("Hello","1");
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.your_status_bar_color));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.your_navigation_bar_color));

        generate_QR_btn=findViewById(R.id.generate_QR_btn);
        scan_QR_btn=findViewById(R.id.scan_QR_btn);
        generate_QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Generate_QR_Activity.class));
            }
        });
        scan_QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Scan_QR_Activity.class));
            }
        });
    }
}