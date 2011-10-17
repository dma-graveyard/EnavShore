package dk.frv.enav.common.net.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.log4j.Logger;

public class HttpClient extends org.apache.commons.httpclient.HttpClient {

    private static final String USER_AGENT = "enavshore/HttpClient";
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    private static final int DEFAULT_READ_TIMEOUT = 60000;
    private static final String HTTPS = "https";

    private static Logger LOG = Logger.getLogger(HttpClient.class);
    protected static Object sslProtocolSocketFactoryMutex = new Object();

    private String url;
    private HttpMethod httpMethod;
    private String requestBody = "";
    private String contentType = "text/plain";
    private String charset = "utf-8";
    private int responseCode;
    private HttpParams requestParams;

    public HttpClient() {
        this(DEFAULT_READ_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
    }

    public HttpClient(int readTimeout, int connectTimeout) {
        super();
        getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int post() throws IOException {
        return post(new PostMethod(this.url));
    }

    public int post(PostMethod method) throws IOException {
        this.httpMethod = method;
        if (this.requestBody.length() > 0) {
            StringRequestEntity requestEntity;
            try {
                requestEntity = new StringRequestEntity(this.requestBody, contentType, charset);
            } catch (UnsupportedEncodingException e) {
                throw new IOException(e);
            }
            method.setRequestEntity(requestEntity);
        } else {
            for (String key : requestParams.keySet()) {
                method.addParameter(key, requestParams.getFirst(key));
            }

        }
        return execute();
    }

    public int execute() throws HttpException, IOException {
        httpMethod.setRequestHeader("User-Agent", USER_AGENT);
        httpMethod.setRequestHeader("Connection", "close");
        httpMethod.addRequestHeader("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain");

        // TODO: Add headers        

        responseCode = executeMethod(httpMethod);

        return responseCode;
    }

    public InputStream getResponseStream() {
        try {
            return httpMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            LOG.info("Failed to get response stream: " + e.getMessage());
            return null;
        }
    }

    public String getHeaderValue(String headerName) {
        Header header = httpMethod.getResponseHeader(headerName);
        if (header != null) {
            return header.getValue();
        }
        return null;
    }

    public String getResponseString() {
        StringBuilder builder = new StringBuilder();
        LinkedList<String> lines = new LinkedList<String>();
        InputStream in = getResponseStream();
        if (in == null) return "";
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine;
        try {
            while (((readLine = br.readLine()) != null)) {
                lines.addLast(readLine);
            }
        } catch (IOException e) {
            LOG.info("Error while reading HTTP response");
        }
        
        while (!lines.isEmpty()) {
            builder.append(lines.removeFirst());
            if (!lines.isEmpty()) {
                builder.append("\n");
            }
        }
        
        return builder.toString();
    }

    public void releaseConnection() {
        httpMethod.releaseConnection();        
    }
    
    public String getResponseHeaders() {        
        StringBuilder buf = new StringBuilder();
        Header[] headers = httpMethod.getResponseHeaders();
        if (headers != null) {
            for (Header header : headers) {
                buf.append(header.getName() + ": " + header.getValue() + "\n");
            }
        }
        return buf.toString();
    }

    public void setCheckCommonName(boolean checkCommonName) {
        synchronized (sslProtocolSocketFactoryMutex) {
            ProtocolSocketFactory factory = Protocol.getProtocol(HTTPS).getSocketFactory();
            ((DTJSSESSLProtocolSocketFactory) factory).setCheckCommonName(checkCommonName);
        }
    }

    public void setRelaxedServerCertCheck(boolean relaxedServerCertCheck) {
        synchronized (sslProtocolSocketFactoryMutex) {
            ProtocolSocketFactory factory = Protocol.getProtocol(HTTPS).getSocketFactory();
            ((DTJSSESSLProtocolSocketFactory) factory).setRelaxedServerCertCheck(relaxedServerCertCheck);
        }
    }

    public void setUseKeyStoreTrustedCertificates(boolean useKeyStoreTrustedCertificates) {
        synchronized (sslProtocolSocketFactoryMutex) {
            ProtocolSocketFactory factory = Protocol.getProtocol(HTTPS).getSocketFactory();
            ((DTJSSESSLProtocolSocketFactory) factory).setUseKeyStoreTrustedCertificates(useKeyStoreTrustedCertificates);
        }
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        setRequestBody(requestBody, "text/plain");
    }

    public void setRequestBody(String requestBody, String contentType) {
        this.requestBody = requestBody;
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public HttpParams getRequestParams() {
        return this.requestParams;
    }

    public void setRequestParams(HttpParams requestParams) {
        this.requestParams = requestParams;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
