package com.example.networking;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyTask extends AsyncTask<String,Void,String> {

    private TextView result;

    public MyTask(TextView result) {
        this.result = result;
    }

    @Override
    protected String doInBackground(String... strings) {
        return goToNetwork(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        result.setText(s);
    }

    private String goToNetwork(String urlString) {
        String resultString = "error";
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            StringBuffer sb = new StringBuffer();
            Thread.sleep(3000);
            int c = reader.read();
            while (c != -1) {
                char letter = (char) c;
                sb.append(letter);
                c = reader.read();
            }
            resultString = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return resultString;
    }

}
