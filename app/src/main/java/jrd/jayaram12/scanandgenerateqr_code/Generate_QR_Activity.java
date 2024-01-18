package jrd.jayaram12.scanandgenerateqr_code;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Generate_QR_Activity extends AppCompatActivity {
    private ImageView gen_IV;
    private Button gen_QR_btn;
    private TextInputEditText text_et;
    private TextView gen_qr_code_tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.your_status_bar_color));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.your_navigation_bar_color));

        gen_IV=findViewById(R.id.gen_qr_iv);
        gen_QR_btn=findViewById(R.id.gen_QR_btn);
        text_et=findViewById(R.id.gen_editText);
        gen_qr_code_tv=findViewById(R.id.gen_qr_code_appear_tv);
        BitMatrix bitMatrix=null;
        gen_QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=text_et.getText().toString();
                if(text.isEmpty()){
                    Toast.makeText(Generate_QR_Activity.this, "Enter Some Data!", Toast.LENGTH_SHORT).show();
                }else{
                    WindowManager manager=(WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display=manager.getDefaultDisplay();
                    Point point=new Point();
                    display.getSize(point);
                    int width=point.x;
                    int height=point.y;
                    int dimen= Math.min(width, height);
                    dimen=dimen*3/4;
                    QRCodeWriter qrCodeWriter=new QRCodeWriter();

                    try{
                        BitMatrix bitMatrix=qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,dimen,dimen);
                        int w = bitMatrix.getWidth();
                        int h = bitMatrix.getHeight();
                        int[] pixels = new int[dimen * dimen];
                        for (int y = 0; y < h; y++) {
                            for (int x = 0; x < w; x++) {
                                pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                            }
                        }
                        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
                        gen_qr_code_tv.setVisibility(View.INVISIBLE);
                        gen_IV.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.d("ErrorMessage",e.toString());
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        gen_IV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(Generate_QR_Activity.this);
                alertDialog.setTitle("Save The QR Code?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveQRCodeImage();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                BitmapDrawable bitmapDrawable=(BitmapDrawable) gen_IV.getDrawable();
                if(bitmapDrawable!=null) {
                    alertDialog.show();
                }
                //saveQRCodeImage();
                return true;
            }
        });

    }
    private void saveQRCodeImage(){
        BitmapDrawable bitmapDrawable=(BitmapDrawable) gen_IV.getDrawable();
        if(bitmapDrawable!=null){
            Bitmap bitmap=bitmapDrawable.getBitmap();
            String root= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File myDir=new File(root+"/QRCodeImages");
            myDir.mkdirs();
            String filename="QRCodeImage_"+System.currentTimeMillis()+".jpg";
            File file=new File(myDir,filename);

            try(FileOutputStream out=new FileOutputStream(file)){
                bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
                Toast.makeText(Generate_QR_Activity.this,"Saved to Pictures",Toast.LENGTH_LONG).show();
                MediaScannerConnection.scanFile(
                        this,
                        new String[]{file.getAbsolutePath()},
                        new String[]{"image/jpeg"},
                        null
                );

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}