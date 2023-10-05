package a.sboev.smack

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ConnectXmpp : Service() {

    private var userName = ""
    private var password = ""
    val xmpp = MyXMPP(this)

    override fun onBind(intent: Intent): IBinder {
        return LocalBinder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        userName = intent?.getStringExtra("user").toString()
        password = intent?.getStringExtra("password").toString()
        xmpp.init(userName, password)
        xmpp.connectConnection()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        xmpp.disconnectConnection()
        super.onDestroy()
    }

}