package com.panda.videolivecore.net.http;

import android.os.AsyncTask;

import com.panda.videolivecore.CoreApplication;
import com.panda.videolivecore.utils.CookieContiner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;

public class HttpRequest {
    public static String CHARSET_NAME_BYTES = "ISO-8859-1";
    public static String CHARSET_NAME_UTF8 = "UTF-8";
    private static String HTTP_PRIVATE_HEADER_KEY = "xiaozhangdepandatv";
    private static String HTTP_PRIVATE_HEADER_VALUE = "1";
    private static int HTTP_TIMEOUT_US = 10000;
    private static int MAX_HEADER_CONTENT_SIZE = 8388608;
    private static String REQUESTTYPE_GET = "";
    private static String REQUESTTYPE_GETBIN = "getbin";
    private static String REQUESTTYPE_POSTFILE = "postfile";
    private IHttpRequestEvent m_event = null;

    public HttpRequest(IHttpRequestEvent paramIHttpRequestEvent) {
        this.m_event = paramIHttpRequestEvent;
    }

    public static String Bytes2String(byte[] paramArrayOfByte) {
        try {
            if (paramArrayOfByte.length <= 4194304) {
                String str = new String(paramArrayOfByte, CHARSET_NAME_BYTES);
                return str;
            }
        } catch (Exception localException) {

        }
        return "";
    }

    public static byte[] ReadFileData(String paramString) {
        try {
            File localFile = new File(paramString);
            byte[] arrayOfByte = null;

            if (localFile != null) {
                boolean bool = localFile.exists();
                if (bool) {
                    FileInputStream localFileInputStream = new FileInputStream(localFile);
                    arrayOfByte = new byte[localFileInputStream.available()];
                    localFileInputStream.read(arrayOfByte);
                    localFileInputStream.close();
                }
            }
            return arrayOfByte;
        } catch (Exception localException) {

        }
    }


    public static byte[] String2Bytes(String paramString) {
        try {
            byte[] arrayOfByte2 = paramString.getBytes(CHARSET_NAME_BYTES);
            return arrayOfByte2;
        } catch (Exception localException) {
            while (true) {
                localException.printStackTrace();
                byte[] arrayOfByte1 = null;
            }
        }
    }

    public static String URLAppendVersionPlat(String paramString, boolean paramBoolean) {
        return paramString;
//    String str2;
//    String str3;
//    if (!paramString.isEmpty())
//    {
//      if (paramBoolean)
//      {
//        str2 = "?";
//        str3 = "" + paramString + str2 + "__version=" + CoreApplication.getInstance().version() + "&__plat=android";
//        String str4 = CookieContiner.getToken();
//        if (!str4.isEmpty())
//          str3 = str3 + "&" + str4;
//      }
//      for (String str1 = str3; ; str1 = "")
//      {
//        return str1;
//        str2 = "&";
//        break;
//      }
//    }

    }

    public static String URLAppendVersionPlatNoToken(String paramString, boolean paramBoolean) {
        return paramString;
//    String str2;
//    if (!paramString.isEmpty())
//      if (paramBoolean)
//        str2 = "?";
//    for (String str1 = "" + paramString + str2 + "__version=" + CoreApplication.getInstance().version() + "&__plat=android"; ; str1 = "")
//    {
//      return str1;
//      str2 = "&";
//      break;
//    }
    }

    public static String URLEncoder(String paramString) {
        try {
            return URLEncoder.encode(paramString, CHARSET_NAME_UTF8);


        } catch (Exception localException) {

//        localException.printStackTrace();
        }
        return "";
    }

