package io.github.henr1klmb.patches.shared

object WebViewHelper {
    private const val DEFAULT_AD_BLOCKER_JS =
        "javascript:(function(){" +
                "window.open=function(){return null;};" +
                "var s=document.getElementById('morphe-adblock')||document.createElement('style');" +
                "s.id='morphe-adblock';" +
                "s.textContent='ins.adsbygoogle,[id*=google_ads],[class*=ad-banner],[class*=ad_banner],[id*=ad-container],[id*=popunder],iframe[src*=ads],iframe[src*=doubleclick],iframe[src*=adsterra],iframe[src*=propeller]{display:none!important;visibility:hidden!important;height:0!important;pointer-events:none!important;}';" +
                "(document.head||document.documentElement).appendChild(s);" +
                "})();"

    private const val DEFAULT_TRACKER_BLOCKER_JS =
        "javascript:(function(){" +
                "window.ga=function(){};" +
                "window.gtag=function(){};" +
                "window.fbq=function(){};" +
                "window._paq=[];" +
                "window.mixpanel={track:function(){},identify:function(){}};" +
                "window.amplitude={getInstance:function(){return{logEvent:function(){},setUserId:function(){}};}};" +
                "})();"

    @JvmStatic
    fun injectAdBlock(webView: Any?) {
        injectJs(webView, DEFAULT_AD_BLOCKER_JS)
    }

    @JvmStatic
    fun injectTrackerBlock(webView: Any?) {
        injectJs(webView, DEFAULT_TRACKER_BLOCKER_JS)
    }

    /**
     * Erlaubt es, für spezielle Apps eigene CSS-Selektoren auszublenden,
     * ohne den Helper neu schreiben zu müssen.
     */
    @JvmStatic
    fun injectCustomCss(webView: Any?, cssSelector: String) {
        val script =
            "javascript:(function(){" +
                    "var s=document.getElementById('morphe-custom-css')||document.createElement('style');" +
                    "s.id='morphe-custom-css';" +
                    "s.textContent='$cssSelector{display:none!important;visibility:hidden!important;height:0!important;pointer-events:none!important;}';" +
                    "(document.head||document.documentElement).appendChild(s);" +
                    "})();"
        injectJs(webView, script)
    }

    @JvmStatic
    fun injectJs(webView: Any?, script: String) {
        if (webView == null) return
        runCatching {
            val clazz = webView.javaClass
            val postMethod = clazz.getMethod("post", Runnable::class.java)
            val loadUrlMethod = clazz.getMethod("loadUrl", String::class.java)
            postMethod.invoke(webView, Runnable {
                runCatching { loadUrlMethod.invoke(webView, script) }
            })
        }
    }
}