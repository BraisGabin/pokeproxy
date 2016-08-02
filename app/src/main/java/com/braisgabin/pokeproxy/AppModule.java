package com.braisgabin.pokeproxy;

import android.content.Context;

import com.braisgabin.pokeproxy.utils.ForApplication;

import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.mitm.Authority;
import org.littleshoot.proxy.mitm.CertificateSniffingMitmManager;
import org.littleshoot.proxy.mitm.RootCertificateException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides
  @ForApplication
  Context contextProvider() {
    return app;
  }

  @Provides
  @Singleton
  Authority authorityProvider() {
    return new Authority(app.getFilesDir(),
        "PokeProxy", new char[0],
        "Poke Proxy", "Proxy to read Pokémon Go responses",
        "Certificate Authority", "Poke Proxy", "This certificate is to allow a man in the middle to read Pokémon Go responses.");
  }

  @Provides
  @Singleton
  MitmManager mitmManagerProvider(Authority authority) {
    try {
      return new CertificateSniffingMitmManager(authority);
    } catch (RootCertificateException e) {
      throw new RuntimeException(e);
    }
  }
}
