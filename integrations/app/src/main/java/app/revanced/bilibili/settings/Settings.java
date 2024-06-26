package app.revanced.bilibili.settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static app.revanced.bilibili.settings.Settings.ValueType.BOOLEAN;
import static app.revanced.bilibili.settings.Settings.ValueType.FLOAT;
import static app.revanced.bilibili.settings.Settings.ValueType.INTEGER;
import static app.revanced.bilibili.settings.Settings.ValueType.LONG;
import static app.revanced.bilibili.settings.Settings.ValueType.STRING;
import static app.revanced.bilibili.settings.Settings.ValueType.STRING_SET;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import app.revanced.bilibili.utils.Constants;
import app.revanced.bilibili.utils.Logger;
import app.revanced.bilibili.utils.PreferenceUpdater;
import app.revanced.bilibili.utils.SettingsSyncHelper;
import app.revanced.bilibili.utils.Utils;
import kotlin.Pair;


public enum Settings {
    DEBUG("debug", BOOLEAN, FALSE, true),
    SHOW_HINT("show_hint", BOOLEAN, FALSE),

    // 解锁番剧限制
    UNLOCK_AREA_LIMIT("main_func", BOOLEAN, FALSE),
    ALLOW_DOWNLOAD("allow_download", BOOLEAN, FALSE),
    ALLOW_MINI_PLAY("allow_mini_play", BOOLEAN, FALSE),
    TW_SERVER("tw_server", STRING, ""),
    HK_SERVER("hk_server", STRING, ""),
    CN_SERVER("cn_server", STRING, ""),
    TH_SERVER("th_server", STRING, ""),
    UPOS_HOST("upos_host", STRING, ""),
    SAVE_TH_HISTORY("save_th_history", BOOLEAN, TRUE),

    // 播放器
    HALF_SCREEN_QUALITY("half_screen_quality", STRING, "0"),
    FULL_SCREEN_QUALITY("full_screen_quality", STRING, "0"),
    MOBILE_FULL_SCREEN_QUALITY("full_screen_quality_mobile", STRING, "0"),
    PLAYER_VERSION("player_version", STRING, "0", true),
    REMEMBER_LOSSLESS_SETTING("remember_lossless_setting", BOOLEAN, FALSE),
    DEFAULT_PLAYBACK_SPEED("default_playback_speed", FLOAT, 0f),
    LONG_PRESS_PLAYBACK_SPEED("long_press_playback_speed", FLOAT, 0f),
    OVERRIDE_PLAYBACK_SPEED("playback_speed_override", STRING, ""),
    TRIAL_VIP_QUALITY("trial_vip_quality", BOOLEAN, FALSE),
    DISABLE_SEGMENTED_SECTION("disable_segmented_section", BOOLEAN, FALSE),
    DISABLE_AUTO_NEXT_PLAY("disable_auto_next_play", BOOLEAN, FALSE),
    DISABLE_PLAYER_LONG_PRESS("disable_player_long_press", BOOLEAN, FALSE),
    SCALE_TO_SWITCH_RATIO("scale_to_switch_ratio", BOOLEAN, FALSE),

