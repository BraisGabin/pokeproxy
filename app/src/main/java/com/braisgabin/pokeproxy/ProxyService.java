package com.braisgabin.pokeproxy;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.braisgabin.pokeproxy.utils.TimberHttpFilters;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.Authority;
import org.littleshoot.proxy.mitm.CertificateSniffingMitmManager;
import org.littleshoot.proxy.mitm.RootCertificateException;

import java.io.File;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import timber.log.Timber;

public class ProxyService extends Service {
  public static Intent getCallingIntent(Context context) {
    final Intent intent = new Intent(context, ProxyService.class);

    return intent;
  }

  private HttpProxyServer proxyServer;

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(1, notification());
    proxyServer = DefaultHttpProxyServer.bootstrap()
        .withPort(8080)
        .withManInTheMiddle(mitm(authority(getFilesDir())))
        .withFiltersSource(new HttpFiltersSource() {
          @Override
          public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
            Timber.i(originalRequest.getUri());
            return new TimberHttpFilters(originalRequest.getUri());
          }

          @Override
          public int getMaximumRequestBufferSizeInBytes() {
            return 0;
          }

          @Override
          public int getMaximumResponseBufferSizeInBytes() {
            return 0;
          }
        })
        .start();

    return Service.START_STICKY;
  }

  private MitmManager mitm(Authority authority) {
    try {
      return new CertificateSniffingMitmManager(authority);
    } catch (RootCertificateException e) {
      throw new RuntimeException(e);
    }
  }

  private Authority authority(File baseDir) {
    return new Authority(baseDir,
        "PokeProxy", new char[0],
        "Poke Proxy", "Proxy to read Pokémon Go responses",
        "Certificate Authority", "Poke Proxy", "This certificate is to allow a man in the middle to read Pokémon Go responses.");
  }

  private Notification notification() {
    return new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getString(R.string.app_name))
        .setOngoing(true)
        .build();
  }

  @Override
  public void onDestroy() {
    proxyServer.abort();
  }
}
