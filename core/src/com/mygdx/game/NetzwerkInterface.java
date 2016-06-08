package com.mygdx.game;

/**
 * Created by Fabse on 08.06.2016.
 */
public interface NetzwerkInterface
{
    public void initialize();
    public boolean isConnectedToNetwork();
    public void stopNetworkConnection();
    public void disconnectFromHost();
    public void startAdvertising();
    public void startDiscovery();
    public void connectTo(String endpointId, final String endpointName);
    public void sendMessage(byte[] msg);
}