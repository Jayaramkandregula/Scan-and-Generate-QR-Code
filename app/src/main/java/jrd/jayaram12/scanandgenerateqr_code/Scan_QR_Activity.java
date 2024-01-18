package jrd.jayaram12.scanandgenerateqr_code;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Scan_QR_Activity extends AppCompatActivity {
    private ImageView scan_iv;
    private CardView scan_cv;
    private TextView res_tv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.your_status_bar_color));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.your_navigation_bar_color));
        res_tv=findViewById(R.id.Result_tv);
        scan_iv=findViewById(R.id.scan_QR_code_iv);
        scan_cv=findViewById(R.id.scan_QR_code_card_View);
        scanQRCode();
        scan_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode();
            }
        });
        scan_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode();
            }
        });

        res_tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(Scan_QR_Activity.this);
                alertDialog.setTitle("Copy Text To Clipboard");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        copyToClipBoard();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.show();
                //copyToClipBoard();
                return false;
            }
        });
    }

    private void copyToClipBoard() {
        ClipboardManager clipboard=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip=ClipData.newPlainText("Copied Text:",res_tv.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    private void scanQRCode() {
        ScanOptions options=new ScanOptions();
        options.setPrompt("Volume up to on Flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(QRCaptureActivity.class);
        barLaucher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            res_tv.setText(result.getContents());
        }
    });

}