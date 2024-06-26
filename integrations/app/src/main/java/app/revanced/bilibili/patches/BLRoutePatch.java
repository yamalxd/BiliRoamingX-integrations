package app.revanced.bilibili.patches;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Keep;

import java.util.List;
import java.util.regex.Pattern;

import app.revanced.bilibili.settings.Settings;
import app.revanced.bilibili.utils.Logger;

public class BLRoutePatch {
    private static final String STORY_ROUTER_QUERY = "&-Arouter=story";
    private static final String STORY_TYPE_QUERY = "&-Atype=story";
    private static final Pattern playerPreloadRegex = Pattern.compile("&player_preload=[^&]*");

    @Keep
    public static Uri intercept(Uri uri) {
        if (uri == null) return null;
        Logger.debug(() -> "Route uri: " + uri);
        String scheme = uri.getScheme();
        String url = uri.toString();
        if ("bilibili".equals(scheme)) {
            String authority = uri.getEncodedAuthority();
            if ("story".equals(authority) || "video".equals(authority)) {
                Uri.Builder newUri = uri.buildUpon();
                if ("story".equals(authority) && Settings.REPLACE_STORY_VIDEO.getBoolean())
                    newUri.authority("video");
                String newQuery = uri.getEncodedQuery();
                if (!TextUtils.isEmpty(newQuery)) {
                    if (Settings.REPLACE_STORY_VIDEO.getBoolean())
                        newQuery = newQuery.replace(STORY_ROUTER_QUERY, "").replace(STORY_TYPE_QUERY, "");
                    boolean needRemovePayload = VideoQualityPatch.halfScreenQuality() != 0 || VideoQualityPatch.getMatchedFullScreenQuality() != 0 || Settings.DEFAULT_PLAYBACK_SPEED.getFloat() != 0f;
                    if (needRemovePayload)
                        newQuery = playerPreloadRegex.matcher(newQuery).replaceAll("");
                }
                newUri.encodedQuery(newQuery);
                return newUri.build();
            } else if ("1".equals(Settings.PLAYER_VERSION.getString()) && url.startsWith("bilibili://music/playlist/playpage") && !url.equals("bilibili://music/playlist/playpage/0")) {
                return uri.buildUpon().appendQueryParameter("force_old_playlist", "1").build();
            } else if (Settings.ADD_CHANNEL.getBoolean() && url.startsWith("bilibili://pegasus/channel/v2")) {
                // for hd, consistent with the default behavior of selecting "select" tab before deleting the "topic" tab
                if (TextUtils.isEmpty(uri.getQueryParameter("tab")))
                    return uri.buildUpon().appendQueryParameter("tab", "select").build();
            }
        } else if ("https".equals(scheme)) {
            if (url.startsWith("https://www.bilibili.com/bangumi/play")) {
                boolean needRemovePayload = VideoQualityPatch.halfScreenQuality() != 0 || VideoQualityPatch.getMatchedFullScreenQuality() != 0 || Settings.DEFAULT_PLAYBACK_SPEED.getFloat() != 0f;
                if (needRemovePayload)
                    return Uri.parse(playerPreloadRegex.matcher(url).replaceAll(""));
            } else if (Settings.REPLACE_STORY_VIDEO.getBoolean() && url.startsWith("https://www.bilibili.com/video")) {
                return Uri.parse(url.replace(STORY_ROUTER_QUERY, "").replace(STORY_TYPE_QUERY, ""));
            } else if (Settings.DEFAULT_MAX_QN.getBoolean() && url.startsWith("https://live.bilibili.com")) {
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments.size() == 1 && TextUtils.isDigitsOnly(pathSegments.get(0))) {
                    Uri.Builder builder = uri.buildUpon().clearQuery();
                    for (String name : uri.getQueryParameterNames()) {
                        if (!"current_qn".equals(name) && !"current_quality".equals(name)
                                && !"playurl_h264".equals(name) && !"playurl_h265".equals(name)) {
                            String value = uri.getQueryParameter(name);
                            builder.appendQueryParameter(name, value);
                        }
                    }
                    return builder.build();
                }
            }
        }
        return uri;
    }
}