    // 首页
    SHOWING_BOTTOM_ITEMS("showing_bottom_items", STRING_SET, Set.of(Constants.ALL_VALUE)),
    HIDED_HOME_TAB("customize_home_tab", STRING_SET, Collections.EMPTY_SET),
    FILTER_HOME_RECOMMEND("customize_home_recommend", STRING_SET, Collections.EMPTY_SET),
    HOME_FILTER_APPLY_TO_VIDEO("home_filter_apply_to_relate", BOOLEAN, FALSE),
    HOME_FILTER_APPLY_TO_POPULAR("home_filter_apply_to_popular", BOOLEAN, FALSE),
    LOW_PLAY_COUNT_LIMIT("hide_low_play_count_recommend_limit", LONG, 0L),
    SHORT_DURATION_LIMIT("hide_short_duration_recommend_limit", INTEGER, 0),
    LONG_DURATION_LIMIT("hide_long_duration_recommend_limit", INTEGER, 0),
    HOME_RCMD_FILTER_TITLE("home_filter_keywords_title", STRING_SET, Collections.EMPTY_SET),
    HOME_RCMD_FILTER_TITLE_REGEX_MODE("home_filter_title_regex_mode", BOOLEAN, FALSE),
    HOME_RCMD_FILTER_REASON("home_filter_keywords_reason", STRING_SET, Collections.EMPTY_SET),
    HOME_RCMD_FILTER_REASON_REGEX_MODE("home_filter_reason_regex_mode", BOOLEAN, FALSE),
    HOME_RCMD_FILTER_UID("home_filter_keywords_uid", STRING_SET, Collections.EMPTY_SET),
    HOME_RCMD_FILTER_UP("home_filter_keywords_up", STRING_SET, Collections.EMPTY_SET),
    HOME_RCMD_FILTER_UP_REGEX_MODE("home_filter_up_regex_mode", BOOLEAN, FALSE),
    HOME_RCMD_FILTER_CATEGORY("home_filter_keywords_category", STRING_SET, Collections.EMPTY_SET),
    HOME_RCMD_FILTER_CHANNEL("home_filter_keywords_channel", STRING_SET, Collections.EMPTY_SET),
    HOME_DISABLE_AUTO_REFRESH("disable_auto_refresh", BOOLEAN, FALSE),
    ADD_BANGUMI("add_bangumi", BOOLEAN, FALSE, true),
    ADD_MOVIE("add_movie", BOOLEAN, FALSE, true),
    ADD_KOREA("add_korea", BOOLEAN, FALSE, true),
    PURIFY_GAME("purify_game", BOOLEAN, FALSE, true),
    DRAWER("drawer", BOOLEAN, FALSE),
    DISABLE_MAIN_PAGE_STORY("disable_main_page_story", BOOLEAN, FALSE, true),
    BLOCK_TOP_ACTIVITY("block_top_activity", BOOLEAN, FALSE, true),
    BLOCK_RECOMMEND_GUIDANCE("block_recommend_guidance", BOOLEAN, FALSE, true),
    BLOCK_POPULAR_TOP_ENTRANCE("block_popular_top_entrance", BOOLEAN, FALSE),
    BLOCK_POPULAR_TOPIC_LIST("block_popular_topic_list", BOOLEAN, FALSE),
    BLOCK_POPULAR_RCMD_UP("block_popular_rcmd_up", BOOLEAN, FALSE),

    // 动态页
    DYNAMIC_PREFER_VIDEO_TAB("prefer_video_tab", BOOLEAN, FALSE),
    DYNAMIC_PURIFY_CITY("purify_city", BOOLEAN, FALSE),
    DYNAMIC_PURIFY_CAMPUS("purify_campus", BOOLEAN, FALSE),
    DYNAMIC_RM_TOPIC_OF_ALL("customize_dynamic_all_rm_topic", BOOLEAN, FALSE),
    DYNAMIC_RM_UP_OF_ALL("customize_dynamic_all_rm_up", BOOLEAN, FALSE),
    DYNAMIC_RM_UP_OF_VIDEO("customize_dynamic_video_rm_up", BOOLEAN, FALSE),
    DYNAMIC_FILTER_APPLY_TO_VIDEO("filter_apply_to_video", BOOLEAN, FALSE),
    DYNAMIC_RM_BLOCKED("customize_dynamic_rm_blocked", BOOLEAN, FALSE),
    DYNAMIC_RM_AD_LINK("customize_dynamic_rm_ad_link", BOOLEAN, FALSE),
    DYNAMIC_RM_UP_RESERVATION("customize_dynamic_rm_up_reservation", BOOLEAN, FALSE),
    DYNAMIC_PURIFY_TYPE("customize_dynamic_type", STRING_SET, Collections.EMPTY_SET),
    DYNAMIC_PURIFY_CONTENT("customize_dynamic_keyword_content", STRING_SET, Collections.EMPTY_SET),
    DYNAMIC_PURIFY_CONTENT_REGEX_MODE("dynamic_content_regex_mode", BOOLEAN, FALSE),
    DYNAMIC_PURIFY_UP("customize_dynamic_keyword_upname", STRING_SET, Collections.EMPTY_SET),
    DYNAMIC_PURIFY_UID("customize_dynamic_keyword_uid", STRING_SET, Collections.EMPTY_SET),
    DYNAMIC_PURIFY_TOPIC("customize_dynamic_keyword_topic", STRING_SET, Collections.EMPTY_SET),
    DYNAMIC_FORCE_OLD_TAB("dynamic_force_old_tab", BOOLEAN, FALSE, true),

