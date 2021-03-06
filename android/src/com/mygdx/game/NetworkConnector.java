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
        NetzwerkInterface {

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
    private int mCurrentPlayer = -1;
    private int x = 0;


    private NetworkConnector() {
    }

    public static NetworkConnector getInstance() {
        if (nc == null)
            nc = new NetworkConnector();
        return nc;
    }

    public void initialize(Context ctx) {
        mCtx = ctx;
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
    public void stopNetworkConnection() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void disconnectFromHost() {
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


    public void startDiscovery() {
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
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionRequest(final String remoteEndpointId, String remoteDeviceId, final String remoteEndpointName, byte[] payload) {
        //Hier Callback mit Nachfrage auf akzeptieren und playerName übergeben.
        String playerName = new String(payload);
        Log.e(LOGTAG, playerName);

        if (mIsHost && mRemotePeerEndpoints.size() < 5) {

            //Accept Connnection
            Player p = new Player(mRemotePeerEndpoints.size() + 1, remoteEndpointId, playerName);
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
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        mLibGDXCallBack.addHost(endpointId, deviceId, serviceId, endpointName);
        Log.e(LOGTAG, "Found Endpoint with: " + endpointId + " und " + endpointName);
    }

    @Override
    public void stopAdvertising() {
        mIsHost = false;
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
        String str = new String(bytes);
        final String[] strsp = str.split(";");
        Log.e(LOGTAG, str);
        if (strsp[0].equals("0")) {
            mLibGDXCallBack.setPlayerId(new Integer(strsp[1]));
            mLibGDXCallBack.startNewGameScreen();
        } else if (strsp[0].equals("1")) {
            mLibGDXCallBack.setCountryAttributes(strsp[1], new Integer(strsp[2]), new Integer(strsp[3]), strsp[4]);
        } else if (strsp[0].equals("2")) {
            mLibGDXCallBack.setCountryAttributes(strsp[1], new Integer(strsp[2]), new Integer(strsp[3]), strsp[4]);
        } else if (strsp[0].equals("3")) {
            if (mIsHost) {
                mCurrentPlayer++;
                if (mCurrentPlayer < mRemotePeerEndpoints.size()) {
                    sendMessage(mRemotePeerEndpoints.get(mCurrentPlayer).getEndpointID(),
                            "3;".getBytes());
                } else {
                    mLibGDXCallBack.initializeTurn();
                }
            } else {
                Log.e(LOGTAG, "CONNNECTOR");
                mLibGDXCallBack.initializeTurn();
            }
        } else if (strsp[0].equals("4")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                sendMessage(("4;" + strsp[1]).getBytes());
                mLibGDXCallBack.reinforce(strsp[1]);
            } else {
                mLibGDXCallBack.reinforce(strsp[1]);
            }
        } else if (strsp[0].equals("5")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                sendMessage(("5;" + strsp[1] + ";" + strsp[2] + ";" + strsp[3] + ";" + strsp[4]).getBytes());
                mLibGDXCallBack.attack(strsp[1], strsp[2], strsp[3], strsp[4]);
            } else {
                mLibGDXCallBack.attack(strsp[1], strsp[2], strsp[3], strsp[4]);
            }
        } else if (strsp[0].equals("6")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                sendMessage(("6;" + strsp[1] + ";" + strsp[2] + ";" + strsp[3]).getBytes());
                mLibGDXCallBack.changeOwner(strsp[1], strsp[2], strsp[3]);
            } else {
                mLibGDXCallBack.changeOwner(strsp[1], strsp[2], strsp[3]);
            }
        } else if (strsp[0].equals("7")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                sendMessage(("7;" + strsp[1] + ";" + strsp[2] + ";" + strsp[3]).getBytes());
                mLibGDXCallBack.move(strsp[1], strsp[2], strsp[3]);
            } else {
                mLibGDXCallBack.move(strsp[1], strsp[2], strsp[3]);
            }
        } else if (strsp[0].equals("8")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                sendMessage(("8;".getBytes()));
                mLibGDXCallBack.end();
            } else {
                mLibGDXCallBack.end();
            }
        } else if (strsp[0].equals("9")) {
            Log.e(LOGTAG, "AAAAAAAA");
            (new Thread() {
                public void run() {
                    if (mIsHost) {
                        x = 1;
                        sendMessage(("9;" + strsp[1]).getBytes());
                        x = 0;
                        mLibGDXCallBack.cheat(strsp[1]);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mLibGDXCallBack.isstillcheat(strsp[1])) {
                            x=1;
                            sendMessage(("11;" + strsp[1]).getBytes());
                            x=0;
                            mLibGDXCallBack.cheatsuccess(strsp[1]);
                        }
                    } else {
                        mLibGDXCallBack.cheat(strsp[1]);
                    }
                }
            }).start();

        } else if (strsp[0].equals("10")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                x=1;
                sendMessage(("10;" + strsp[1]).getBytes());
                x=0;
                mLibGDXCallBack.foundcheat(strsp[1]);
            } else {
                mLibGDXCallBack.foundcheat(strsp[1]);
            }
        } else if (strsp[0].equals("11")) {
            Log.e(LOGTAG, "AAAAAAAA");
            if (mIsHost) {
                x=1;
                sendMessage(("11;" + strsp[1]).getBytes());
                x=0;
                mLibGDXCallBack.cheatsuccess(strsp[1]);
            } else {
                mLibGDXCallBack.cheatsuccess(strsp[1]);
            }
        }
    }

    @Override
    public void stopDiscovery() {
        Nearby.Connections.stopDiscovery(mGoogleApiClient, "NO_RISIKO_NO_FUN");
    }

    @Override
    public void onDisconnected(String s) {
        Toast.makeText(mCtx, "Disconnected from" + s, Toast.LENGTH_SHORT).show();
        Player p = this.findPlayerbyEndpointId(s);
        if (p != null)
            mRemotePeerEndpoints.remove(p);
        else
            Toast.makeText(mCtx, "Remove from mRemotePeerEndpoints failed, " + s + " was not in list!", Toast.LENGTH_SHORT).show();
        mLibGDXCallBack.removeClient(s);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void connectTo(final String endpointId, final String endpointName) {
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
                            mRemoteHostEndpoint = endpointId;
                        } else {
                            // Failed connection
                            Log.e(LOGTAG, "Connection failed to endpoint: " + status.toString() + "--" + endpointName);
                        }
                    }
                }, this);
    }

    public void sendMessage(byte[] msg) {
        int cnt = 0;
        if (x == 0) {
            if (mIsHost == true) {
                for (Player endpoint : mRemotePeerEndpoints) {
                    Log.e(LOGTAG, (cnt == mCurrentPlayer) + "");
                    if (cnt == mCurrentPlayer) {
                    } else {
                        Nearby.Connections.sendReliableMessage(mGoogleApiClient, endpoint.getEndpointID(), msg);
                    }
                    cnt++;
                }
            } else
                Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemoteHostEndpoint, msg);
        } else {
            if (mIsHost == true) {
                for (Player endpoint : mRemotePeerEndpoints) {
                    Log.e(LOGTAG, (cnt == mCurrentPlayer) + "");
                    Nearby.Connections.sendReliableMessage(mGoogleApiClient, endpoint.getEndpointID(), msg);
                }
            } else
                Nearby.Connections.sendReliableMessage(mGoogleApiClient, mRemoteHostEndpoint, msg);
        }
    }

    public void sendMessage(String remoteEndpointId, byte[] msg) {
        Nearby.Connections.sendReliableMessage(mGoogleApiClient, remoteEndpointId, msg);
    }

    public ArrayList<Player> getmRemotePeerEndpoints() {
        return mRemotePeerEndpoints;
    }

    public void setmRemotePeerEndpoints(ArrayList<Player> mRemotePeerEndpoints) {
        this.mRemotePeerEndpoints = mRemotePeerEndpoints;
    }

    private Player findPlayerbyEndpointId(String endpointID) {
        for (Player p : mRemotePeerEndpoints) {
            if (p.endpointID.equals(endpointID)) {
                return p;
            }
        }
        return null;
    }

    public boolean ismIsHost() {
        return mIsHost;
    }

    public int getmCurrentPlayer() {
        return mCurrentPlayer;
    }

    public void setmCurrentPlayer(int mCurrentPlayer) {
        this.mCurrentPlayer = mCurrentPlayer;
    }

    public void setX(int x) {
        this.x = x;
    }
}