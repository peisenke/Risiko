package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import sun.rmi.runtime.Log;

/**
 * Created by peise on 08.06.2016.
 */
public class LibgdxNetzwerkHandler {

    private static LibgdxNetzwerkHandler lnh;
    private MyGdxGame ga;
    private Screen gs;

    private LibgdxNetzwerkHandler()
    {

    }

    public static LibgdxNetzwerkHandler getInstance()
    {
        if(lnh == null)
            lnh = new LibgdxNetzwerkHandler();
        return lnh;
    }

    public void setGa(MyGdxGame ga) {
        this.ga = ga;
    }

    public void addHost(final String endpointId, String deviceId, String serviceId, final String endpointName){
        gs=ga.getScreen();
        Host h=new Host(endpointId, deviceId, serviceId, endpointName);
        Gdx.app.log("asdf2","asdf2");
        if (gs instanceof JoinScreen)
        {
            Gdx.app.log("asdf","asdf");
            ((JoinScreen) gs).addHost(h);
        }else {

        }
    }

    public void removeHost(final String endpointId){
        gs=ga.getScreen();
        if (gs instanceof JoinScreen)
        {
            ((JoinScreen) gs).removeHost(endpointId);
        }else {

        }
    }

    public void connectTo(String endpointId){
        gs=ga.getScreen();
        if (gs instanceof JoinScreen)
        {
            ((JoinScreen)gs).connectTo(endpointId);
        }else {

        }
    }

    public void addClient(String remoteEndpointId){
        gs=ga.getScreen();
        if (gs instanceof HostScreen)
        {
            ((HostScreen)gs).addClient(remoteEndpointId);
        }else {

        }
    }

    public void removeClient(String remoteEndpointId){
        gs=ga.getScreen();
        if (gs instanceof HostScreen)
        {
            ((HostScreen)gs).removeClient(remoteEndpointId);
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

    public void recievedWorld(RisikoWorld w) {
        if (gs instanceof GameScreen)
        {
            ((GameScreen)gs).getGl().getGs().setWorld(w);
        }else if (gs instanceof JoinScreen)
        {
            ga.setScreen(new GameScreen(w));
        }else {

        }
    }
    public void setPlayerId(int Id)
    {
        ga.getP().setId(Id);
    }


}