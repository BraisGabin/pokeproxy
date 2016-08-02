package com.braisgabin.pokeproxy.ui;

import android.app.Activity;
import android.content.Context;

import com.braisgabin.pokeproxy.utils.ForActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides
  Activity activityProvider() {
    return activity;
  }

  @Provides
  @ForActivity
  Context contextProvider() {
    return activity;
  }
}
