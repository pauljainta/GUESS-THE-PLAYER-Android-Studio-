package com.example.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    GridLayout gridLayout;
    ImageView imageView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        

//        int count = 0;

//        while(matcher.find()) {
//            count++;
//            Log.i("found: " + count + " : ",
//                    + matcher.start() + " - " + matcher.end());
//        }


    }
}