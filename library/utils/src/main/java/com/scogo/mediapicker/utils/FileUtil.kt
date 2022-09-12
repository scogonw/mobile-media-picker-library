package com.scogo.mediapicker.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.io.*
import java.net.URLEncoder
import java.util.*


@Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
object FileUtil {

    private const val DOCUMENTS_DIR = "documents"
    private const val MIME_IMAGE_JPEG = "image/jpeg"
    private const val IMAGES_FOLDER_NAME = "Scogo"

    fun saveFile(context: Context, uri: Uri): File? {
        val fileName = getFileName(context, uri)
        val cacheDir = getDocumentCacheDir(context)
        val file = generateFileName(fileName, cacheDir)
        if (file != null) {
            val destinationPath = file.absolutePath
            saveFileFromUri(context, uri, destinationPath)
        }
        return file
    }

    fun saveFileFromUri(context: Context, uri: Uri): Uri? {
        return try {
            val path = saveFile(context,uri)?.absolutePath ?: return null
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
        var inputStream: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
            val buf = ByteArray(1024)
            inputStream?.read(buf)
            do {
                bos.write(buf)
            } while (inputStream?.read(buf) !== -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
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

    fun getMimeType(uri: Uri, context: Context): String? {
        val mimeType: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr: ContentResolver = context.contentResolver
            cr.getType(uri)
        } else {
            val file = uri.path?.let { File(it) }
            val encoded = try {
                URLEncoder.encode(file?.name, "UTF-8").replace("+","%20")
            }catch (e: Exception){
                file?.name
            }
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(encoded)
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase(Locale.ROOT)
            )
        }
        return mimeType
    }

    @Throws(IOException::class)
    fun saveImage(
        context: Context?,
        uri: Uri?,
    ) {
        if(uri == null || context == null) return
        try {
            val actualUri = saveFileFromUri(context,uri) ?: Uri.EMPTY
            val bitmap = getBitmap(context, actualUri)
            val mimeType = getMimeType(actualUri,context)
            val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis())
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$IMAGES_FOLDER_NAME")
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir: String = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM
                ).toString() + File.separator + IMAGES_FOLDER_NAME
                val file = File(imagesDir)
                if (!file.exists()) {
                    file.mkdir()
                }
                val image = File(imagesDir, "${System.currentTimeMillis()}.jpeg")
                FileOutputStream(image)
            }
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos?.flush()
            fos?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch(e: Error) {
            e.printStackTrace()
        }
    }
    private fun getBitmap(context: Context, uri: Uri): Bitmap? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }
}