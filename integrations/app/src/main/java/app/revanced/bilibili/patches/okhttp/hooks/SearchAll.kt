package app.revanced.bilibili.patches.okhttp.hooks

import app.revanced.bilibili.patches.okhttp.ApiHook
import app.revanced.bilibili.patches.okhttp.BangumiSeasonHook
import app.revanced.bilibili.settings.Settings
import org.json.JSONObject

/**
 * for hd
 */
object SearchAll : ApiHook() {
    override fun shouldHook(url: String, code: Int): Boolean {
        return code.isOk && (Settings.SEARCH_BANGUMI.boolean || Settings.SEARCH_MOVIE.boolean)
                && url.contains("/x/v2/search?")
    }

    override fun hook(url: String, code: Int, request: String, response: String): String {
        val jsonObject = JSONObject(response)
        if (jsonObject.optInt("code") == 0) {
            val data = jsonObject.optJSONObject("data")
                ?: return response
            BangumiSeasonHook.searchAllResponseHookForHd(data)
            return jsonObject.toString()
        }
        return response
    }
}
