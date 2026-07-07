package com.duivo.redbastion;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;

    private static final String COMPACT_CSS =
            "#topbar{height:40px!important;}" +
            ".tcell{padding:0 10px!important;gap:6px!important;font-size:11px!important;}" +
            ".tcell .lb{font-size:9px!important;letter-spacing:.18em!important;}" +
            ".tcell b{font-size:13px!important;}" +
            ".mini{width:90px!important;height:7px!important;}" +
            "#btn-wave{margin:4px!important;padding:0 16px!important;height:30px!important;font-size:11px!important;}" +
            ".icobtn{width:30px!important;height:30px!important;margin:4px 2px!important;font-size:12px!important;}" +
            "#shop{left:6px!important;bottom:6px!important;gap:5px!important;padding:6px!important;max-width:calc(100vw - 250px)!important;}" +
            "#shop .card{width:64px!important;min-width:64px!important;height:auto!important;padding:5px 4px 4px!important;gap:2px!important;}" +
            "#shop .card img{width:40px!important;height:40px!important;}" +
            "#shop .card .nm{font-size:7.5px!important;height:18px!important;}" +
            "#shop .card .cost{font-size:10px!important;}" +
            "#shop .card .pw,#shop .card .hk,#shop .card .lk{font-size:8px!important;}" +
            "#abilities{right:6px!important;bottom:6px!important;gap:6px!important;padding:6px!important;}" +
            "#abilities .ab{width:48px!important;height:48px!important;}" +
            "#abilities .ab .ic{font-size:18px!important;}" +
            "#abilities .ab .nm{font-size:7px!important;}" +
            "#panel{width:220px!important;top:48px!important;}" +
            "#hint{bottom:88px!important;}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView = new WebView(this);
        webView.setBackgroundColor(0xFF07090D);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String escapedCss = COMPACT_CSS
                        .replace("\\", "\\\\")
                        .replace("'", "\\'")
                        .replace("\n", "\\n");
                view.evaluateJavascript(
                        "(function(){var s=document.getElementById('android-compact-ui');" +
                        "if(!s){s=document.createElement('style');s.id='android-compact-ui';document.head.appendChild(s);}" +
                        "s.textContent='" + escapedCss + "';})();",
                        null);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/index.html");
        enterImmersiveMode();
    }

    private void enterImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
        enterImmersiveMode();
    }

    @Override
    protected void onPause() {
        if (webView != null) webView.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
