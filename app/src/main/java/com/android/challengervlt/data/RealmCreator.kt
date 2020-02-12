package com.android.challengervlt.data

import android.content.Context
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration

object RealmCreator {
    private const val REALM_FILE_NAME = "rvlt_rates.realm"

    fun create(context: Context): Realm {
        Realm.init(context)
        Stetho.initialize(
            Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(
                    RealmInspectorModulesProvider.builder(context)
                        .withMetaTables()
                        .build()
                )
                .build()
        )
        val configuration = RealmConfiguration.Builder()
            .name(REALM_FILE_NAME)
            .modules(RealmModule())
            .schemaVersion(1)
            .build()
        return Realm.getInstance(configuration)
    }
}
