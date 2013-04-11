package framework;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class CommonHttpClient {

    private final Log log = LogFactory.getLog(CommonHttpClient.class);
    private final HttpClient client;
    private final CookieStore cookieStore;
    private final HttpContext localContext;

    public CommonHttpClient() {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http",
                                           80,
                                           PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https",
                                           443,
                                           SSLSocketFactory.getSocketFactory()));

        final PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);

        this.client = new DefaultHttpClient(cm);
        this.cookieStore = new BasicCookieStore();
        this.localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        this.localContext.setAttribute(ClientContext.COOKIE_STORE,
                                       this.cookieStore);

        final HttpParams params = this.client.getParams();
        params.setParameter(ClientPNames.COOKIE_POLICY,
                            CookiePolicy.BROWSER_COMPATIBILITY);
    }

    public String getData(final String url) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("method : GET");
            this.log.debug("   url : " + url);
        }
        final HttpGet httpget = new HttpGet(url);
        this.initHttpHeader(httpget);

        String result = null;
        try {
            // Pass local context as a parameter
            final HttpResponse response = this.client.execute(httpget,
                                                              this.localContext);
            final HttpEntity entity = response.getEntity();
            result = this.entityToString(entity);
            // Consume response content
            EntityUtils.consume(entity);
        } catch (final ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("result : " + result);
        }
        return result;
    }

    public String postData(final String url,
                           final List<? extends NameValuePair> nvps) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("method : POST");
            this.log.debug("   url : " + url);
            for (final NameValuePair nvp : nvps) {
                this.log.debug("form-" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        final HttpPost httppost = new HttpPost(url);
        this.initHttpHeader(httppost);

        String result = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nvps,
                                                        SystemConstants.ENCODING));

            final HttpResponse response = this.client.execute(httppost,
                                                              this.localContext);
            final HttpEntity entity = response.getEntity();
            result = this.entityToString(entity);

            // Consume response content
            EntityUtils.consume(entity);
        } catch (final ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("result : " + result);
        }
        return result;
    }

    public String findCookie(final String cookieName) {
        final List<Cookie> cookies = this.cookieStore.getCookies();
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    protected void initHttpHeader(final HttpMessage httpMessage) {
        httpMessage.addHeader("Accept",
                              "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpMessage.addHeader("Accept-Charset", "UTF-8;");
        httpMessage.addHeader("Accept-Encoding", "gzip, deflate");
        httpMessage.addHeader("Accept-Language",
                              "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        httpMessage.addHeader("Cache-Control", "no-cache");
        httpMessage.addHeader("Connection", "keep-alive");
        httpMessage.addHeader("Pragma", "no-cache");
        // httpMessage.addHeader("Referer", SinaHttpClient.REFERER);
        httpMessage.addHeader("User-Agent",
                              "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0");
    }

    private String entityToString(final HttpEntity entity)
            throws ParseException, IOException {
        String result = null;
        if (this.isGzip(entity)) {
            result = EntityUtils.toString(new GzipDecompressingEntity(entity));
        } else {
            result = EntityUtils.toString(entity, SystemConstants.ENCODING);
        }
        return result;
    }

    private boolean isGzip(final HttpEntity entity) {
        final Header contentEncodingHeader = entity.getContentEncoding();
        if (contentEncodingHeader != null) {
            final String contentEncoding = contentEncodingHeader.getValue();
            if (StringUtils.equalsIgnoreCase(contentEncoding, "gzip")) {
                return true;
            }
        }
        return false;
    }
}