    // 我的页
    DRAWER_STYLE("drawer_style_value", STRING, "0"),
    PURIFY_DRAWER_RED_DOT("purify_drawer_reddot", BOOLEAN, FALSE),
    REMOVE_VIP_SECTION("remove_vip_section", BOOLEAN, FALSE),
    SHOWING_DRAWER_ITEMS("showing_drawer_items", STRING_SET, Set.of(Constants.ALL_VALUE)),
    SWITCH_DARK_DIALOG("switch_dark_dialog", BOOLEAN, FALSE),
    ADD_CHANNEL("add_channel", BOOLEAN, FALSE, true),
    ADD_PODCAST("add_podcast", BOOLEAN, FALSE),

    // 直播间
    FORBID_SWITCH_LIVE_ROOM("forbid_switch_live_room", BOOLEAN, FALSE),
    DISABLE_LIVE_ROOM_DOUBLE_CLICK("disable_live_room_double_click", BOOLEAN, FALSE),
    PURIFY_LIVE_POPUPS("purify_live_popups", STRING_SET, Collections.EMPTY_SET),
    REMOVE_LIVE_MASK("remove_live_mask", BOOLEAN, FALSE),
    DEFAULT_MAX_QN("default_max_qn", BOOLEAN, FALSE),
    DISABLE_SLIDE_LEFT("disable_slide_left", BOOLEAN, FALSE),
    DISABLE_AUTO_FLOAT("disable_auto_float", BOOLEAN, FALSE),

    // 视频详情页
    AUTO_LIKE("auto_like", BOOLEAN, FALSE),
    SAVE_COMMENT_IMAGE("save_comment_image", BOOLEAN, FALSE),
    UNLOCK_PLAY_LIMIT("play_arc_conf", BOOLEAN, FALSE),
    REPLACE_STORY_VIDEO("replace_story_video", BOOLEAN, FALSE),
    DISABLE_STORY_FULL("disable_story_full", BOOLEAN, FALSE),
    REMOVE_CMD_DMS("remove_video_cmd_dms", BOOLEAN, FALSE),
    BLOCK_WORD_SEARCH("block_word_search", BOOLEAN, FALSE),
    BLOCK_COMMENT_GUIDE("block_comment_guide", BOOLEAN, FALSE),
    BLOCK_VIDEO_COMMENT("block_video_comment", BOOLEAN, FALSE),
    BLOCK_UP_RCMD_ADS("block_up_rcmd_ads", BOOLEAN, FALSE),
    BLOCK_BANGUMI_PAGE_ADS("block_bangumi_page_ads", BOOLEAN, FALSE),
    REMOVE_RELATE_PROMOTE("remove_video_relate_promote", BOOLEAN, FALSE),
    REMOVE_RELATE_ONLY_AV("remove_video_relate_only_av", BOOLEAN, FALSE),
    REMOVE_RELATE_NOTHING("remove_video_relate_nothing", BOOLEAN, FALSE),
    DISABLE_AUTO_SELECT("disable_auto_select", BOOLEAN, FALSE),
    DISABLE_AUTO_SUBSCRIBE("disable_auto_subscribe", BOOLEAN, FALSE),
    FILTER_STORY("filter_story", STRING_SET, Collections.EMPTY_SET),
    BLOCK_DM_FEEDBACK("block_dm_feedback", BOOLEAN, FALSE),
    BLOCK_FAN_GUIDE("block_fan_guide", BOOLEAN, FALSE),
    REMOVE_ELEC_BUTTON("remove_elec_button", BOOLEAN, FALSE),
    BLOCK_LIVE_ORDER("block_live_order", BOOLEAN, FALSE),
    BLOCK_ACTIVITY_TAB("block_activity_tab", BOOLEAN, FALSE),
    OLD_FAV("old_fav", BOOLEAN, FALSE, true),
    BLOCK_ONLY_AT_COMMENT("block_comment_only_at", BOOLEAN, FALSE),
    BLOCK_COMMENT_GOODS("block_comment_goods", BOOLEAN, FALSE),
    BLOCK_COMMENT_UID("block_comment_uid", STRING_SET, Collections.EMPTY_SET),
    BLOCK_COMMENT_UP("block_comment_up", STRING_SET, Collections.EMPTY_SET),
    BLOCK_COMMENT_UP_REGEX_MODE("block_comment_up_regex_mode", BOOLEAN, FALSE),
    BLOCK_COMMENT_CONTENT("block_comment_content", STRING_SET, Collections.EMPTY_SET),
    BLOCK_COMMENT_CONTENT_REGEX_MODE("block_comment_content_regex_mode", BOOLEAN, FALSE),
    BLOCK_COMMENT_UP_LEVEL("block_comment_up_level", INTEGER, 0),
    EXTERNAL_DOWNLOADER("external_downloader", BOOLEAN, FALSE),
    EXTERNAL_DOWNLOADER_NAME("external_downloader_name", STRING, "com.junkfood.seal"),
    NO_COLORFUL_DANMAKU("no_colorful_danmaku", BOOLEAN, FALSE),

