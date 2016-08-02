package com.braisgabin.pokeproxy;

import com.braisgabin.pokeproxy.ui.ActivityComponent;
import com.braisgabin.pokeproxy.ui.ActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {
        AppModule.class,
    }
)
public interface AppComponent {
  ActivityComponent plus(ActivityModule activityModule);

  void inject(ProxyService service);
}
