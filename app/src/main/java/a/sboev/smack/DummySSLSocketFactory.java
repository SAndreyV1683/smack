package a.sboev.smack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class DummySSLSocketFactory extends SSLSocketFactory {

    /** The default instance. */
    private static SocketFactory instance;

    /** The delegate. */
    private SSLSocketFactory delegate;


    /**
     * Creates a new instance of DummySSLSocketFactory.
     */
    public DummySSLSocketFactory()
    {
        try
        {
            TrustManager tm = new X509TrustManager()
            {
                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return new X509Certificate[0];
                }


                @Override
                public void checkClientTrusted( X509Certificate[] arg0, String arg1 ) throws CertificateException
                {
                }


                @Override
                public void checkServerTrusted( X509Certificate[] arg0, String arg1 ) throws CertificateException
                {
                }
            };
            TrustManager[] tma =
                    { tm };
            SSLContext sc = SSLContext.getInstance( "TLS" );
            sc.init( null, tma, new SecureRandom() );
            delegate = sc.getSocketFactory();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     * Gets the default instance.
     *
     * Note: This method is invoked from the JNDI framework when
     * creating a ldaps:// connection.
     *
     * @return the default instance
     */
    public static SocketFactory getDefault()
    {
        if ( instance == null )
        {
            instance = new DummySSLSocketFactory();
        }
        return instance;
    }


    /**
     * @see javax.net.ssl.SSLSocketFactory#getDefaultCipherSuites()
     */
    @Override
    public String[] getDefaultCipherSuites()
    {
        return delegate.getDefaultCipherSuites();
    }


    /**
     * @see javax.net.ssl.SSLSocketFactory#getSupportedCipherSuites()
     */
    @Override
    public String[] getSupportedCipherSuites()
    {
        return delegate.getSupportedCipherSuites();
    }


    /**
     * @see javax.net.ssl.SSLSocketFactory#createSocket(java.net.Socket, java.lang.String, int, boolean)
     */
    @Override
    public Socket createSocket( Socket arg0, String arg1, int arg2, boolean arg3 ) throws IOException
    {
        try
        {
            return delegate.createSocket( arg0, arg1, arg2, arg3 );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
     */
    @Override
    public Socket createSocket( String arg0, int arg1 ) throws IOException
    {
        try
        {
            return delegate.createSocket( arg0, arg1 );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
     */
    @Override
    public Socket createSocket( InetAddress arg0, int arg1 ) throws IOException
    {
        try
        {
            return delegate.createSocket( arg0, arg1 );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
     */
    @Override
    public Socket createSocket( String arg0, int arg1, InetAddress arg2, int arg3 ) throws IOException
    {
        try
        {
            return delegate.createSocket( arg0, arg1, arg2, arg3 );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int, java.net.InetAddress, int)
     */
    @Override
    public Socket createSocket( InetAddress arg0, int arg1, InetAddress arg2, int arg3 ) throws IOException
    {
        try
        {
            return delegate.createSocket( arg0, arg1, arg2, arg3 );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw e;
        }
    }
}