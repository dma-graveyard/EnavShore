package dk.frv.enav.common.net.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class DTJSSESSLProtocolSocketFactory implements SecureProtocolSocketFactory {
    protected boolean checkCommonName = true;
    protected boolean relaxedServerCertCheck = false;
    protected boolean useKeyStoreTrustedCertificates = false;
    protected String keyStoreFileName = null;
    protected String keyStorePassword = null;
    protected String proxyHost = null;
    protected int proxyPort = 0;
    protected int socketTimeout = 0;
    protected static Boolean initiatedProvider = new Boolean(false);

    public DTJSSESSLProtocolSocketFactory() {
        super();

        setupSSLProvider();
    }

    public static void setupSSLProvider() {
        synchronized (initiatedProvider) {
            if (!initiatedProvider.booleanValue()) {
                java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                initiatedProvider = new Boolean(true);
            }
        }
    }

    /**
     * Specifies whether or not the common name
     * of the server certificate should be checked
     * against the domain name used for the connection.
     * As a general rule, this check should always
     * be made. However, in some certain cases
     * it may be necessary not to check this,
     * for example if only the IP number of the
     * remote server is given.
     */
    public void setCheckCommonName(boolean checkCommonName) {
        this.checkCommonName = checkCommonName;
    }

    /** 
     * If relaxedServerCertCheck is set to true, then
     * all server certificates will be accepted
     * (i.e. there is no need for a trusted CA
     * keystore, and all self-signed certificates
     * will be accepted).
     *
     * @param relaxedServerCertCheck
     */
    public void setRelaxedServerCertCheck(boolean relaxedServerCertCheck) {
        this.relaxedServerCertCheck = relaxedServerCertCheck;
    }

    public void setUseKeyStoreTrustedCertificates(boolean useKeyStoreTrustedCertificates) {
        this.useKeyStoreTrustedCertificates = useKeyStoreTrustedCertificates;
    }

    public String getKeyStoreFileName() {
        return keyStoreFileName;
    }

    public void setKeyStoreFileName(String v) {
        this.keyStoreFileName = v;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String v) {
        this.keyStorePassword = v;
    }

    public void setTimeout(int timeout) {
        this.socketTimeout = timeout;
    }

    /**
     * Sets the proxy host and port to use when
     * establishing the connection. To connect
     * directly without a proxy, set proxyHost 
     * to null. This is the default
     * if this method is never called.
     */
    public void setProxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort, HttpConnectionParams connectionParams)
                                                                                                                                    throws IOException,
                                                                                                                                    UnknownHostException {
        try {
            SSLSocketFactory sf = getSSLSocketFactory();

            SSLSocket sslSocket = null;
            if (proxyHost == null) {
                sslSocket = (SSLSocket) sf.createSocket(host, port, clientHost, clientPort);
            } else {
                SocketFactory sockFactory = SocketFactory.getDefault();
                Socket proxySocket = sockFactory.createSocket(proxyHost, proxyPort);
                doTunnelHandshake(proxySocket, host, port);
                sslSocket = (SSLSocket) sf.createSocket(proxySocket, host, port, false);
            }
            checkCommonName(sslSocket);

            sslSocket.setSoTimeout(socketTimeout);
            return sslSocket;
        } catch (java.security.GeneralSecurityException e) {
            throw new IOException(e.toString() + " " + e.getMessage());
        }
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException,
                                                                                             UnknownHostException {
        try {
            SSLSocketFactory sf = getSSLSocketFactory();

            SSLSocket sslSocket = null;
            if (proxyHost == null) {
                sslSocket = (SSLSocket) sf.createSocket(host, port, clientHost, clientPort);
            } else {
                SocketFactory sockFactory = SocketFactory.getDefault();
                Socket proxySocket = sockFactory.createSocket(proxyHost, proxyPort);
                doTunnelHandshake(proxySocket, host, port);
                sslSocket = (SSLSocket) sf.createSocket(proxySocket, host, port, false);
            }
            checkCommonName(sslSocket);

            sslSocket.setSoTimeout(socketTimeout);
            return sslSocket;
        } catch (java.security.GeneralSecurityException e) {
            throw new IOException(e.toString() + " " + e.getMessage());
        }
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        try {
            SSLSocketFactory sf = getSSLSocketFactory();

            SSLSocket sslSocket = null;
            if (proxyHost == null) {
                sslSocket = (SSLSocket) sf.createSocket(host, port);
            } else {
                SocketFactory sockFactory = SocketFactory.getDefault();
                Socket proxySocket = sockFactory.createSocket(proxyHost, proxyPort);
                doTunnelHandshake(proxySocket, host, port);
                sslSocket = (SSLSocket) sf.createSocket(proxySocket, host, port, false);
            }

            checkCommonName(sslSocket);

            sslSocket.setSoTimeout(socketTimeout);
            return sslSocket;
        } catch (java.security.GeneralSecurityException e) {
            throw new IOException(e.toString() + " " + e.getMessage());
        }

    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        try {
            SSLSocketFactory sf = getSSLSocketFactory();

            SSLSocket sslSocket = (SSLSocket) sf.createSocket(socket, host, port, autoClose);

            checkCommonName(sslSocket);

            sslSocket.setSoTimeout(socketTimeout);
            return sslSocket;
        } catch (java.security.GeneralSecurityException e) {
            throw new IOException(e.toString() + " " + e.getMessage());
        }
    }

    protected SSLSocketFactory getSSLSocketFactory() throws java.security.NoSuchAlgorithmException,
                                                    java.security.KeyStoreException, java.security.KeyManagementException,
                                                    java.security.cert.CertificateException,
                                                    java.security.UnrecoverableKeyException, java.io.FileNotFoundException,
                                                    java.io.IOException {
        SSLSocketFactory factory = null;

        if (keyStorePassword != null) {
            SSLContext context = SSLContext.getInstance("TLS");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");

            FileInputStream file_is = null;
            if (keyStoreFileName != null) file_is = new FileInputStream(keyStoreFileName);

            ks.load(file_is, keyStorePassword.toCharArray());

            kmf.init(ks, keyStorePassword.toCharArray());

            TrustManager[] trustManagers = null;
            if (this.useKeyStoreTrustedCertificates) {
                String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
                trustMgrFactory.init(ks);
                trustManagers = trustMgrFactory.getTrustManagers();
                // trustManagers = null;
            }

            context.init(kmf.getKeyManagers(), trustManagers, null);

            factory = context.getSocketFactory();
        } else if (relaxedServerCertCheck) {
            // The relaxed server cert check
            // will not throw exceptions if
            // the server certificate is
            // self-signed.
            //KeyManager[] km = null;
            TrustManager[] tm = { new RelaxedX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
            factory = sslContext.getSocketFactory();
        } else {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        return factory;
    }

    /*
     * Tell our tunnel where we want to CONNECT, and look for the
     * right reply.  Throw IOException if anything goes wrong.
     */
    private void doTunnelHandshake(Socket tunnel, String host, int port) throws IOException {
        java.io.OutputStream out = tunnel.getOutputStream();
        String msg = "CONNECT " + host + ":" + port + " HTTP/1.0\n" + "User-Agent: "
                     + sun.net.www.protocol.http.HttpURLConnection.userAgent + "\r\n\r\n";
        byte b[];
        try {
            /*
             * We really do want ASCII7 -- the http protocol doesn't change
             * with locale.
             */
            b = msg.getBytes("ASCII7");
        } catch (java.io.UnsupportedEncodingException ignored) {
            /*
             * If ASCII7 isn't there, something serious is wrong, but
             * Paranoia Is Good (tm)
             */
            b = msg.getBytes();
        }
        out.write(b);
        out.flush();

        /*
         * We need to store the reply so we can create a detailed
         * error message to the user.
         */
        byte reply[] = new byte[200];
        int replyLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false; /* Done on first newline */

        java.io.InputStream in = tunnel.getInputStream();
        //boolean error = false;

        while (newlinesSeen < 2) {
            int i = in.read();
            if (i < 0) {
                throw new IOException("Unexpected EOF from proxy");
            }
            if (i == '\n') {
                headerDone = true;
                ++newlinesSeen;
            } else if (i != '\r') {
                newlinesSeen = 0;
                if (!headerDone && replyLen < reply.length) {
                    reply[replyLen++] = (byte) i;
                }
            }
        }

        /*
         * Converting the byte array to a string is slightly wasteful
         * in the case where the connection was successful, but it's
         * insignificant compared to the network overhead.
         */
        String replyStr;
        try {
            replyStr = new String(reply, 0, replyLen, "ASCII7");
        } catch (java.io.UnsupportedEncodingException ignored) {
            replyStr = new String(reply, 0, replyLen);
        }

        /* We asked for HTTP/1.0, so we should get that back */
        if (!replyStr.startsWith("HTTP/1.0 200")) {
            throw new IOException("Unable to tunnel through " + proxyHost + ":" + proxyPort + ".  Proxy returns \"" + replyStr
                                  + "\"");
        }

        /* tunneling Handshake was successful! */
    }

    /**
     * 
     *
     * @param socket a <code>SSLSocket</code> value
     * @exception SSLPeerUnverifiedException  If there are problems obtaining
     * the server certificates from the SSL session, or the server host name 
     * does not match with the "Common Name" in the server certificates 
     * SubjectDN.
     * @exception UnknownHostException  If we are not able to resolve
     * the SSL sessions returned server host name. 
     */
    private void checkCommonName(SSLSocket socket) throws SSLPeerUnverifiedException, UnknownHostException {
        if (!checkCommonName) return;

        SSLSession session = socket.getSession();
        String hostname = session.getPeerHost();
        try {
            @SuppressWarnings("unused")
            InetAddress addr = InetAddress.getByName(hostname);
        } catch (UnknownHostException uhe) {
            throw new UnknownHostException("Could not resolve SSL sessions " + "server hostname: " + hostname);
        }

        X509Certificate[] certs = session.getPeerCertificateChain();
        if (certs == null || certs.length == 0) throw new SSLPeerUnverifiedException("No server certificates found!");

        //get the servers DN in its string representation
        String dn = certs[0].getSubjectDN().getName();

        //         //might be useful to print out all certificates we receive from the
        //         //server, in case one has to debug a problem with the installed certs.
        //         if (LOG.isDebugEnabled()) {
        //             LOG.debug("Server certificate chain:");
        //             for (int i = 0; i < certs.length; i++) {
        //                 LOG.debug("X509Certificate[" + i + "]=" + certs[i]);
        //             }
        //         }
        //get the common name from the first cert
        String cn = getCN(dn);
        if (hostname.equalsIgnoreCase(cn)) {
            //             if (LOG.isDebugEnabled()) {
            //                 LOG.debug("Target hostname valid: " + cn);
            //             }
        } else {
            throw new SSLPeerUnverifiedException("HTTPS hostname invalid: expected '" + hostname + "', received '" + cn + "'");
        }
    }

    /**
     * Parses a X.500 distinguished name for the value of the 
     * "Common Name" field.
     * This is done a bit sloppy right now and should probably be done a bit
     * more according to <code>RFC 2253</code>.
     *
     * @param dn  a X.500 distinguished name.
     * @return the value of the "Common Name" field.
     */
    private String getCN(String dn) {
        int i = 0;
        i = dn.indexOf("CN=");
        if (i == -1) {
            return null;
        }
        //get the remaining DN without CN=
        dn = dn.substring(i + 3);
        // System.out.println("dn=" + dn);
        char[] dncs = dn.toCharArray();
        for (i = 0; i < dncs.length; i++) {
            if (dncs[i] == ',' && i > 0 && dncs[i - 1] != '\\') {
                break;
            }
        }
        return dn.substring(0, i);
    }

    private class RelaxedX509TrustManager implements X509TrustManager {
        @SuppressWarnings("unused")
        public boolean isClientTrusted(java.security.cert.X509Certificate[] chain) {
            return true;
        }

        @SuppressWarnings("unused")
        public boolean isServerTrusted(java.security.cert.X509Certificate[] chain) {
            return true;
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            // Do nothing
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            // Do nothing
        }
    }

}
