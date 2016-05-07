package com.mygdx.game;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class ActionResolverAndroid implements ActionResolver {

    Handler handler;
    Context context;

    public ActionResolverAndroid(Context context) {
        handler = new Handler();
        this.context = context;
    }

    @Override
    public void showPreferences() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(context, OptionsScreen.class);
                context.startActivity(i);
            }
        });
    }
}
