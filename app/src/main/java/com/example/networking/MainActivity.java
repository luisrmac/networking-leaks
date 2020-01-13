package com.example.networking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_TAG";
    private static final String RESULT_EXTRA = "com.example.networking.RESULT_EXTRA";
    private TextView result;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String value = msg.getData().getString(RESULT_EXTRA);
            result.setText(value);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        // offloadWork("https://www.google.com");

        new MyTaskI(result).execute("https://www.google.com");
    }

    private void offloadWork(final String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                goToNetwork(url);
            }
        });
        thread.start();
    }

    private String goToNetwork(String urlString, boolean task) {
        String resultString = "error";
        if(!task) {
            goToNetwork(urlString);
        } else {
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStreamReader reader = new InputStreamReader(con.getInputStream());
                StringBuffer sb = new StringBuffer();
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
            }
        }
        return resultString;
    }

    private void goToNetwork(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            StringBuffer sb = new StringBuffer();
            int c = reader.read();
            while (c != -1) {
                char letter = (char) c;
                sb.append(letter);
                c = reader.read();
            }
            String resultString = sb.toString();
            myHandler.sendMessage(prepareResultMessage(resultString));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    result.setText(resultString);
//                }
//            });
//            result.post(new Runnable() {
//                @Override
//                public void run() {
//                    result.setText(resultString);
//                }
//            });
            // result.setText(resultString); this crashes with CalledFromWrongThreadException
            Log.d(TAG, "goToNetwork: " + resultString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private Message prepareResultMessage(String resultString) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_EXTRA, resultString);
        msg.setData(bundle);
        return msg;
    }

    static class MyTaskI extends AsyncTask<String,Void,String> {
        private WeakReference<TextView> resultMT;

        MyTaskI(TextView resultMT) {
            this.resultMT = new WeakReference<>(resultMT);
        }

        @Override
        protected String doInBackground(String... strings) {
            return goToNetwork(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultMT.get().setText(s);
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

}
