package app.revanced.bilibili.settings.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.preference.Preference
import app.revanced.bilibili.settings.Settings.prefs
import app.revanced.bilibili.settings.dialog.SpeedTestDialog
import app.revanced.bilibili.utils.*

class UnlockAreaLimitFragment :
    BiliRoamingBaseSettingFragment("biliroaming_setting_unlock_area_limit") {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        findPreference<Preference>("custom_server")?.onClick {
            AlertDialog.Builder(context).run {
                val view = context.inflateLayout("biliroaming_dialog_area_server")
                val editTexts = arrayOf<EditText>(
                    view.findView("cn_server"),
                    view.findView("hk_server"),
                    view.findView("tw_server"),
                    view.findView("th_server")
                )
                editTexts.forEach { it.setText(prefs.getString(it.tag.toString(), "")) }
                setTitle(Utils.getString("biliroaming_custom_server_title"))
                setView(view)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    editTexts.forEach {
                        val host = it.text.toString().trim()
                        if (host.isNotEmpty())
                            prefs.edit {
                                putString(
                                    it.tag.toString(),
                                    host.replace(Regex("^https?://"), "")
                                )
                            }
                        else prefs.edit { remove(it.tag.toString()) }
                    }
                }
                setNegativeButton(Utils.getString("biliroaming_get_public_server")) { _, _ ->
                    val uri = Uri.parse(Utils.getString("biliroaming_server_url"))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }.create().constraintSize().show()
            true
        }
        findPreference<Preference>("test_upos")?.onClick {
            SpeedTestDialog(requireContext()) {
                if (!it) return@SpeedTestDialog
                findPreference<Preference>("upos_host")?.runCatchingOrNull {
                    callMethod("notifyChanged")
                }
            }.show()
            true
        }
    }
}
