package com.example.durley.lugaresfavoritos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.DBHelper;

public class CRUD extends AppCompatActivity {


    //DB class
    private DBHelper mydb;
    //TextViews
    TextView nombre;
    TextView url;
    //Var which save the id that is going to be edited or updated
    int id_To_Update = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_crud);

        //Relate the vars with the TextViews in the XML
        nombre = (TextView) findViewById(R.id.txtNombre);
        url = (TextView) findViewById(R.id.txtUrl);

        //Relate the vars with the buttons in the XML
        Button b = (Button) findViewById(R.id.btnGuardar);
        Button b1 = (Button) findViewById(R.id.btnEditar);
        Button b2 = (Button) findViewById(R.id.btnEliminar);

        //Set DB
        mydb = new DBHelper(this);

        //Set the data sent from other activity to this one
        Bundle extras = getIntent().getExtras();
        //Check if there's a data passed from other activity to this one
        if (extras != null) {
            //If there is
            //Set the var Value with the data sent with the tag "id"
            int Value = extras.getInt("id");

            //If that value is > 0, then the site is already added and it's going to be edited or deleted
            if (Value > 0) {
                //First, set the Cursor rs to the return of the "getData" method in the DBHelper,
                //Sending the "Value", (id), of the site
                Cursor rs = mydb.getData(Value);
                //Save the var id_To_Update with the id of the site
                id_To_Update = Value;
                //Move to first record found
                rs.moveToFirst();
                //Save the name, url and ip of that specific site
                String nomb = rs.getString(rs.getColumnIndex(DBHelper.LUGARES_COLUMN_NOMBRE));
                String ur = rs.getString(rs.getColumnIndex(DBHelper.LUGARES_COLUMN_URL));
                //If the Cursor is not closed, close it
                if (!rs.isClosed()) {
                    rs.close();
                }

                //Set the TextViews with the respective values
                nombre.setText((CharSequence) nomb);
                url.setText((CharSequence) ur);

                //Then, make visible the button edit and deleted, and make invisible the button save

                b.setVisibility(View.INVISIBLE);
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);

                //Set the TextViews enabled, focusable and clickable
                nombre.setEnabled(true);
                nombre.setFocusableInTouchMode(true);
                nombre.setClickable(true);

                url.setEnabled(true);
                url.setFocusableInTouchMode(true);
                url.setClickable(true);

            }else{
                //If the id is equals to 0, it means that will be created a new site, then
                //make invisible the button edit and deleted, and make visible the button save
                b.setVisibility(View.VISIBLE);
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crud, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            //Close this activity and start the "Lugar" activity
            case R.id.cancelar:
                Toast toast = Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_SHORT);
                iniciarLugar();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Method for when save button is clicked
    public void run(View view) {


        //If the data are inserted correctly
        if (mydb.insertLugar(nombre.getText().toString(), url.getText().toString())) {
            //Shows a message saying "done"
            Toast.makeText(getApplicationContext(), "Hecho", Toast.LENGTH_SHORT).show();
        } else {//If the data are not inserted correctly, shows a message saying "not done"
            Toast.makeText(getApplicationContext(), "No Guardado", Toast.LENGTH_SHORT).show();
        }
        //Start the activity "Lugar" and close this one
        iniciarLugar();

    }

    //Method for when edit button is clicked
    public void editar(View view) {
        if (mydb.updateLugar(id_To_Update, nombre.getText().toString(), url.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Actualizado", Toast.LENGTH_SHORT).show();
        }
        //Start the activity "Lugar" and close this one
        iniciarLugar();
    }

    //Method for when delete button is clicked
    public void eliminar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.borrarLugar)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //If the site that is going to be deleted is default/favorite
                        mydb.deleteLugar(id_To_Update);
                        Toast.makeText(getApplicationContext(), "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
                        //Start the activity "Lugar" and close this one
                        iniciarLugar();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //User cancelled the dialog
                    }
                });
        AlertDialog d = builder.create();
        d.setTitle("Seguro");
        d.show();
    }

    //Start the activity "Lugar" and close this one
    private void iniciarLugar(){
        Intent intent = new Intent(getApplicationContext(), Lugar.class);
        startActivity(intent);
        finish();
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            iniciarLugar();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
