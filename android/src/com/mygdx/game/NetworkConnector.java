package com.mygdx.game;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabse on 05.06.2016.
 */
public class NetworkConnector implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener,
        NetzwerkInterface
{

    // Identify if the device is the host
    private boolean mIsHost = false;
    private static NetworkConnector nc;
    private Context mCtx;
    private GoogleApiClient mGoogleApiClient;
    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_ETHERNET};
    private static String LOGTAG = "NETWORK_CONNECTOR";
    private String mRemoteHostEndpoint;
    private ArrayList<Player> mRemotePeerEndpoints = new ArrayList<Player>();
    private LibgdxNetzwerkHandler mLibGDXCallBack = LibgdxNetzwerkHandler.getInstance();


    private NetworkConnector()
    {
    }

    public static NetworkConnector getInstance()
    {
        if(nc == null)
            nc = new NetworkConnector();
        return nc;
    }

    public void initialize(Context ctx)
    {
        mCtx= ctx;
        mGoogleApiClient = new GoogleApiClient.Builder(mCtx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();

        mGoogleApiClient.connect();
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager connManager =
                (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

@Override
    public void stopNetworkConnection()
    {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void disconnectFromHost()
    {
        Nearby.Connections.stopAllEndpoints(mGoogleApiClient);
    }


    //ToDo: Auslagern des AppIdentifiers in die String.xml
    public void startAdvertising() {

        // Identify that this device is the host
        mIsHost = true;

        // Advertising with an AppIdentifer lets other devices on the
        // network discover this application and prompt the user to
        // install the application.
        List<AppIdentifier> appIdentifierList = new ArrayList<>();

        String serviceId = mCtx.getString(R.string.service_id);
        appIdentifierList.add(new AppIdentifier(serviceId));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        // The advertising timeout is set to run indefinitely
        // Positive values represent timeout in milliseconds
        long NO_TIMEOUT = 0L;

        String name = null;
        Nearby.Connections.startAdvertising(mGoogleApiClient, name, appMetadata, NO_TIMEOUT,
                this).setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
            @Override
            public void onResult(Connections.StartAdvertisingResult result) {
                if (result.getStatus().isSuccess()) {
                    // Device is advertising
                    Log.e(LOGTAG, "Device is advertising!");
                } else {
                    int statusCode = result.getStatus().getStatusCode();
                    // Advertising failed - see statusCode for more details

                    if (statusCode == 8000)
                        Toast.makeText(mCtx, "Sie sind mit keinem Netzwerk verbunden!", Toast.LENGTH_SHORT).show();

                    Log.e(LOGTAG, "Device advertising failed with code: " + statusCode);
                }
            }
        });
    }


    public void startDiscovery()
    {
        mIsHost = false;
        String serviceId = "NO_RISIKO_NO_FUN";

        // Set an appropriate timeout length in milliseconds
        long DISCOVER_TIMEOUT = 0L;

        // Discover nearby apps that are advertising with the required service ID.
        Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, DISCOVER_TIMEOUT, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            // Device is discovering
                            Log.e(LOGTAG, "Device is discovering!");
                        } else {
                            int statusCode = status.getStatusCode();
                            // Discovering failed - see statusCode for more details
                            Log.e(LOGTAG, "Device discovering failed with code: " + statusCode);
                        }
                    }
                });
    }


    @Override
    public void onConnected(Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionRequest(final String remoteEndpointId, String remoteDeviceId,final String remoteEndpointName, byte[] payload)
    {
        //Hier Callback mit Nachfrage auf akzeptieren und playerName übergeben.
        String playerName = new String(payload);
        Log.e(LOGTAG, playerName);

        if (mIsHost && mRemotePeerEndpoints.size() < 5) {

            //Accept Connnection
            Player p = new Player(mRemotePeerEndpoints.size()+2,remoteEndpointId,playerName);
            mRemotePeerEndpoints.add(p);
            mLibGDXCallBack.addClient(remoteEndpointId);

            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, remoteEndpointId,
                    null, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Toast.makeText(mCtx, "Connected to " + remoteEndpointName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mCtx, "Failed to connect to: " + remoteEndpointName, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Clients should not be advertising and will reject all connection requests.
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
        }

    }



    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName)
    {
        mLibGDXCallBack.addHost(endpointId, deviceId, serviceId, endpointName);
        Log.e(LOGTAG, "Found Endpoint with: " + endpointId + " und " + endpointName);
    }

    @Override
    public void stopAdvertising(){
        mIsHost=false;
        Log.e(LOGTAG, "Device stoped advertising#");
        Nearby.Connections.stopAdvertising(mGoogleApiClient);
    }

    @Override
    public void onEndpointLost(String endpointId) {
        Log.e(LOGTAG, "Lost Endpoint with: " + endpointId);
        mLibGDXCallBack.removeHost(endpointId);
    }


    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {
        String str=new String(bytes);
        String[] strsp=str.split(";");
        Log.e(LOGTAG, str);
        if(strsp[0].equals("0")){
            mLibGDXCallBack.setPlayerId(new Integer(strsp[1]));
            mLibGDXCallBack.startNewGameScreen();
        }else if (strsp[0].equals("1")){
            try {
                Thread.sleep(5000);
            }catch(Exception e){};
            mLibGDXCallBack.setCountryAttributes(strsp[1],new Integer(strsp[2]),new Integer(strsp[3]),strsp[4]);
        }
    }

    @Override
    public void stopDiscovery()
    {
        Nearby.Connections.stopDiscovery(mGoogleApiClient, "NO_RISIKO_NO_FUN");
    }
    @Override
    public void onDisconnected(String s) {
        Toast.makeText(mCtx, "Disconnected from" + s, Toast.LENGTH_SHORT).show();
        Player p = this.findPlayerbyEndpointId(s);
        if(p != null)
            mRemotePeerEndpoints.remove(p);
        else
            Toast.makeText(mCtx, "Remove from mRemotePeerEndpoints failed, " +s+ " was not in list!", Toast.LENGTH_SHORT).show();
        mLibGDXCallBack.removeClient(s);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public void connectTo(String endpointId, final String endpointName) {
        // Send a connection request to a remote endpoint. By passing 'null' for
        // the name, the Nearby Connections API will construct a default name
        // based on device model such as 'LGE Nexus 5'.
        Log.e(LOGTAG, "Want to connect to: " + endpointId);
        String myName = endpointName;
        byte[] myPayload = mLibGDXCallBack.getGa().getP().getName().getBytes();
        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, myName,
                endpointId, myPayload, new Connections.ConnectionResponseCallback() {
                    @Override
                    public void onConnectionResponse(String remoteEndpointId, Status status,
                                                     byte[] bytes) {
                        if (status.isSuccess()) {
                            // Successful connection
                            Log.e(LOGTAG, "Connected to: " + endpointName);
                        } else {
                            // Failed connection
                            Log.e(LOGTAG, "Connection failed to endpoint: "+status.toString() + "--" + endpointName);
                        }
                    }
                }, this);
    }

    public void sendMessage(byte[] msg)
    {
        if(mIsHost==true)
        {
            for (Player endpoint: mRemotePeerEndpoints)
                Nearby.Connections.sendReliableMessage(mGoogleApiClient, endpoint.getEndpointID(), msg);

        }
        else
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemoteHostEndpoint, msg);
    }

    public void sendMessage(String remoteEndpointId, byte[] msg)
    {
        Nearby.Connections.sendReliableMessage(mGoogleApiClient, remoteEndpointId , msg);
    }

    public ArrayList<Player> getmRemotePeerEndpoints() {
        return mRemotePeerEndpoints;
    }
    public void setmRemotePeerEndpoints(ArrayList<Player> mRemotePeerEndpoints) {
        this.mRemotePeerEndpoints = mRemotePeerEndpoints;
    }

    private Player findPlayerbyEndpointId(String endpointID)
    {
        for (Player p:mRemotePeerEndpoints)
        {
            if(p.endpointID.equals(endpointID))
            {
                return p;
            }
        }
        return null;
    }

    public boolean ismIsHost() {
        return mIsHost;
    }
}