    // 用户空间页
    FIX_SPACE("fix_space", BOOLEAN, TRUE),
    CUSTOMIZE_SPACE("customize_space", STRING_SET, Collections.EMPTY_SET),
    ADD_ARTICLE_TAB("add_article_tab", BOOLEAN, FALSE),

    // 搜索页
    PURIFY_SEARCH("purify_search", BOOLEAN, FALSE),
    FILTER_SEARCH_TYPE("filter_search_type", STRING_SET, Collections.EMPTY_SET),
    SEARCH_BANGUMI("search_area_bangumi", BOOLEAN, FALSE),
    SEARCH_MOVIE("search_area_movie", BOOLEAN, FALSE),

    // 字幕
    SUBTITLE_AUTO_GENERATE("auto_generate_subtitle", BOOLEAN, FALSE),
    SUBTITLE_STYLE_SWITCH("custom_subtitle", BOOLEAN, FALSE),
    SUBTITLE_REMOVE_BG("subtitle_remove_bg", BOOLEAN, TRUE),
    SUBTITLE_BOLD_TEXT("subtitle_bold", BOOLEAN, TRUE),
    SUBTITLE_FONT_SIZE_PORTRAIT("subtitle_font_size_portrait", INTEGER, 0),
    SUBTITLE_FONT_SIZE_LANDSCAPE("subtitle_font_size_landscape", INTEGER, 0),
    SUBTITLE_FILL_COLOR("subtitle_font_color2", STRING, "FFFFFFFF"),
    SUBTITLE_STROKE_COLOR("subtitle_stroke_color", STRING, "FF000000"),
    SUBTITLE_STROKE_WIDTH("subtitle_stroke_width", FLOAT, 5.0f),
    SUBTITLE_OFFSET("subtitle_offset", INTEGER, 0),
    SUBTITLE_TRANSLATE_SERVER("subtitle_translate_server", STRING, "microsoft"),
    SUBTITLE_IMPORT_SAVE("subtitle_import_save", BOOLEAN, FALSE),

