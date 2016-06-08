package com.mygdx.game;

import com.badlogic.gdx.Screen;

/**
 * Created by peise on 08.06.2016.
 */
public class LibgdxNetzwerkHandler {

    private static LibgdxNetzwerkHandler lnh;
    private MyGdxGame ga;
    private Screen gs;

    private LibgdxNetzwerkHandler(MyGdxGame g)
    {
        ga=g;
    }

    public static LibgdxNetzwerkHandler getInstance(MyGdxGame g)
    {
        if(lnh == null)
            lnh = new LibgdxNetzwerkHandler(g);
        return lnh;
    }

    public void addHost(final String endpointId, String deviceId, String serviceId, final String endpointName){
        gs=ga.getScreen();
        Host h=new Host(endpointId, deviceId, serviceId, endpointName);
        if (gs instanceof HostScreen)
        {
            ((HostScreen) gs).addHost(h);
        }else {

        }
    }

    public void removeHost(final String endpointId){
        gs=ga.getScreen();
        if (gs instanceof HostScreen)
        {
            ((HostScreen) gs).removeHost(endpointId);
        }else {

        }
    }

    public void addClient(){
        gs=ga.getScreen();
        if (gs instanceof JoinScreen)
        {

        }else {

        }
    }

    public void removeClien(){
        gs=ga.getScreen();
        if (gs instanceof JoinScreen)
        {

        }else {

        }
    }

    public void receiveMessage(){
        gs=ga.getScreen();
        if (gs instanceof JoinScreen)
        {

        }else if (gs instanceof HostScreen)
        {

        }else if (gs instanceof GameScreen)
        {

        }else {

        }
    }

    public void sendMessage(){
        gs=ga.getScreen();
        if (gs instanceof JoinScreen)
        {

        }else if (gs instanceof HostScreen)
        {

        }else if (gs instanceof GameScreen)
        {

        }else {

        }}
}
