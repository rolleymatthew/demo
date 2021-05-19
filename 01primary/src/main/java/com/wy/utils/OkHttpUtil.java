package com.wy.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: dizhang216861
 * @Date: 2019/11/29 11:45
 */
public class OkHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    private final static OkHttpClient okHttpClient;

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    static {
        okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                //是否开启缓存
                .retryOnConnectionFailure(true)
                .connectionPool(pool())
                //连接池
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(20L, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static String doGet(String url) {
        return doGet(url,null);
    }

    public static String doGet(String url, Map<String,String> header) {
        Request.Builder builder = new Request.Builder();
        if(header != null && header.size() != 0){
            for(Map.Entry<String,String> e:header.entrySet()) {
                builder.addHeader(e.getKey(),e.getValue());
            }
        }
        Request request = builder.url(url).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            logger.error("error get with url[{}]",url,e);
        }
        return null;
    }

    public static String doPost(String url, Map<String,String> params, Map<String,String> headers){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if(params!= null && params.size()>0) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                String value = params.get(key);
                formBodyBuilder.add(key, value);
            }
        }
        FormBody formBody = formBodyBuilder.build();

        Request.Builder builder = new Request.Builder();
        if(formBody.contentLength()>0){
            builder.post(formBody);
        }
        if(headers != null && headers.size()>0){
            for(Map.Entry<String,String> e:headers.entrySet()){
                builder.addHeader(e.getKey(),e.getValue());
            }
        }
        Request request = builder.url(url).build();

        String res = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            res = response.body().string();
        }catch (Exception e){
            logger.error("error post with url[{}]",url,e);
        }
        return res;
    }

    public static String doPostJson(String url, Map<String,String> params, Map<String,String> headers){
        Request.Builder builder = new Request.Builder();
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(params));
        if(headers != null && headers.size()>0){
            for(Map.Entry<String,String> e:headers.entrySet()){
                builder.header(e.getKey(),e.getValue());
            }
        }
        Request request = builder.url(url).post(requestBody).build();
        String res = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            res = response.body().string();
        }catch (Exception e){
            logger.error("error post with url[{}]",url,e);
        }
        return res;
    }

    public static String doPostJson(String url, String jsonStr, Map<String,String> headers){
        Request.Builder builder = new Request.Builder();
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, String.valueOf(jsonStr));
        if(headers != null && headers.size()>0){
            for(Map.Entry<String,String> e:headers.entrySet()){
                builder.header(e.getKey(),e.getValue());
            }
        }
        Request request = builder.url(url).post(requestBody).build();
        String res = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            res = response.body().string();
        }catch (Exception e){
            logger.error("error post with url[{}]",url,e);
        }
        return res;
    }

    public static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public static SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new connection pool with tuning parameters appropriate for a single-user application.
     * The tuning parameters in this pool are subject to change in future OkHttp releases. Currently
     */

    public static ConnectionPool pool() {
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }

}