    // 杂项
    TEENAGER_MODE_DIALOG("teenagers_mode_dialog", BOOLEAN, FALSE),
    COMMENT_COPY("comment_copy", BOOLEAN, FALSE),
    COMMENT_COPY_ENHANCE("comment_copy_enhance", BOOLEAN, FALSE),
    BLOCK_UPDATE("block_update", BOOLEAN, FALSE),
    CUSTOM_UPDATE("custom_update", BOOLEAN, TRUE),
    BLOCK_FOLLOW_BUTTON("block_follow_button", STRING_SET, Collections.EMPTY_SET),
    CUSTOM_THEME("custom_theme", BOOLEAN, FALSE),
    SKIN("skin", BOOLEAN, FALSE),
    TEXT_FOLD_COMMENT_MAX_LINES("text_fold_comment_max_lines", INTEGER, Constants.DEF_COMMENT_MAX_LINES),
    TEXT_FOLD_DYN_MAX_LINES("text_fold_dyn_max_lines", INTEGER, Constants.DEF_DYN_MAX_LINES),
    TEXT_FOLD_DYN_LINES_TO_ALL("text_fold_dyn_lines_to_all", INTEGER, Constants.DEF_DYN_LINES_TO_ALL),
    BLOCK_MODULES("block_modules", BOOLEAN, FALSE),
    BLOCK_MODULES_EXCEPTION("block_modules_exception", STRING_SET, Collections.EMPTY_SET),
    MUSIC_NOTIFICATION("music_notification", BOOLEAN, FALSE, true),
    PURIFY_SHARE("purify_share", BOOLEAN, FALSE),
    FUCK_MINI_PROGRAM("mini_program", BOOLEAN, FALSE),
    NUMBER_FORMAT("number_format", BOOLEAN, FALSE),
    AUTO_RECEIVE_COUPON("auto_receive_coupon", BOOLEAN, FALSE),
    DISPLAY_SIZE("display_size", STRING, "0", true),
    CUSTOM_SPLASH("custom_splash", BOOLEAN, FALSE),
    CUSTOM_SPLASH_LOGO("custom_splash_logo", BOOLEAN, FALSE),
    FULL_SPLASH("full_splash", BOOLEAN, FALSE),
    FORCE_HW_CODEC("force_hw_codec", BOOLEAN, FALSE),
    ENABLE_AV("enable_av", BOOLEAN, FALSE, true),
    ENABLE_DOC_PROVIDER("enable_doc_provider", BOOLEAN, FALSE),

    // 去广告杂项
    PURIFY_SPLASH("purify_splash", BOOLEAN, FALSE),

    // 非配置项
    LOSSLESS_ENABLED("lossless_enabled", BOOLEAN, FALSE),
    BG_PLAYING_ENABLED("bg_playing_enabled", BOOLEAN, FALSE),
    CUSTOM_COLOR("biliroaming_custom_color", INTEGER, -0xe6b7d),
    SKIN_JSON("skin_json", STRING, "");

    public static final String PREFS_NAME = "biliroaming";

    public static SharedPreferences prefs;

    private static final ArrayList<WeakReference<SharedPreferences.OnSharedPreferenceChangeListener>> preferenceChangeListeners = new ArrayList<>();

    private static final SharedPreferences.OnSharedPreferenceChangeListener innerListener = Settings::onPreferenceChanged;

    @NonNull
    public final String key;
    @NonNull
    public final ValueType valueType;
    @NonNull
    public final Object defValue;
    /**
     * If the app should be rebooted, if this setting is changed
     */
    public final boolean needReboot;

    private Object value;

    Settings(String key, ValueType valueType, Object defValue) {
        this(key, valueType, defValue, false);
    }

    Settings(@NonNull String key, @NonNull ValueType valueType, @NonNull Object defValue, boolean needReboot) {
        this.key = key;
        this.valueType = valueType;
        this.defValue = defValue;
        this.needReboot = needReboot;
    }

    static {
        reload();
    }

    public static void reload() {
        prefs = Utils.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadAllSettings();
        prefs.registerOnSharedPreferenceChangeListener(innerListener);
    }

    private static void loadAllSettings() {
        try {
            for (Settings setting : values())
                setting.load(prefs);
        } catch (Exception ex) {
            Logger.error(ex, () -> "Failed to load settings");
        }
    }

    @SuppressWarnings("unchecked")
    private void load(SharedPreferences prefs) {
        switch (valueType) {
            case BOOLEAN -> setValue(prefs.getBoolean(key, (Boolean) defValue));
            case INTEGER -> setValue(prefs.getInt(key, (Integer) defValue));
            case LONG -> setValue(prefs.getLong(key, (Long) defValue));
            case FLOAT -> setValue(prefs.getFloat(key, (Float) defValue));
            case STRING -> setValue(prefs.getString(key, (String) defValue));
            case STRING_SET -> setValue(prefs.getStringSet(key, (Set<String>) defValue));
            default -> throw new IllegalStateException(name());
        }
        Logger.debug(() -> String.format("Loaded setting '%s' with value %s", name(), value));
    }

    /**
     * Sets, but does _not_ persistently save the value.
     * <p>
     * This intentionally is a static method, to deter accidental usage
     * when {@link #saveValue(Object)} was intended.
     */
    public static void setValue(Settings setting, Object newValue) {
        setting.setValue(newValue);
    }

