package jianxiongrao.smileimitation.commen.utils

import java.io.File

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/6
 */
class FileUtil {

    companion object {
        /**
         * 目录下所有的文件大小
         */
        @Throws(Exception::class)
        fun getFolderSize(file: File): Long {
            var size: Long = 0
            try {
                val fileList = file.listFiles()
                for (aFileList in fileList) {
                    if (aFileList.isDirectory) {
                        size += getFolderSize(aFileList)
                    } else {
                        size += aFileList.length()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return size
        }

        interface DeleteListener {
            fun onDelte(size: Long)
            fun onDone()
        }

        fun deleteFloderFile(deleteListener: DeleteListener, vararg dirs: File) {
            for (dir in dirs) {
                try {
                    val fileList = dir.listFiles()
                    for (aFileList in fileList) {
                        if (aFileList.isDirectory) {
                            deleteFloderFile(deleteListener, aFileList)
                        } else {
                            deleteListener.onDelte(deleteFile(aFileList))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                deleteListener.onDone()
            }
        }

        private fun deleteFile(file: File): Long {
            val size = file.length()
            file.delete()
            return size
        }

        /**
         * 输出文件大小
         */
        fun getPrintSize(s: Long): String {
            var size = s.toFloat()
            if (size < 1024) {
                return save2Float(size) + "B" + ")"
            } else {
                size /= 1024
            }
            if (size < 1024) {
                return save2Float(size) + "K" + ")"
            } else {
                size /= 1024
            }
            if (size < 1024) {
                return save2Float(size) + "M" + ")"
            } else {
                size /= 1024
            }
            return save2Float(size) + "G" + ")"
        }

        /**
         * 保留两位有效数字
         */
        private fun save2Float(n: Float): String {
            return "(" + (Math.round(n * 100).toFloat() / 100).toString()
        }
    }


}