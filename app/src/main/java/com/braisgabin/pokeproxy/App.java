package com.braisgabin.pokeproxy;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class App extends Application {

  private AppComponent component;

  public static AppComponent component(Context context) {
    return ((App) context.getApplicationContext()).getComponent();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    Timber.plant(new Timber.DebugTree());
  }

  public synchronized AppComponent getComponent() {
    if (component == null) {
      component = DaggerAppComponent.builder()
          .appModule(new AppModule(this))
          .build();
    }
    return component;
  }
}
