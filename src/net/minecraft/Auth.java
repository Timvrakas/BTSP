package net.minecraft;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import javax.net.ssl.HttpsURLConnection;

public class Auth {
		  public static String executePost(String paramString1, String paramString2)
		  {
		    HttpsURLConnection localHttpsURLConnection = null;
		    try
		    {
		      URL localURL = new URL(paramString1);
		      localHttpsURLConnection = (HttpsURLConnection)localURL.openConnection();
		      localHttpsURLConnection.setRequestMethod("POST");
		      localHttpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		      localHttpsURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(paramString2.getBytes().length));
		      localHttpsURLConnection.setRequestProperty("Content-Language", "en-US");

		      localHttpsURLConnection.setUseCaches(false);
		      localHttpsURLConnection.setDoInput(true);
		      localHttpsURLConnection.setDoOutput(true);

		      localHttpsURLConnection.connect();
		      Certificate[] arrayOfCertificate = localHttpsURLConnection.getServerCertificates();

		      byte arrayOfByte1[] = new byte[294];
		      DataInputStream localDataInputStream = new DataInputStream(Auth.class.getResourceAsStream("minecraft.key"));
		      localDataInputStream.readFully(arrayOfByte1);
		      localDataInputStream.close();

		      Certificate localCertificate = arrayOfCertificate[0];
		      PublicKey localPublicKey = localCertificate.getPublicKey();
		      byte[] arrayOfByte2 = localPublicKey.getEncoded();

		      for (int i = 0; i < arrayOfByte2.length; i++) {
		        if (arrayOfByte2[i] != arrayOfByte1[i]) throw new RuntimeException("Public key mismatch");

		      }

		      DataOutputStream localDataOutputStream = new DataOutputStream(localHttpsURLConnection.getOutputStream());
		      localDataOutputStream.writeBytes(paramString2);
		      localDataOutputStream.flush();
		      localDataOutputStream.close();

		      InputStream localInputStream = localHttpsURLConnection.getInputStream();
		      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));

		      StringBuffer localStringBuffer = new StringBuffer();
		      String str1;
		      while ((str1 = localBufferedReader.readLine()) != null) {
		        localStringBuffer.append(str1);
		        localStringBuffer.append('\r');
		      }
		      localBufferedReader.close();

		      return localStringBuffer.toString();
		    }
		    catch (Exception localException)
		    {
		      @SuppressWarnings("unused")
			byte[] arrayOfByte1;
		      localException.printStackTrace();
		      return null;
		    }
		    finally
		    {
		      if (localHttpsURLConnection != null)
		        localHttpsURLConnection.disconnect();
		    }
		  }
}
