package com.example.durley.lugaresfavoritos;

/*
 * Demo of creating an application to open any URL inside the application and clicking on any link from that URl
should not open Native browser but  that URL should open in the same screen.

- Load WebView with progress bar

Modified by Durley 14/10/2015
 */


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import database.DBHelper;

public class Navegador extends AppCompatActivity {


    WebView web;
    ProgressBar progressBar;
    private DBHelper mydb;
    private String ip,url,user,contra, serial;
    public int Value;
    private String Text1 = "", Text2 = "";
    final Bundle dataBundle = new Bundle();
    int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/


        setContentView(R.layout.activity_navegador);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Value = extras.getInt("id");

            if (Value > 0) {
                //Get the values saved on the DB, according with the id received from other activity
                Cursor rs = mydb.getData(Value);
                rs.moveToFirst();
                url = rs.getString(rs.getColumnIndex(DBHelper.LUGARES_COLUMN_URL));
                if (!rs.isClosed()) {
                    rs.close();
                }
            }
        }

        //WebView related to the xml
        web = (WebView) findViewById(R.id.webview01);

        //ProgressBar related to the xml
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setMax(100);

        web.setWebViewClient(new myWebClient());
        web.setWebChromeClient(new MyWebViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);

        web.loadUrl("http://" + url);

        //Support Zoom
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);

        progressBar.setProgress(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navegador, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.lugares:
                showToast("Lugares");
                finish();
				Intent intent = new Intent(getApplicationContext(), Lugar.class);
				startActivity(intent);
                return true;

            case R.id.refrescar:
                showToast("Refrescar Página");
                web.loadUrl("javascript:window.location.reload( true )");
                progressBar.setVisibility(View.VISIBLE);
                Navegador.this.progressBar.setProgress(0);
                return true;

            case R.id.regresar:
                if ( web.canGoBack()) {
                    showToast("Regresar");
                    progressBar.setVisibility(View.VISIBLE);
                    Navegador.this.progressBar.setProgress(0);
                    web.goBack();
                    return true;
                }

            case R.id.salir:
                showToast("Salir");
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Show the message, according the selected item of the menu
    private void showToast(String s) {
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        toast.show();
    }
    //Progress of the WebView for the ProgressBar
    public void setValue(int progress) {
        this.progressBar.setProgress(progress);
    }


    //WebView Client
    public class myWebClient extends WebViewClient
    {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            Navegador.this.progressBar.setProgress(0);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }

    }

    //WebViewChrome let you know when progress change to set the value for the progress bar
    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Navegador.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }


    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            progressBar.setVisibility(View.VISIBLE);
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

