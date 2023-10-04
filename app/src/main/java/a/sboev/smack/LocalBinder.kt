package a.sboev.smack

import android.os.Binder
import java.lang.ref.WeakReference

class LocalBinder<S>(service: S): Binder() {
    private val mService = WeakReference(service)

    fun getService():S?{
        return mService.get()
    }
}