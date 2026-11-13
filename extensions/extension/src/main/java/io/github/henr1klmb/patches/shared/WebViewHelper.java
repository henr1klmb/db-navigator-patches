package io.github.henr1klmb.patches.shared;

import java.lang.reflect.Method;

public class WebViewHelper {
    private static final String DEFAULT_AD_BLOCKER_JS =
        "javascript:(function(){" +
        "window.open=function(){return null;};" +
        "var s=document.getElementById('morphe-adblock')||document.createElement('style');" +
        "s.id='morphe-adblock';" +
        "s.textContent='ins.adsbygoogle,[id*=google_ads],[class*=ad-banner],[class*=ad_banner],[id*=ad-container],[id*=popunder],iframe[src*=ads],iframe[src*=doubleclick],iframe[src*=adsterra],iframe[src*=propeller]{display:none!important;visibility:hidden!important;height:0!important;pointer-events:none!important;}';" +
        "(document.head||document.documentElement).appendChild(s);" +
        "})();";

    private static final String DEFAULT_TRACKER_BLOCKER_JS =
        "javascript:(function(){" +
        "window.ga=function(){};" +
        "window.gtag=function(){};" +
        "window.fbq=function(){};" +
        "window._paq=[];" +
        "window.mixpanel={track:function(){},identify:function(){}};" +
        "window.amplitude={getInstance:function(){return{logEvent:function(){},setUserId:function(){}};}};" +
        "})();";

    public static void injectAdBlock(Object webView) {
        injectJs(webView, DEFAULT_AD_BLOCKER_JS);
    }

    public static void injectTrackerBlock(Object webView) {
        injectJs(webView, DEFAULT_TRACKER_BLOCKER_JS);
    }

    public static void injectCustomCss(Object webView, String cssSelector) {
        String script =
            "javascript:(function(){" +
            "var s=document.getElementById('morphe-custom-css')||document.createElement('style');" +
            "s.id='morphe-custom-css';" +
            "s.textContent='" + cssSelector + "{display:none!important;visibility:hidden!important;height:0!important;pointer-events:none!important;}';" +
            "(document.head||document.documentElement).appendChild(s);" +
            "})();";
        injectJs(webView, script);
    }

    public static void injectJs(Object webView, String script) {
        if (webView == null) return;
        try {
            Class<?> clazz = webView.getClass();
            Method postMethod = clazz.getMethod("post", Runnable.class);
            final Method loadUrlMethod = clazz.getMethod("loadUrl", String.class);
            final Object target = webView;
            final String js = script;
            postMethod.invoke(webView, new Runnable() {
                @Override
                public void run() {
                    try {
                        loadUrlMethod.invoke(target, js);
                    } catch (Throwable ignored) {
                    }
                }
            });
        } catch (Throwable ignored) {
        }
    }
}
