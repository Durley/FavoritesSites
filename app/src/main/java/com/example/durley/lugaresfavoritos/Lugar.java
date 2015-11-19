package com.example.durley.lugaresfavoritos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import database.DBHelper;


public class Lugar extends AppCompatActivity {

    //ListView used to show the sites added
    private ListView obj;
    //DB class
    DBHelper mydb;
    //TextView used to show the default/favorite site
    final Bundle dataBundle = new Bundle();
    //Var used to save the record id on the DB
    public int Value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_lugar);

        //Set DB
        mydb = new DBHelper(this);

        //Check there's no records on Sites Table, if true shows activity "CRUD" and end this activity
        if (mydb.checkForTables() == 0) {

            startCRUD(0);

            //if false (there are at least one record on the Sites Table
        } else {
            //List all the sites added in the ListView
            listSites();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.crear:
                startCRUD(0);
                return true;
            //End the app
            case R.id.cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void listSites(){
        //Set the list with the sites added to the Sites Table
        ArrayList array_list = mydb.getAllLugares();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

        obj = (ListView) findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);

        //When some item is selected in the list
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                //Set a var "data" as the displayed string in the position of the item selected in the listview
                String data = (String) arg0.getItemAtPosition(arg2);
                //Set id var to the number of the displayed item in position "data" of the listview before ")",
                // that is the id of the site in the DB
                String id = data.substring(0, data.indexOf(")"));
                //Show a Toast message with the id
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                // Parse that id (string) to int and save it on the id_To_Search var
                int id_To_Search = Integer.parseInt(id);
                //Send the id to the Navegador
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), Navegador.class);

                intent.putExtras(dataBundle);
                startActivity(intent);

            }
        });

        //When some item is long selected in the list
        obj.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                // TODO Auto-generated method stub
                //Set a var "data" as the displayed string in the position of the item selected in the listview
                String data = (String) arg0.getItemAtPosition(pos);
                //Set id var to the number of the displayed item in position "data" of the listview before ")",
                // that is the id of the site in the DB
                String id = data.substring(0, data.indexOf(")"));
                //Show a Toast message with the id
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                // Parse that id (string) to int and save it on the id_To_Search var
                int id_To_Search = Integer.parseInt(id);

                startCRUD(id_To_Search);

                return true;
            }
        });
    }

    public void startCRUD(int id){


        //Send the id to the "Lugar" for edit/delete the site
        dataBundle.putInt("id", id);

        Intent intent = new Intent(getApplicationContext(), CRUD.class);

        intent.putExtras(dataBundle);
        startActivity(intent);

        finish();


    }
}


