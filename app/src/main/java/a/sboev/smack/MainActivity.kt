package a.sboev.smack

import a.sboev.smack.databinding.ActivityMainBinding
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private var mService: ConnectXmpp? = null
    private var bounded = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as LocalBinder<*>).getService().also { mService = it as ConnectXmpp? }
            bounded = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            bounded = false
            Log.d(TAG, "Service disconnected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignin.setOnClickListener {
            val userIdEditText = binding.txtUser
            val userPwdEditText = binding.txtPwd
            val userName = userIdEditText.text.toString()
            val password = userPwdEditText.text.toString()
            val intent = Intent(baseContext, ConnectXmpp::class.java)
            intent.putExtra("user", userName)
            intent.putExtra("pwd",password)
            startService(intent)
        }

        binding.btnSend.setOnClickListener {
            mService?.xmpp?.sendMsg()
        }



        /*val runnable = {
            val config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("andreysv1", "P@ssw0rd")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                .setXmppDomain("jix.im")
                .setHost("jix.im")
                .setSendPresence(true)
                .build()

            val connection = XMPPTCPConnection(config)
            connection.connect()
            connection.login()

            //Thread.sleep(2000)

            val chatManager = ChatManager.getInstanceFor(connection)
            val jid = JidCreate.entityBareFrom("andreysv@jabber.eu.org")
            val chat = chatManager.chatWith(jid)
            chat.send("Hello")

        }

        val thread = Thread(runnable)
        thread.start()*/
    }
}