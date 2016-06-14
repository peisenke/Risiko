package com.mygdx.game;

import java.util.ArrayList;

/**
 * Created by Fabse on 08.06.2016.
 */
public interface NetzwerkInterface
{
    public boolean isConnectedToNetwork();
    public void stopNetworkConnection();
    public void disconnectFromHost();
    public void startAdvertising();
    public void stopAdvertising();
    public void startDiscovery();
    public void stopDiscovery();
    public void connectTo(String endpointId, final String endpointName);
    public void sendMessage(byte[] msg);
    public ArrayList<Player> getmRemotePeerEndpoints();
    public void sendMessage(String remoteEndpointId, byte[] msg);
    public boolean ismIsHost();
    public int getmCurrentPlayer();
    public void setmCurrentPlayer(int mCurrentPlayer);
    public void onMessageReceived(String s, byte[] bytes, boolean b);
    public void setX(int x);
    }
