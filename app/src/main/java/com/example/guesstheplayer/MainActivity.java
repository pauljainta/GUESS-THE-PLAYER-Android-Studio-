package com.example.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    GridLayout gridLayout;
    ImageView imageView;
    int name_index;
    ArrayList<String> arrayList;

    HashMap<String,String> hashMap;

    public void GO(View view)
    {
        Button button=(Button) view;
        if(button.getText().toString().equalsIgnoreCase(arrayList.get(name_index)))
        {
            Toast.makeText(this,"CORRECT",Toast.LENGTH_SHORT).show();
            Log.i("MESSAGE","CORRECT");

        }
        else
        {
            Toast.makeText(this,"WRONG",Toast.LENGTH_SHORT).show();
            Log.i("MESSAGE","WRONG");
        }

        name_index=new Random().nextInt(arrayList.size());
        int button_index=new Random().nextInt(gridLayout.getChildCount());
        Button real_button=(Button) gridLayout.getChildAt(button_index);

        download(hashMap.get(arrayList.get(name_index)));
        real_button.setText(arrayList.get(name_index));

        for(int i=0;i<gridLayout.getChildCount();i++)
        {
            if(i!=button_index)
            {
                 button= (Button) gridLayout.getChildAt(i);
                int index=new Random().nextInt(arrayList.size());
                while (index==name_index)
                {
                    index=new Random().nextInt(arrayList.size());
                }
                button.setText(arrayList.get(index));


            }

        }


    }



    public class DownloadImage extends AsyncTask<String,Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url = null;
            HttpURLConnection httpURLConnection = null;


            try {
                url = new URL(urls[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                return BitmapFactory.decodeStream(httpURLConnection.getInputStream());


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        };
    }


    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            URL url=null;
            HttpURLConnection httpURLConnection=null;
            InputStreamReader inputStreamReader=null;
            int data = 0;
            String res="";




            try {

                url=new URL(urls[0]);
                httpURLConnection= (HttpURLConnection) url.openConnection();
                inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
                data=inputStreamReader.read();

                while (data!=-1)
                {
                    char curr=(char) data;
                    res+=curr;
                    data=inputStreamReader.read();

                }
                // Log.i("URL",urls[0]+" "+urls[1]);
                return res;


            } catch (Exception e) {
                e.printStackTrace();
            }






            return "FAILED";
        }
    }


    public void download(String url) {
        DownloadImage task = new DownloadImage();

        try {
            Bitmap bitmap = task.execute(url).get();
            imageView.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout=findViewById(R.id.gridLayout);
        imageView=findViewById(R.id.imageView);
        hashMap=new HashMap<>();

         arrayList=new ArrayList<>();

        DownloadTask task=new DownloadTask();
        String result=null;
        try {
            result= task.execute("https://www.transfermarkt.com/fc-barcelona/startseite/verein/131").get();
       //     Log.i("RESULT",result+"");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        Pattern pattern=Pattern.compile("<a href=\"#\">.*bilderrahmen-fixed lazy lazy");
        Matcher matcher = pattern.matcher(result);

        

        int count = 0;

        while(matcher.find()) {
            count++;


            String whole_string=matcher.group();

          Pattern  pattern1=Pattern.compile("=\".*?\"");
            Matcher matcher1=pattern1.matcher(whole_string);
            Log.i("WHole",whole_string);

            int count2=0;
            String img_src=null;
            String player_name=null;

            while(matcher1.find())
            {
                 count2++;
                 String str=matcher1.group();
                 if(count2==2)
                 {
                        img_src=str.substring(2,str.length()-1);
                        Log.i("img-src",img_src);
                 }

                if(count2==4)
                {
                    player_name=str.substring(2,str.length()-1);
                    Log.i("player-name",player_name);
                }

                if(player_name!=null && img_src!=null)
                {
                     arrayList.add(player_name);
                    hashMap.put(player_name,img_src);
                }


            }

        }


             name_index=new Random().nextInt(arrayList.size());
            int button_index=new Random().nextInt(gridLayout.getChildCount());
            Button real_button=(Button) gridLayout.getChildAt(button_index);

            download(hashMap.get(arrayList.get(name_index)));
            real_button.setText(arrayList.get(name_index));

            for(int i=0;i<gridLayout.getChildCount();i++)
            {
                if(i!=button_index)
                {
                    Button button= (Button) gridLayout.getChildAt(i);
                    int index=new Random().nextInt(arrayList.size());
                    while (index==name_index)
                    {
                        index=new Random().nextInt(arrayList.size());
                    }
                    button.setText(arrayList.get(index));


                }

            }

            for(int i=0;i<4;i++)
            {
                Button button=(Button) gridLayout.getChildAt(i);
                Log.i(i+"",button.getText().toString());
            }













    }
}