    public static boolean getResponse(HttpURLConnection paramHttpURLConnection, ByteArrayOutputStream paramByteArrayOutputStream) {
        boolean bool = false;
        BufferedInputStream localBufferedInputStream;
        try {
            int i = paramHttpURLConnection.getResponseCode();
            bool = false;
            if (i == 200) {
                localBufferedInputStream = new BufferedInputStream(paramHttpURLConnection.getInputStream());
                byte[] arrayOfByte = new byte[1024];
                while (true) {
                    int j = localBufferedInputStream.read(arrayOfByte);
                    if (j == -1)
                        break;
                    paramByteArrayOutputStream.write(arrayOfByte, 0, j);
                }
                localBufferedInputStream.close();
                return true;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return false;


    }

    public static boolean postSync(String paramString1, String paramString2, ByteArrayOutputStream paramByteArrayOutputStream) {
        return false;
//    HttpURLConnection localHttpURLConnection = null;
//    try
//    {
//      localHttpURLConnection = (HttpURLConnection)new URL(paramString1).openConnection();
//      CookieContiner.fillCookieHeaders(localHttpURLConnection);
//      setPrivateHeader(localHttpURLConnection);
//      localHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//      localHttpURLConnection.setRequestMethod("POST");
//      localHttpURLConnection.setDoOutput(true);
//      localHttpURLConnection.setChunkedStreamingMode(0);
//      localHttpURLConnection.connect();
//      byte[] arrayOfByte = ReadFileData(paramString2);
//      if (arrayOfByte != null)
//      {
//        BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localHttpURLConnection.getOutputStream());
//        localBufferedOutputStream.write(arrayOfByte);
//        localBufferedOutputStream.flush();
//        localBufferedOutputStream.close();
//      }
//      boolean bool2 = getResponse(localHttpURLConnection, paramByteArrayOutputStream);
//      bool1 = bool2;
//      return bool1;
//    }
//    catch (Exception localException)
//    {
//      while (true)
//      {
//        localException.printStackTrace();
//        localHttpURLConnection.disconnect();
//        boolean bool1 = false;
//      }
//    }
//    finally
//    {
//      localHttpURLConnection.disconnect();
//    }
    }

    public static boolean sendSync(String paramString, ByteArrayOutputStream paramByteArrayOutputStream) {
        return false;
//    HttpURLConnection localHttpURLConnection = null;
//    try
//    {
//      localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection();
//      CookieContiner.fillCookieHeaders(localHttpURLConnection);
//      setPrivateHeader(localHttpURLConnection);
//      localHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//      localHttpURLConnection.connect();
//      int i = localHttpURLConnection.getContentLength();
//      int j = MAX_HEADER_CONTENT_SIZE;
//      bool1 = false;
//      if (i <= j)
//      {
//        boolean bool2 = getResponse(localHttpURLConnection, paramByteArrayOutputStream);
//        bool1 = bool2;
//      }
//      return bool1;
//    }
//    catch (Exception localException)
//    {
//      while (true)
//      {
//        localException.printStackTrace();
//        boolean bool1 = false;
//        if (localHttpURLConnection != null)
//        {
//          localHttpURLConnection.disconnect();
//          bool1 = false;
//        }
//      }
//    }
//    finally
//    {
//      if (localHttpURLConnection != null)
//        localHttpURLConnection.disconnect();
//    }
    }

    public static boolean sendSync(String paramString, StringBuffer paramStringBuffer) {
        return false;
//    try
//    {
//      HttpGet localHttpGet = new HttpGet(paramString);
//      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
//      CookieContiner.fillCookieHeaders(localHttpGet);
//      setPrivateHeader(localHttpGet);
//      HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
//      int i = localHttpResponse.getStatusLine().getStatusCode();
//      bool = false;
//      if (i == 200)
//      {
//        paramStringBuffer.append(EntityUtils.toString(localHttpResponse.getEntity(), "utf-8"));
//        bool = true;
//      }
//      return bool;
//    }
//    catch (Exception localException)
//    {
//      while (true)
//      {
//        localException.printStackTrace();
//        boolean bool = false;
//      }
//    }
    }

    //public static void setPrivateHeader(HttpURLConnection paramHttpURLConnection) {
      //  paramHttpURLConnection.setRequestProperty(HTTP_PRIVATE_HEADER_KEY, HTTP_PRIVATE_HEADER_VALUE);
    //}

    //public static void setPrivateHeader(HttpGet paramHttpGet) {
        //paramHttpGet.setHeader(HTTP_PRIVATE_HEADER_KEY, HTTP_PRIVATE_HEADER_VALUE);
    //}

    public void post(String paramString1, String paramString2, String paramString3) {
        try {
            AsyncTaskHttpRequest localAsyncTaskHttpRequest = new AsyncTaskHttpRequest();
            String[] arrayOfString = new String[4];
            arrayOfString[0] = paramString3;
            arrayOfString[1] = REQUESTTYPE_POSTFILE;
            arrayOfString[2] = paramString1;
            arrayOfString[3] = paramString2;
            localAsyncTaskHttpRequest.execute(arrayOfString);
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public void send(String paramString1, boolean paramBoolean, String paramString2) {
        if (paramBoolean) ;
        try {
            for (String str = REQUESTTYPE_GET; ; str = REQUESTTYPE_GETBIN) {
                new AsyncTaskHttpRequest().execute(new String[]{paramString2, str, paramString1, ""});
                break;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    class AsyncTaskHttpRequest extends AsyncTask<String, Integer, HttpRequest.AsyncTaskHttpResult> {
        AsyncTaskHttpRequest() {
        }

        protected HttpRequest.AsyncTaskHttpResult doInBackground(String[] paramArrayOfString) {

//      String str1 = paramArrayOfString[0];
//      String str2 = paramArrayOfString[1];
//      String str3 = paramArrayOfString[2];
//      String str4 = paramArrayOfString[3];
//      HttpRequest.AsyncTaskHttpResult localAsyncTaskHttpResult;
//      if (HttpRequest.REQUESTTYPE_GETBIN == str2)
//      {
//        ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
//        boolean bool1 = HttpRequest.sendSync(str3, localByteArrayOutputStream1);
//        localAsyncTaskHttpResult = new HttpRequest.AsyncTaskHttpResult(HttpRequest.this, bool1, HttpRequest.Bytes2String(localByteArrayOutputStream1.toByteArray()), str1);
//      }
//      while (true)
//      {
//        return localAsyncTaskHttpResult;
//        if (HttpRequest.REQUESTTYPE_POSTFILE == str2)
//        {
//          ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
//          boolean bool2 = HttpRequest.postSync(str3, str4, localByteArrayOutputStream2);
//          localAsyncTaskHttpResult = new HttpRequest.AsyncTaskHttpResult(HttpRequest.this, bool2, HttpRequest.Bytes2String(localByteArrayOutputStream2.toByteArray()), str1);
//        }
//        else
//        {
//          StringBuffer localStringBuffer = new StringBuffer();
//          boolean bool3 = HttpRequest.sendSync(str3, localStringBuffer);
//          localAsyncTaskHttpResult = new HttpRequest.AsyncTaskHttpResult(HttpRequest.this, bool3, localStringBuffer.toString(), str1);
//        }
//      }
        }

        protected void onPostExecute(HttpRequest.AsyncTaskHttpResult paramAsyncTaskHttpResult) {
            super.onPostExecute(paramAsyncTaskHttpResult);
            if (HttpRequest.this.m_event != null)
                HttpRequest.this.m_event.onResponse(paramAsyncTaskHttpResult.m_bResult, paramAsyncTaskHttpResult.m_strResonse, paramAsyncTaskHttpResult.m_strContext);
        }
    }

    class AsyncTaskHttpResult {
        public boolean m_bResult = false;
        public String m_strContext = null;
        public String m_strResonse = null;

        public AsyncTaskHttpResult(boolean paramString1, String paramString2, String arg4) {
            this.m_bResult = paramString1;
            this.m_strResonse = paramString2;
            Object localObject;
            this.m_strContext = localObject;
        }
    }
}

/* Location:           D:\software\onekey-decompile-apk好用版本\pandalive_1.0.0.1097.apk.jar
 * Qualified Name:     com.panda.videolivecore.net.http.HttpRequest
 * JD-Core Version:    0.6.1
 */