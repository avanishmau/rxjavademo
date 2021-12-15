package infor.avanish.demo.aes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import infor.avanish.demo.R;

public class AesMainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1000;
    private static final File DESTINY_DIR = new File("/sdcard/avanish/");

    Button select, output;
    ImageView inImage;
    ImageView ouImage;
    TextView result;

    //byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aes_main);

        initObject();
        listener();
    }

    private void listener() {
        select.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });

        output.setOnClickListener(v -> {
            byte[] bitmapdata = AES.decrypt( getFile("test1"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            ouImage.setImageBitmap(bitmap);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            inImage.setImageURI(selectedImage);

            Bitmap bitmap = ((BitmapDrawable)inImage.getDrawable()).getBitmap();

            byte[] encryptedByte = AES.encrypt(getByteFromMixedImage(bitmap, 100));

            saveFile(encryptedByte, "test1");

            result.setText("Ready to decrypt...");
        }
    }

    public static void saveFile(byte[] data, String fileName) throws RuntimeException {
        if (!DESTINY_DIR.exists() && !DESTINY_DIR.mkdirs()) {
            return;/*from   w  w w  .j  a  va 2 s .  com*/
        }
        File mainPicture = new File(DESTINY_DIR, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(mainPicture);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("Image could not be saved.", e);
        }
    }


    public static byte[] getFile(String fileName) throws RuntimeException {
        if (!DESTINY_DIR.exists() && !DESTINY_DIR.mkdirs()) {
            return null;
        }
        File file = new File(DESTINY_DIR, fileName);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (Exception e) {
            throw new RuntimeException("Image could not be saved.", e);
        }
        return bytes;
    }

    private void initObject() {
        select = findViewById(R.id.select_img);
        inImage = findViewById(R.id.selected_img);
        ouImage = findViewById(R.id.after_decoding);
        result = findViewById(R.id.result_tv);
        output = findViewById(R.id.output);
    }

    byte[] mB;
    public byte[] getByteFromMixedImage(Bitmap img, int quality)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, quality, baos); // bm is the bitmap object
        mB = baos.toByteArray();
        return mB;
    }
}