    private void setValue(Object newValue) {
        switch (valueType) {
            case FLOAT, LONG, INTEGER, BOOLEAN, STRING, STRING_SET -> value = newValue;
            default -> throw new IllegalStateException(name());
        }
    }

    @SuppressWarnings("unchecked")
    public void saveValue(Object newValue) {
        if (Utils.isMainProcess()) {
            setValue(newValue);
            SharedPreferences.Editor editor = prefs.edit();
            switch (valueType) {
                case BOOLEAN -> editor.putBoolean(key, (Boolean) newValue);
                case INTEGER -> editor.putInt(key, (Integer) newValue);
                case LONG -> editor.putLong(key, (Long) newValue);
                case FLOAT -> editor.putFloat(key, (Float) newValue);
                case STRING -> editor.putString(key, (String) newValue);
                case STRING_SET -> editor.putStringSet(key, (Set<String>) newValue);
                default -> throw new IllegalStateException(name());
            }
            editor.apply();
        } else {
            PreferenceUpdater.update(new Pair<>(key, newValue));
        }
    }

    public void appendValue(String value) {
        var stringSet = getStringSet();
        var newSet = new HashSet<>(stringSet);
        newSet.add(value);
        saveValue(newSet);
    }

    public boolean getBoolean() {
        return (Boolean) value;
    }

    public int getInt() {
        return (Integer) value;
    }

    public long getLong() {
        return (Long) value;
    }

    public float getFloat() {
        return (Float) value;
    }

    public String getString() {
        return (String) value;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getStringSet() {
        return (Set<String>) value;
    }

    @NonNull
    public Object getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    static void onPreferenceChanged(SharedPreferences preferences, String key) {
        Logger.debug(() -> "onPreferenceChanged, key: " + key);
        for (Settings settings : values()) {
            if (settings.key.equals(key)) {
                switch (settings.valueType) {
                    case BOOLEAN:
                        boolean newBool = preferences.getBoolean(key, (Boolean) settings.defValue);
                        Settings.setValue(settings, newBool);
                        SettingsSyncHelper.sync(new Pair<>(key, newBool));
                        break;
                    case INTEGER:
                        int newInt = preferences.getInt(key, (Integer) settings.defValue);
                        Settings.setValue(settings, newInt);
                        SettingsSyncHelper.sync(new Pair<>(key, newInt));
                        break;
                    case LONG:
                        long newLong = preferences.getLong(key, (Long) settings.defValue);
                        Settings.setValue(settings, newLong);
                        SettingsSyncHelper.sync(new Pair<>(key, newLong));
                        break;
                    case FLOAT:
                        float newFloat = preferences.getFloat(key, (Float) settings.defValue);
                        Settings.setValue(settings, newFloat);
                        SettingsSyncHelper.sync(new Pair<>(key, newFloat));
                        break;
                    case STRING:
                        String newStr = preferences.getString(key, (String) settings.defValue);
                        Settings.setValue(settings, newStr);
                        SettingsSyncHelper.sync(new Pair<>(key, newStr));
                        break;
                    case STRING_SET:
                        Set<String> newSet = preferences.getStringSet(key, (Set<String>) settings.defValue);
                        Settings.setValue(settings, newSet);
                        SettingsSyncHelper.sync(new Pair<>(key, newSet));
                        break;
                    default:
                        throw new IllegalStateException(settings.name());
                }
                break;
            }
        }
        for (var ref : preferenceChangeListeners) {
            var listener = ref.get();
            if (listener != null)
                listener.onSharedPreferenceChanged(preferences, key);
        }
    }

    public static void notifySettingsChangedForViceProcess(String key, Object value) {
        for (Settings settings : Settings.values()) {
            if (settings.key.equals(key)) {
                Settings.setValue(settings, value);
                for (var ref : preferenceChangeListeners) {
                    var listener = ref.get();
                    if (listener != null)
                        listener.onSharedPreferenceChanged(prefs, key);
                }
                break;
            }
        }
    }

    public static void registerPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferenceChangeListeners.add(new WeakReference<>(listener));
    }

    public static void unregisterPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferenceChangeListeners.removeIf(ref -> ref.get() == listener);
    }

    public enum ValueType {
        BOOLEAN,
        INTEGER,
        LONG,
        FLOAT,
        STRING,
        STRING_SET,
    }
}
