package app.revanced.bilibili.patches.okhttp.hooks

import android.net.Uri
import app.revanced.bilibili.api.BiliRoamingApi.getSeason
import app.revanced.bilibili.patches.okhttp.ApiHook
import app.revanced.bilibili.patches.okhttp.BangumiSeasonHook.FAIL_CODE
import app.revanced.bilibili.patches.okhttp.BangumiSeasonHook.isBangumiWithWatchPermission
import app.revanced.bilibili.patches.okhttp.BangumiSeasonHook.seasonAreasCache
import app.revanced.bilibili.settings.Settings
import app.revanced.bilibili.utils.Area
import app.revanced.bilibili.utils.Versions
import app.revanced.bilibili.utils.cachePrefs
import app.revanced.bilibili.utils.toJSONObject
import org.json.JSONObject

object Media : ApiHook() {
    override fun shouldHook(url: String, code: Int): Boolean {
        return Settings.UNLOCK_AREA_LIMIT.boolean
                && Versions.ge7_39_0()
                && url.contains("/pgc/view/v2/app/media")
                && code.isOk
    }

    override fun hook(url: String, code: Int, request: String, response: String): String {
        val mediaId = Uri.parse(url).getQueryParameter("media_id")
        if (JSONObject(response).optInt("code") != 0
            && (Area.th == seasonAreasCache[mediaId] || (cachePrefs.contains(mediaId)
                    && Area.th.value == cachePrefs.getString(mediaId, null)))
        ) {
            val (newCode, newResult) = getSeason(seasonId = mediaId?.toLong() ?: 0L)
                ?.toJSONObject()?.let {
                    it.optInt("code", FAIL_CODE) to it.optJSONObject("result")
                } ?: (FAIL_CODE to null)
            if (isBangumiWithWatchPermission(newResult, newCode)) {
                val data = JSONObject().apply {
                    put("actor", newResult.optJSONObject("actor"))
                    put("alias", newResult.optString("alias"))
                    put("areas", newResult.optJSONArray("areas"))
                    put("cover", newResult.optString("cover"))
                    put("evaluate", newResult.optString("evaluate"))
                    put("media_id", mediaId)
                    put("media_title", newResult.optString("title"))
                    put("origin_name", newResult.optString("origin_name"))
                    put("publish", newResult.optJSONObject("publish"))
                    put("season_id", mediaId)
                    put("staff", newResult.optJSONObject("staff"))
                    put("styles", newResult.optJSONArray("styles"))
                    put("type_name", newResult.optString("type_name"))
                }
                return JSONObject().apply {
                    put("code", 0)
                    put("data", data)
                }.toString()
            }
        }
        return response
    }
}
