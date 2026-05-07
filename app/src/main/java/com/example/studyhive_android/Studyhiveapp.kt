package com.example.studyhive_android

import android.app.Application
import com.example.studyhive_android.data.network.RetrofitClient
import com.example.studyhive_android.data.network.SupabaseClient


class StudyHiveApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialise Supabase SDK with URL + anon key from BuildConfig
        SupabaseClient.init(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseAnonKey = BuildConfig.SUPABASE_ANON_KEY
        )

        // Initialise Retrofit / OkHttp (needs context for DataStore token persistence)
        RetrofitClient.init(this)
    }
}