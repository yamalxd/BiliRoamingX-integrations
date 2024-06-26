package app.revanced.bilibili.utils

import android.content.Context
import app.revanced.bilibili.patches.SplashPatch
import app.revanced.bilibili.settings.Settings
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object BackupHelper {
    private const val DESCRIPTOR_ID_INTERNAL = "internal"
    private const val DESCRIPTOR_ID_EXTERNAL = "external"
    private const val TYPE_PREFS = "prefs"
    private const val TYPE_FILE = "file"
    private const val TYPE_BLKV = "blkv"

    private val internalDir: File
        get() = Utils.getContext().dataDir
    private val externalDir: File
        get() = Utils.getContext().getExternalFilesDir(null)?.parentFile!!
    private val tempFile: File
        get() = File(Utils.getContext().getExternalFilesDir(null), "biliroaming_backup.tmp")

    fun backup(output: OutputStream) {
        val zipOut = ZipOutputStream(output)
        zipOut.setLevel(5)
        val metaInfo = JSONObject()
        metaInfo.put("version", 1)
        metaInfo.put("time", System.currentTimeMillis())
        val items = JSONArray()
        metaInfo.put("items", items)

        val backupPrefsIfExist = { name: String, blkv: Boolean ->
            val prefsFile = prefsPath(name, blkv)
            if (prefsFile.isFile) {
                val type = if (blkv) TYPE_BLKV else TYPE_PREFS
                val ext = if (blkv) "blkv" else "xml"
                val prefsItem = JSONObject().apply {
                    put("type", type)
                    put("name", name)
                }
                items.put(prefsItem)
                zipOut.putNextEntry(ZipEntry("$type/$name.$ext"))
                prefsFile.inputStream().use { it.copyTo(zipOut) }
            }
        }
        val backupFileIfExist = { file: File ->
            if (file.isFile) {
                val fileItem = JSONObject().apply {
                    put("type", TYPE_FILE)
                    put("location", file.name)
                    put("restore_path", file.toPathDescriptor())
                }
                items.put(fileItem)
                zipOut.putNextEntry(ZipEntry("$TYPE_FILE/${file.name}"))
                file.inputStream().use { it.copyTo(zipOut) }
            }
        }

        backupPrefsIfExist(Settings.PREFS_NAME, false)
        backupPrefsIfExist(Constants.PREFS_VH, false)
        backupFileIfExist(SubtitleParamsCache.FONT_FILE)
        backupFileIfExist(File(Utils.getContext().filesDir, SplashPatch.SPLASH_IMAGE))
        backupFileIfExist(File(Utils.getContext().filesDir, SplashPatch.LOGO_IMAGE))

        zipOut.putNextEntry(ZipEntry("backup.json"))
        zipOut.write(metaInfo.toString().toByteArray())
        zipOut.finish()
    }

    fun restore(input: InputStream) {
        val tempFile = tempFile.apply { delete() }
        tempFile.outputStream().use { input.copyTo(it) }
        val zipFile = ZipFile(tempFile)
        fun ZipFile.entry(name: String) = run { getInputStream(getEntry(name)) }
        val metaInfo = zipFile.entry("backup.json")
            .use { it.readBytes() }.toString(Charsets.UTF_8).toJSONObject()
        val version = metaInfo.optInt("version")
        Logger.debug { "backup version: $version" }

        val time = metaInfo.optLong("time")
        Logger.debug {
            val formatTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date(time))
            "backup time: $formatTime"
        }

        val items = metaInfo.optJSONArray("items")
        Logger.debug { "backup items: ${items?.toString(2)}" }

        items?.forEach { item ->
            when (val type = item.optString("type")) {
                TYPE_PREFS, TYPE_BLKV -> {
                    val name = item.optString("name")
                    val blkv = type == TYPE_BLKV
                    val entryName = if (blkv) "$TYPE_BLKV/$name.blkv" else "$TYPE_PREFS/$name.xml"
                    val prefsPath = prefsPath(name, blkv)
                    zipFile.entry(entryName).use { input ->
                        prefsPath.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                TYPE_FILE -> {
                    val location = item.optString("location")
                    val restorePath = item.optString("restore_path")
                    zipFile.entry("$type/$location").use { input ->
                        restorePath.fromPathDescriptor().outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                else -> Logger.warn { "not supported backup item type: $type" }
            }
        }

        tempFile.delete()
    }

    private fun prefsPath(name: String, blkv: Boolean) = if (blkv) {
        File(Utils.getContext().getDir("blkv", Context.MODE_PRIVATE), "$name.blkv")
    } else {
        File(Utils.getContext().dataDir, "shared_prefs/$name.xml")
    }

    private fun File.toPathDescriptor(): String {
        val path = absolutePath

        val internalPath = internalDir.absolutePath
        if (path.startsWith(internalPath))
            return path.replace(internalPath, DESCRIPTOR_ID_INTERNAL)

        val externalPath = externalDir.absolutePath
        if (path.startsWith(externalPath))
            return path.replace(externalPath, DESCRIPTOR_ID_EXTERNAL)

        return path
    }

    private fun String.fromPathDescriptor(): File {
        if (startsWith(DESCRIPTOR_ID_INTERNAL))
            return File(replace(DESCRIPTOR_ID_INTERNAL, internalDir.absolutePath))

        if (startsWith(DESCRIPTOR_ID_EXTERNAL))
            return File(replace(DESCRIPTOR_ID_EXTERNAL, externalDir.absolutePath))

        return File(this)
    }
}
