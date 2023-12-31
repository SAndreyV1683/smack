package a.sboev.smack

import android.R
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.security.KeyStoreException
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.SASLAuthentication
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.SmackException.NotConnectedException
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.debugger.SmackDebugger
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.security.cert.CertificateException


class MyXMPP(private val context: Context) {

    private var userName = ""
    private var passWord = ""
    var connection: AbstractXMPPConnection? = null
    var chatManager: ChatManager? = null
    var newChat: Chat? = null
    private var connectionListener = XMPPConnectionListener()
    private var connected = false
    private val isToasted = false
    private var chat_created = false
    private var loggedin = false
    private var smackDebuger: SmackDebugger? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception handled: ${throwable.localizedMessage}") }
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)
    private fun onError(message: String) {
        Log.e(TAG, message)
    }

    //Initialize
    fun init(userId: String, pwd: String) {
        Log.i("XMPP", "Initializing!")
        userName = userId
        passWord = pwd
        AndroidSmackInitializer.initialize(context);
        val configBuilder = XMPPTCPConnectionConfiguration.builder()
        configBuilder.setUsernameAndPassword(userName, passWord)
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required)
        /*val sslContext = SSLContext.getDefault()
        configBuilder.setSocketFactory(sslContext.socketFactory)*/

        configBuilder.setXmppDomain(HOST)
        //configBuilder.setHost(HOST)
        configBuilder.setPort(PORT)
        connection = XMPPTCPConnection(configBuilder.build())
        (connection as XMPPTCPConnection).addConnectionListener(connectionListener)

        System.setProperty("smack.debuggerClass","org.jivesoftware.smack.debugger.ConsoleDebugger");
        System.setProperty("smack.debugEnabled", "true");
        SmackConfiguration.DEBUG = true
    }

    /*@Throws(
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        KeyManagementException::class,
        IOException::class,
        CertificateException::class
    )
    private fun createSSLContext(context: Context): SSLContext? {
        val trustStore: KeyStore
        var `in`: InputStream? = null
        trustStore = KeyStore.getInstance("BKS")
        `in` = context.resources.openRawResource(R.raw.my_keystore)
        trustStore.load(`in`, "MyPassword123".toCharArray())
        val trustManagerFactory = TrustManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
        return sslContext
    }*/



    // Disconnect Function
    fun disconnectConnection() {
        coroutineScope.launch {
            connection?.disconnect()
        }
    }

    fun connectConnection() {

        coroutineScope.launch {
            try {
                Log.d(TAG, "Connecting ...")
                connection!!.connect()
                connected = true
            } catch (e: IOException) {
                e.message?.let { Log.e(TAG, it) }
            } catch (e: SmackException) {
                e.message?.let { Log.e(TAG, it) }
            } catch (e: XMPPException) {
                e.message?.let { Log.e(TAG, it) }
            }
        }
    }

    fun sendMsg() {
        if (connection?.isConnected == true) {
            // Assume we've created an XMPPConnection name "connection"._
            chatManager = ChatManager.getInstanceFor(connection)
            val jid = JidCreate.entityBareFrom("andreysv@jabber.eu.org")
            newChat = chatManager?.chatWith(jid)
            try {
                newChat?.send("Howdy!")
            } catch (e: NotConnectedException) {
                e.printStackTrace()
            }
        }
    }

    fun login() {
        try {
            connection?.login(userName, passWord)
            Log.i("LOGIN", "Yey! We're connected to the Xmpp server!")
        } catch (e: XMPPException) {
            e.message?.let { Log.e(TAG, it) }
        } catch (e: SmackException) {
            e.message?.let { Log.e(TAG, it) }
        } catch (e: IOException) {
            e.message?.let { Log.e(TAG, it) }
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
        }
    }

    //Connection Listener to check connection state
    inner class XMPPConnectionListener : ConnectionListener {
        override fun connected(connection: XMPPConnection) {
            Log.d("xmpp", "Connected! isAuthenticated ${connection.isAuthenticated}")
            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            connected = true
            if (!connection.isAuthenticated) {
                login()
            }
        }

        override fun connectionClosed() {
            if (isToasted) Handler(Looper.getMainLooper()).post(Runnable {
                // TODO Auto-generated method stub
            })
            Log.d("xmpp", "ConnectionCLosed!")
            connected = false
            chat_created = false
            loggedin = false
        }

        override fun connectionClosedOnError(arg0: Exception) {
            if (isToasted) Handler(Looper.getMainLooper()).post(Runnable { })
            Log.d("xmpp", "ConnectionClosedOn Error!")
            connected = false
            chat_created = false
            loggedin = false
        }

        fun reconnectingIn(arg0: Int) {
            Log.d("xmpp", "Reconnectingin $arg0")
            loggedin = false
        }

        fun reconnectionFailed(arg0: Exception?) {
            if (isToasted) Handler(Looper.getMainLooper()).post(Runnable { })
            Log.d("xmpp", "ReconnectionFailed!")
            connected = false
            chat_created = false
            loggedin = false
        }

        fun reconnectionSuccessful() {
            if (isToasted) Handler(Looper.getMainLooper()).post(Runnable {
                // TODO Auto-generated method stub
            })
            Log.d("xmpp", "ReconnectionSuccessful")
            connected = true
            chat_created = false
            loggedin = false
        }

        override fun authenticated(arg0: XMPPConnection, arg1: Boolean) {
            Log.d("xmpp", "Authenticated!")
            loggedin = true
            chat_created = false
            Thread {
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {

                    e.printStackTrace()
                }
            }.start()
            if (isToasted) Handler(Looper.getMainLooper()).post(Runnable {

            })
        }
    }

    companion object {
        private const val HOST = "jix.im"
        private const val PORT = 5222
        private val TAG = MyXMPP::class.simpleName
    }
}