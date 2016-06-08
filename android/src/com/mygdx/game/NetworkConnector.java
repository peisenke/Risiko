package com.mygdx.game;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

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
    private ArrayList<String> mRemotePeerEndpoints = new ArrayList<String>();
    private LibgdxNetzwerkHandler mLibGDXCallBack = LibgdxNetzwerkHandler.getInstance();


    private NetworkConnector()
    {}

    public static NetworkConnector getInstance()
    {
        if(nc == null)
            nc = new NetworkConnector();
        return nc;
    }

    public void setContext(Context ctx)
    {
        mCtx= ctx;
    }

    public void initialize()
    {
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


    public void stopNetworkConnection()
    {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

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

        appIdentifierList.add(new AppIdentifier("NO_RISIKO_NO_FUN"));
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
        long DISCOVER_TIMEOUT = 1000L;

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
        if (mIsHost) {  //ToDo: Eises Callback um den Host zu fragen ob der Client beitreten darf ins if
            byte[] myPayload = null;

            //Accept Connnection
            mRemotePeerEndpoints.add(remoteEndpointId);
            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, remoteEndpointId,
                    myPayload, this).setResultCallback(new ResultCallback<Status>() {
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
        //ToDo: Hier muss die Liste von Hosts aktualisiert werden!
        mLibGDXCallBack.addHost(endpointId, deviceId, serviceId, endpointName);
    }

    @Override
    public void onEndpointLost(final String endpointId) {
        //ToDo: Hier muss die Liste von Hosts aktualisiert werden!
        mLibGDXCallBack.removeHost(endpointId);
    }


    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {}

    @Override
    public void onDisconnected(String s) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public void connectTo(String endpointId, final String endpointName) {
        // Send a connection request to a remote endpoint. By passing 'null' for
        // the name, the Nearby Connections API will construct a default name
        // based on device model such as 'LGE Nexus 5'.
        String myName = null;
        byte[] myPayload = null;
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
                            Log.e(LOGTAG, "Connection failed to endpoint: " + endpointName);
                        }
                    }
                }, this);
    }

    public void sendMessage(byte[] msg)
    {
        if(mIsHost)
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemotePeerEndpoints , msg);
        else
            Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemoteHostEndpoint, msg);
    }

}