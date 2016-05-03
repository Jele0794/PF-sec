package com.edmundo.pfseguridad;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.edmundo.pfseguridad.Adapter.DividerItemDecoration;
import com.edmundo.pfseguridad.Adapter.RVAdapter;
import com.edmundo.pfseguridad.Model.Mensaje;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import org.json.*;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    String theTotalMessage;
    String previousMessage;
    String theMessage;
    TextView muestraMensaje;
    Button botonRefresh;
    Thread hilo;
    JSONObject jsonObject;
    public static String mensaje;
    final String PUBLISH_KEY = "pub-c-4d9e5707-8927-46a7-b8bc-d329e675c24c";
    final String SUBSCRIBE_KEY = "sub-c-a1d78842-10eb-11e6-b422-0619f8945a4f";
    final String CHANNEL = "canal_seguro";
    Pubnub pubnub;
    public Vector<Mensaje> listaDeMensajes;
    private RecyclerView recyclerView;
    public RVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        previousMessage = "";
        theMessage = "";
        theTotalMessage = "";

        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaDeMensajes = new Vector<>();

        recyclerView = (RecyclerView) findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, null));

        adapter = new RVAdapter(listaDeMensajes);
        recyclerView.setAdapter(adapter);

        muestraMensaje = (TextView) findViewById(R.id.entradamensaje);
        botonRefresh = (Button) findViewById(R.id.refresh);


        System.out.println("jalando");


        Callback callback = new Callback() {

            public String theMessage = "";
            EditText texto;
            //

            public String getMessage(){

                return theMessage;
            }
            @Override
            public void connectCallback(String channel, Object message) {
                System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                        + " : " + message.getClass() + " : ");

            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            public void reconnectCallback(String channel, Object message) {
                System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void successCallback(String channel, Object message) {
                System.out.println("SUBSCRIBE : " + channel + " : "
                        + message.getClass() + " : " + message.toString());
                 //theMessage = message.toString();
                jsonObject = (JSONObject) message;
                try {
                    setMessage(jsonObject.getString("mensaje"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("SUBSCRIBE : ERROR on channel " + channel
                        + " : " + error.toString());
            }

        };
        try {
            pubnub.subscribe(CHANNEL, callback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }

        hilo = new Thread(){

            @Override
            public void run() {


                try{

                    while (true)
                    {
                        //System.out.println("narucotoda");

                        if (previousMessage.equals(theMessage) )
                        {
                            //System.out.println("narucotoda");
                        }
                        else
                        {
                            previousMessage = theMessage;
                            theTotalMessage = theTotalMessage + "\n" + theMessage;
                        }
                    }


                }catch (Exception e)
                {
                    System.out.println(e);
                }

            }

        };

        hilo.start();

        muestraMensaje.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    Callback callback = new Callback() {};
                    JSONObject mensajeNuevo = new JSONObject();

                    try {
                        mensajeNuevo.put("mensaje", muestraMensaje.getText().toString());
                    } catch (JSONException e) {
                        System.out.println("Problema...");
                    }

                    pubnub.publish(CHANNEL, mensajeNuevo, callback);
                    Mensaje mensajeNew = new Mensaje("Usuario 1", muestraMensaje.getText().toString(), "Aquí va el resultado del mensaje encriptado");
                    addToList(mensajeNew);
                    Snackbar.make(v,"Mensaje enviado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    muestraMensaje.setText(null);

                    handled = true;
                }
                return handled;
            }
        });


    }

    public void refresh(View view)
    {
        muestraMensaje.setText(theTotalMessage);
    }

    public void setMessage(String string){
        theMessage = string;
    }


    private void addToList(Mensaje mensajeItem) {

        adapter.vectorDeMensajes.add(0, mensajeItem);
        adapter.notifyItemInserted(0);
        Toast mensaje = Toast.makeText(this, "Nuevo mensaje", Toast.LENGTH_SHORT);
        mensaje.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.desconectar) {
            pubnub.unsubscribe(CHANNEL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
