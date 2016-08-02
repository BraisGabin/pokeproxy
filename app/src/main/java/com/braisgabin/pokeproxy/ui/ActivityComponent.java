package com.braisgabin.pokeproxy.ui;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Singleton
@Subcomponent(
    modules = ActivityModule.class
)
public interface ActivityComponent {
  void inject(MainActivity activity);

  void inject(SetupActivity activity);
}
