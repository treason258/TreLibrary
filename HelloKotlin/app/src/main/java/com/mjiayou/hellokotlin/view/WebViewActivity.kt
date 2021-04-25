package com.mjiayou.hellokotlin.view

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mjiayou.hellokotlin.R
import com.mjiayou.hellokotlin.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }

    lateinit var mBinding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview)

        val url = intent.getStringExtra(EXTRA_URL);
        Log.e("treason111", "WebViewActivity | onCreate | url = $url")

        mBinding.wvContainer.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.e("treason111", "WebViewActivity | onCreate | webChromeClient | onProgressChanged | newProgress = $newProgress")
            }
        }
        mBinding.wvContainer.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("treason111", "WebViewActivity | onCreate | webViewClient | onPageStarted | url = " + url.toString())
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("treason111", "WebViewActivity | onCreate | webViewClient | onPageFinished | url = " + url.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                Log.e("treason111", "WebViewActivity | onCreate | webViewClient | shouldOverrideUrlLoading")
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        mBinding.wvContainer.loadUrl(url.toString())
    }
}