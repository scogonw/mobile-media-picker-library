package com.scogo.mediapicker.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.io.*

@Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
object FileUtil {

    private const val DOCUMENTS_DIR = "documents"

    fun saveFile(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri)
        val cacheDir = getDocumentCacheDir(context)
        val file = generateFileName(fileName, cacheDir)

        if (file != null) {
            val destinationPath = file.absolutePath
            saveFileFromUri(context, uri, destinationPath)
        }
        return file?.absolutePath
    }

    fun saveFileFromUri(context: Context, uri: Uri): Uri? {
        return try {
            val path = saveFile(context,uri) ?: return null
            val file = File(path)
            Uri.fromFile(file)
        }
        catch (e: Error) { null }
        catch (e: Exception) { null }
    }

    private fun saveFileFromUri(
        context: Context,
        uri: Uri,
        destinationPath: String?
    ) {
        var `is`: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            `is` = context.contentResolver.openInputStream(uri)
            bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
            val buf = ByteArray(1024)
            `is`?.read(buf)
            do {
                bos.write(buf)
            } while (`is`?.read(buf) !== -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getDocumentCacheDir(@NonNull context: Context): File {
        val dir = File(context.cacheDir, DOCUMENTS_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun getFileName(
        @NonNull context: Context?,
        uri: Uri
    ): String? {
        val mimeType = context!!.contentResolver.getType(uri)
        var filename: String? = null
        if (mimeType == null) {
            val path = getPath(context, uri)
            filename = File(path).name
        } else {
            val returnCursor: Cursor? = context.contentResolver.query(
                uri, null,
                null, null, null
            )
            if (returnCursor != null) {
                val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                filename = returnCursor.getString(nameIndex)
                returnCursor.close()
            }
        }
        return filename
    }

    private fun getName(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index = filename.lastIndexOf('/')
        return filename.substring(index + 1)
    }

    private fun getPath(context: Context, uri: Uri): String {
        val absolutePath = PathUtil.getPath(context,uri)
        return absolutePath ?: uri.toString()
    }

    @Nullable
    fun generateFileName(
        @Nullable mName: String?,
        directory: File
    ): File? {
        var name = mName ?: return null
        var file = File(directory, name)
        if (file.exists()) {
            var fileName = name
            var extension = ""
            val dotIndex = name.lastIndexOf('.')
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex)
                extension = name.substring(dotIndex)
            }
            var index = 0
            while (file.exists()) {
                index++
                name = "$fileName($index)$extension"
                file = File(directory, name)
            }
        }
        try {
            if (!file.createNewFile()) {
                return null
            }
        } catch (e: IOException) {
            return null
        }
        return file
    }

}