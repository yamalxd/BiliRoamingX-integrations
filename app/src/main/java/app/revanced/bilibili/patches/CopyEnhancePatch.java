package app.revanced.bilibili.patches;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bilibili.bplus.followingcard.widget.EllipsizingTextView;
import com.bilibili.bplus.im.business.model.BaseTypedMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.function.Consumer;

import app.revanced.bilibili.patches.main.ApplicationDelegate;
import app.revanced.bilibili.settings.Settings;
import app.revanced.bilibili.utils.Jsons;
import app.revanced.bilibili.utils.KtUtils;
import app.revanced.bilibili.utils.Reflex;
import app.revanced.bilibili.utils.Toasts;
import app.revanced.bilibili.utils.Utils;
import app.revanced.bilibili.widget.OnLongClickOriginListener;

public class CopyEnhancePatch {

    private static final String[] DYNAMIC_COPYABLE_IDS = new String[]{
            "dy_card_text",
            "dy_opus_paragraph_desc",
            "dy_opus_paragraph_title",
            "dy_opus_copy_right_id",
            "dy_opus_paragraph_text"
    };

    public static boolean onCopyDesc(boolean flag, String desc) {
        if (!Settings.COMMENT_COPY.getBoolean())
            return false;
        if (!Settings.COMMENT_COPY_ENHANCE.getBoolean())
            return true;
        showCopyDialog(ApplicationDelegate.requireTopActivity(), desc, (v) -> {
            KtUtils.setClipboardContent("text", v);
            if (flag) {
                Toasts.showShortWithId("video_detail_decs_copy_avid");
            } else {
                Toasts.showShortWithId("video_detail_decs_copy_decs");
            }
        });
        return true;
    }

    public static boolean onDynamicLongClick(OnLongClickOriginListener listener, View view) {
        if (!Settings.COMMENT_COPY.getBoolean())
            return false;
        if (!Settings.COMMENT_COPY_ENHANCE.getBoolean())
            return true;
        for (String id : DYNAMIC_COPYABLE_IDS) {
            int resId = Utils.getResId(id, "id");
            if (resId == 0) continue;
            TextView textView = view.findViewById(resId);
            if (textView == null) continue;
            CharSequence text;
            if (textView instanceof EllipsizingTextView) {
                text = (CharSequence) Reflex.getFirstFieldByExactType(textView, CharSequence.class);
            } else {
                text = textView.getText();
            }
            showCopyDialog(textView.getContext(), text, (v) -> listener.onLongClick_Origin(view));
            return true;
        }
        return false;
    }

    public static boolean onCommentLongClick(OnLongClickOriginListener listener, View view, String idName) {
        if (!Settings.COMMENT_COPY.getBoolean())
            return false;
        if (!Settings.COMMENT_COPY_ENHANCE.getBoolean())
            return true;
        int resId = Utils.getResId(idName, "id");
        View textView = view.findViewById(resId);
        if (!(textView instanceof TextView)) return false;
        CharSequence text = (CharSequence) Reflex.getFirstFieldByExactTypeOrNull(textView, CharSequence.class);
        if (text == null) return false;
        showCopyDialog(textView.getContext(), text, (v) -> listener.onLongClick_Origin(view));
        return true;
    }

    public static boolean onConversationCopy(Activity activity, BaseTypedMessage<?> message, PopupWindow popupWindow) {
        if (!Settings.COMMENT_COPY_ENHANCE.getBoolean())
            return false;
        JSONObject jo = Jsons.toJSONObject(message.getContentString());
        String text = jo.optString("content");
        if (text.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(jo.optString("title").trim()).append('\n');
            sb.append(jo.optString("text").trim()).append('\n');
            JSONArray modules = jo.optJSONArray("modules");
            if (modules != null) {
                for (int i = 0; i < modules.length(); i++) {
                    JSONObject module = modules.optJSONObject(i);
                    if (module != null) {
                        String title = module.optString("title");
                        String detail = module.optString("detail");
                        sb.append(title).append("：").append(detail).append('\n');
                    }
                }
            }
            if (sb.length() != 0)
                sb.deleteCharAt(sb.length() - 1);
            text = sb.toString();
        }
        showCopyDialog(activity, text, KtUtils::setClipboardContent);
        popupWindow.dismiss();
        return true;
    }

    private static void showCopyDialog(Context context, CharSequence text, Consumer<CharSequence> onCopyAll) {
        var appDialogTheme = Utils.getResId("AppTheme.Dialog.Alert", "style");
        var title = Utils.getString("biliroaming_dialog_title_copy_enhance");
        var posButton = Utils.getString("biliroaming_dialog_btn_share");
        var neuButton = Utils.getString("biliroaming_dialog_btn_copy_all");
        var alertDialog = new AlertDialog.Builder(context, appDialogTheme)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(posButton, (dialog, which) -> {
                    var intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.setType("text/plain");
                    context.startActivity(Intent.createChooser(intent, posButton));
                })
                .setNeutralButton(neuButton, (dialog, which) -> onCopyAll.accept(text))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
        ((TextView) alertDialog.findViewById(android.R.id.message))
                .setTextIsSelectable(true);
    }
}
