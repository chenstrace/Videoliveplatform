package kd.push.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public final class HttpClient {
    public static String get(String strUrl) {
        HttpURLConnection httpConn = null;
        StringBuilder builder = new StringBuilder();
        try {
            httpConn = (HttpURLConnection) new URL(strUrl).openConnection();
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("GET");
            OutputStream out = httpConn.getOutputStream();
            out.flush();
            out.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String str = "";
            while (true) {
                str = rd.readLine();
                if (str == null) {
                    break;
                }
                builder.append(str).append("\n");
            }
            rd.close();
            if (httpConn != null) {
                httpConn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return builder.toString();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String[] ipList = get("http://md.openapi.360.cn/list/get").split("\n");
        System.out.println(ipList[new Random().nextInt(ipList.length)]);
    }
}
