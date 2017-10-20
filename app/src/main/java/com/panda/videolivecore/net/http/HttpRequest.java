package com.panda.videolivecore.net.http;

import android.os.AsyncTask;
import com.panda.videolivecore.CoreApplication;
import com.panda.videolivecore.utils.CookieContiner;
//import com.umeng.message.proguard.C0035k;
//import com.umeng.message.proguard.e;
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
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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

    class AsyncTaskHttpRequest extends AsyncTask<String, Integer, AsyncTaskHttpResult> {
        AsyncTaskHttpRequest() {
        }

        protected AsyncTaskHttpResult doInBackground(String... params) {
            String strContext = params[0];
            String strType = params[1];
            String strUrl = params[2];
            String strPostFilePath = params[3];
            ByteArrayOutputStream buffer;
            if (HttpRequest.REQUESTTYPE_GETBIN == strType) {
                buffer = new ByteArrayOutputStream();
                return new AsyncTaskHttpResult(HttpRequest.sendSync(strUrl, buffer), HttpRequest.Bytes2String(buffer.toByteArray()), strContext);
            } else if (HttpRequest.REQUESTTYPE_POSTFILE == strType) {
                buffer = new ByteArrayOutputStream();
                return new AsyncTaskHttpResult(HttpRequest.postSync(strUrl, strPostFilePath, buffer), HttpRequest.Bytes2String(buffer.toByteArray()), strContext);
            } else {
                StringBuffer buffer2 = new StringBuffer();
                return new AsyncTaskHttpResult(HttpRequest.sendSync(strUrl, buffer2), buffer2.toString(), strContext);
            }
        }

        protected void onPostExecute(AsyncTaskHttpResult result) {
            super.onPostExecute(result);
            if (HttpRequest.this.m_event != null) {
                HttpRequest.this.m_event.onResponse(result.m_bResult, result.m_strResonse, result.m_strContext);
            }
        }
    }

    class AsyncTaskHttpResult {
        public boolean m_bResult = false;
        public String m_strContext = null;
        public String m_strResonse = null;

        public AsyncTaskHttpResult(boolean bResult, String strReponse, String strContext) {
            this.m_bResult = bResult;
            this.m_strResonse = strReponse;
            this.m_strContext = strContext;
        }
    }

    public HttpRequest(IHttpRequestEvent event) {
        this.m_event = event;
    }

    public void send(String strUrl, boolean bIsNormalGet, String strContext) {
        String strType;
        if (bIsNormalGet) {
            try {
                strType = REQUESTTYPE_GET;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        strType = REQUESTTYPE_GETBIN;
        new AsyncTaskHttpRequest().execute(new String[]{strContext, strType, strUrl, ""});
    }

    public void post(String strUrl, String strPostFilePath, String strContext) {
        try {
            new AsyncTaskHttpRequest().execute(new String[]{strContext, REQUESTTYPE_POSTFILE, strUrl, strPostFilePath});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean sendSync(String strUrl, StringBuffer buffer) {
        try {
            HttpGet getMethod = new HttpGet(strUrl);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            CookieContiner.fillCookieHeaders(getMethod);
            setPrivateHeader(getMethod);
            HttpResponse response = httpClient.execute(getMethod);
            if (response.getStatusLine().getStatusCode() != 200) {
                return false;
            }
            buffer.append(EntityUtils.toString(response.getEntity(), "utf-8"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendSync(String strUrl, ByteArrayOutputStream baos) {
        boolean bReturn = false;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            CookieContiner.fillCookieHeaders(urlConnection);
            setPrivateHeader(urlConnection);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.connect();
            if (urlConnection.getContentLength() <= MAX_HEADER_CONTENT_SIZE) {
                bReturn = getResponse(urlConnection, baos);
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        } catch (Throwable th) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bReturn;
    }

    public static boolean postSync(String strUrl, String strPostFilePath, ByteArrayOutputStream baos) {
        boolean bReturn = false;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            CookieContiner.fillCookieHeaders(urlConnection);
            setPrivateHeader(urlConnection);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.connect();
            byte[] postData = ReadFileData(strPostFilePath);
            if (postData != null) {
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(postData);
                out.flush();
                out.close();
            }
            bReturn = getResponse(urlConnection, baos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return bReturn;
    }

    public static boolean getResponse(HttpURLConnection urlConnection, ByteArrayOutputStream baos) {
        try {
            if (urlConnection.getResponseCode() != 200) {
                return false;
            }
            InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
            byte[] buffer = new byte[1024];
            while (true) {
                int len = inStream.read(buffer);
                if (len != -1) {
                    baos.write(buffer, 0, len);
                } else {
                    inStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setPrivateHeader(HttpGet getMethod) {
        getMethod.setHeader(HTTP_PRIVATE_HEADER_KEY, HTTP_PRIVATE_HEADER_VALUE);
    }

    public static void setPrivateHeader(HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty(HTTP_PRIVATE_HEADER_KEY, HTTP_PRIVATE_HEADER_VALUE);
    }

    public static byte[] String2Bytes(String strData) {
        byte[] data = null;
        try {
            data = strData.getBytes(CHARSET_NAME_BYTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String Bytes2String(byte[] data) {
        String strContent = "";
        try {
            if (data.length <= 4194304) {
                return new String(data, CHARSET_NAME_BYTES);
            }
            return strContent;
        } catch (Exception e) {
            e.printStackTrace();
            return strContent;
        }
    }

    public static String URLEncoder(String strData) {
        String strReturn = "";
        try {
            strReturn = URLEncoder.encode(strData, CHARSET_NAME_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }

    public static byte[] ReadFileData(String strFilePath) {
        try {
            File file = new File(strFilePath);
            if (file == null || !file.exists()) {
                return null;
            }
            InputStream in = new FileInputStream(file);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String URLAppendVersionPlat(String strUrl, boolean bFirstParam) {
        String strReturn = "";
        if (strUrl.isEmpty()) {
            return strReturn;
        }
        strReturn = strReturn + strUrl + (bFirstParam ? "?" : "&") + "__version=" + CoreApplication.getInstance().version() + "&__plat=android";
        String strToken = CookieContiner.getToken();
        if (!strToken.isEmpty()) {
            strReturn = strReturn + "&" + strToken;
        }
        return strReturn;
    }

    public static String URLAppendVersionPlatNoToken(String strUrl, boolean bFirstParam) {
        String strReturn = "";
        if (strUrl.isEmpty()) {
            return strReturn;
        }
        return strReturn + strUrl + (bFirstParam ? "?" : "&") + "__version=" + CoreApplication.getInstance().version() + "&__plat=android";
    }
}
