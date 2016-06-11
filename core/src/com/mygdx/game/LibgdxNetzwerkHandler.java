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

        }
    }


    public void setPlayerId(int Id)
    {
        ga.getP().setId(Id);
    }

    public MyGdxGame getGa() {
        return ga;
    }


    public void startNewGameScreen()
    {
        Gdx.app.log("New Game", "in startNewGameScreen");

        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                ga.setScreen(new GameScreen(ga));
                //ga.getScreen().show();
            }
        });
    }

    public void setCountryAttributes(String countryName, int troops, int ownerID, String ownerName)
    {
        while(! (ga.getScreen() instanceof GameScreen)) {
            Gdx.app.log("Instance of screen", ga.getScreen().getClass().toString());
        }
        //if(ga.getScreen() instanceof GameScreen) {
            GameScreen gs = (GameScreen) ga.getScreen();

            // while(!gs.isInitialized()){}

            Gdx.app.log("Nach while", "Nach while.");
            for (Country c : gs.getGl().getGs().getWorld().getCountries().values()) {
                if (c.getName().equals(countryName)) {
                    c.setTroops(troops);
                    Player p = new Player(ownerID, null, ownerName);
                    c.setOwner(p);
                    c.setColor(p.getC());
                }
            }
       // }
    }
}