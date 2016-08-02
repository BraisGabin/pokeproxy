package com.braisgabin.pokeproxy.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.mitm.Authority;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

import javax.inject.Inject;

import dagger.Lazy;

public class CheckFactory {
  private final Context context;
  private final Authority authority;
  private final Lazy<MitmManager> mitm;

  @Inject
  public CheckFactory(@ForActivity Context context, Authority authority, Lazy<MitmManager> mitm) {
    this.context = context;
    this.authority = authority;
    this.mitm = mitm;
  }

  public CheckImage create() {
    return new CheckImage();
  }

  public class CheckImage extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... voids) {
      try {
        // Generate the .p12 file
        mitm.get();

        X509Certificate certificate = p12ToX509Certificate(authority.aliasFile(".p12"), new char[0]);
        final byte[] signature = certificate.getSignature();

        final KeyStore caStore = KeyStore.getInstance("AndroidCAStore");
        caStore.load(null, null);
        final Enumeration<String> aliases = caStore.aliases();
        while (aliases.hasMoreElements()) {
          final String alias = aliases.nextElement();
          final X509Certificate cert = (X509Certificate) caStore.getCertificate(alias);

          if (Arrays.equals(cert.getSignature(), signature)) {
            return false;
          }
        }
        return true;
      } catch (CertificateException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (KeyStoreException e) {
        throw new RuntimeException(e);
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected void onPostExecute(Boolean needInstallCA) {
      if (needInstallCA) {
        Toast.makeText(context, "Need setup", Toast.LENGTH_SHORT).show();
      }
    }

    public X509Certificate p12ToX509Certificate(File p12Path, char[] pass) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
      final KeyStore p12 = KeyStore.getInstance("PKCS12");
      p12.load(new FileInputStream(p12Path), pass);
      final Enumeration<String> aliases1 = p12.aliases();
      return (X509Certificate) p12.getCertificate(aliases1.nextElement());
    }
  }
}
