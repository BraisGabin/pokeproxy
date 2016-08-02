package com.braisgabin.pokeproxy.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.braisgabin.pokeproxy.App;
import com.braisgabin.pokeproxy.utils.CheckFactory;
import com.braisgabin.pokeproxy.ProxyService;
import com.braisgabin.pokeproxy.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
  public static Intent getCallingIntent(Context context) {
    final Intent intent = new Intent(context, MainActivity.class);

    return intent;
  }

  private OkHttpClient httpClient;

  @Inject
  CheckFactory checkFactory;

  private CheckFactory.CheckImage check;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    App.component(this)
        .plus(new ActivityModule(this))
        .inject(this);
    ButterKnife.bind(this);

    // Don't try this at home.
    new AsyncTask<Void, Void, Void>() {

      @Override
      protected Void doInBackground(Void... voids) {
        httpClient = new OkHttpClient.Builder()
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080)))
            .connectTimeout(1, TimeUnit.DAYS)
            .writeTimeout(1, TimeUnit.DAYS)
            .readTimeout(1, TimeUnit.DAYS)
            .build();
        return null;
      }
    }.execute();
  }

  @Override
  protected void onStart() {
    super.onStart();
    this.check = checkFactory.create();
    check.execute();
  }

  @Override
  protected void onStop() {
    super.onStop();
    check.cancel(true);
  }

  @OnClick(R.id.button)
  void onClick() {
    final Intent intent = ProxyService.getCallingIntent(this);
    if (isMyServiceRunning(ProxyService.class)) {
      stopService(intent);
    } else {
      startService(intent);
    }
  }

  @OnClick(R.id.button2)
  void onClick2() {
    send(new Request.Builder()
        .url("http://www.pokemon.com")
        .build());
  }

  @OnClick(R.id.button3)
  void onClick3() {
    send(new Request.Builder()
        .url("https://en.wikipedia.org/wiki/Man-in-the-middle_attack")
        .build());

  }

  private void send(final Request request) {
    httpClient.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Timber.e(e, request.url().toString());
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Timber.d("SUCCESS: %s", request.url().toString());
      }
    });
  }

  // Extracted from: http://stackoverflow.com/a/5921190/842697
  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }
}
