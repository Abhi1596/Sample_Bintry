package com.example.sys1159.sample_itwdc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.BreakIterator;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private static String FILE = "";

    TextView textlabel;
    ArrayList StoreContacts=new ArrayList();
    StringBuilder sb=new StringBuilder();


    public String str= "";
     Image image;
    Button save;
    EditText editText;
     ImageView img;
    Bitmap bmp;
     Bitmap bt;
     byte[] bArray;

    private Cursor cursor;
    String name,ACCOUNT_TYPE_AND_DATA_SET_Phone,phonenumber;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//        try{
//            getActionBar().hide();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        GetContactsIntoArrayList();
        img = (ImageView) findViewById(R.id.logo);
        final Dialog dialog= new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.save_as);
        editText= (EditText) dialog.findViewById(R.id.saveas);
        save= (Button) dialog.findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    Log.d("Abhi ", "entered  keypad remove" );
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                dialog.dismiss();
                convertPDF();

            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();


            }
        });




    }
    public void convertPDF(){
        try {
            str= editText.getText().toString();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            if(str.length()!=0 && !str.equals("")) {
                Rectangle pagesize = new Rectangle(height / 1.5f, (height -148 )+ width);

                Document document = new Document(pagesize);


                PdfWriter.getInstance(document, new FileOutputStream("mnt/sdcard/ITWinner_" + str + "_DC.pdf"));
                Log.d("Abhi ", "Abhilash " + str);
                document.open();

                addImage(document);
                document.close();
            }else {
                Toast.makeText(MainActivity.this,"Enter details ",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".pdf";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }*/

   /* private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image");
        startActivity(intent);
    }*/


    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
// cursor = getContentResolver().query(
// ContactsContract.Data.CONTENT_URI
// ,new String[] { ContactsContract.Contacts.Data._ID }
// ,"mimetype=?",
// new String[] { "vnd.android.cursor.item/vnd.com.whatsapp.profile" }, null);

// cursor = getContentResolver().query(
// ContactsContract.Data.CONTENT_URI
// ,null,null,null, null);




        while (cursor.moveToNext()) {

             name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ACCOUNT_TYPE_AND_DATA_SET_Phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ACCOUNT_TYPE_AND_DATA_SET));
            ACCOUNT_TYPE_AND_DATA_SET_Phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.ACCOUNT_TYPE_AND_DATA_SET));

            StoreContacts.add(name + " " + ":" + " " + phonenumber+"ACCOUNT_TYPE_AND_DATA_SET"+ACCOUNT_TYPE_AND_DATA_SET_Phone);
            Log.d("sree","ACCOUNT_TYPE.."+ACCOUNT_TYPE_AND_DATA_SET_Phone+"-phonenumber-"+phonenumber+"-Name-"+name);
            sb.append("ACCOUNT_TYPE.."+ACCOUNT_TYPE_AND_DATA_SET_Phone+"-phonenumber-"+phonenumber+"-Name-"+name+"\n");

            Log.d("abhi ",""+sb.toString()+"Email "+name);
            Log.d("abhi l",""+"Email "+name);
        }

        cursor.close();

    }
    private  void addImage(Document document) {

        try {

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap= Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bArray = baos.toByteArray();
            image = Image.getInstance(bArray);  ///Here i set byte array..you can do bitmap to byte array and set in image...
        } catch (BadElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}