package com.braisgabin.pokeproxy.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.KeyChain;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.braisgabin.pokeproxy.App;
import com.braisgabin.pokeproxy.R;

import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.mitm.Authority;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;

import static com.braisgabin.pokeproxy.utils.Utils.file2byteArray;

public class SetupActivity extends AppCompatActivity {
  private static final int REQUEST_CODE_STEP_1 = 1;

  public static Intent getCallingIntent(Context context) {
    final Intent intent = new Intent(context, SetupActivity.class);

    return intent;
  }

  @Inject
  Authority authority;

  @Inject
  Lazy<MitmManager> mitm;

  @BindView(R.id.step1)
  Button step1Button;

  @BindView(R.id.step2)
  Button step2Button;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);

    App.component(this)
        .plus(new ActivityModule(this))
        .inject(this);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.step1)
  void onStep1() {
    final File p12 = authority.aliasFile(".p12");
    final Intent installIntent = KeyChain.createInstallIntent();
    installIntent.putExtra(KeyChain.EXTRA_PKCS12, file2byteArray(p12));
    installIntent.putExtra(KeyChain.EXTRA_NAME, authority.alias());
    startActivityForResult(installIntent, REQUEST_CODE_STEP_1);
  }

  @OnClick(R.id.step2)
  void onStep2() {
    startActivity(MainActivity.getCallingIntent(this));
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_CODE_STEP_1:
        if (resultCode == RESULT_OK) {
          step1Button.setEnabled(false);
          step2Button.setEnabled(true);
        }
        break;
      default:
        super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
