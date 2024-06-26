package app.revanced.bilibili.patches.okhttp.hooks

import android.net.Uri
import app.revanced.bilibili.patches.okhttp.ApiHook
import app.revanced.bilibili.patches.okhttp.BangumiSeasonHook

/**
 * for hd
 */
object SearchByType : ApiHook() {
    override fun shouldHook(url: String, code: Int): Boolean {
        return code.isOk && url.contains("/x/v2/search/type") && url.run {
            Uri.parse(this).getQueryParameter("type")?.let {
                BangumiSeasonHook.extraSearchHandleable(it.toInt())
            } ?: false
        }
    }

    override fun hook(url: String, code: Int, request: String, response: String): String {
        return BangumiSeasonHook.handleExtraSearchForHd(url)
    }
}
