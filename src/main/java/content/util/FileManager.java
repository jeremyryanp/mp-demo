package content.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.io.IOUtils;

public class FileManager {
    private static final String ROOT_FOLDER = "/ora1/content";
    private static final MimetypesFileTypeMap fileTypeMap;

    static {
        fileTypeMap = new MimetypesFileTypeMap();
        fileTypeMap.addMimeTypes("image/png png");
        fileTypeMap.addMimeTypes("application/pdf pdf");
        fileTypeMap.addMimeTypes("application/vnd.ms-excel xls xlsx");
        fileTypeMap.addMimeTypes("application/msword doc docx");
        fileTypeMap.addMimeTypes("application/vnd.ms-powerpoint ppt pptx");
    }

    private FileManager() {
        super();
    }

    public static String createFullFilePath(String filePath, String fileName) {
        return FileManager.getRoot() + File.separator + filePath + File.separator + fileName;
    }

    public static String createFilePathFromRoot(String filePath, String fileName) {
        return filePath + File.separator + fileName;
    }

    public static KeyValue splitPath(String fullPath) {
        String folder = null, fileName = null;
        if (fullPath == null) {
            return null;
        }
        int idx = fullPath.lastIndexOf("/");
        if (idx == -1) {
            fileName = fullPath;
            folder = File.separator;
        } else {
            if (idx == 0) {
                folder = fullPath.substring(0, 1);
            } else {
                folder = fullPath.substring(0, idx);
            }

            if ((idx + 1) == fullPath.length()) {
                fileName = "";
            } else {
                fileName = fullPath.substring(idx + 1, fullPath.length());
            }
        }
        folder = FileManager.removeTrailingSeperator(folder);
        return new DefaultKeyValue(folder, fileName);
    }

    public void copy(String srcPath, String srcFileName, String targetPath, String targetFileName)
        throws FileNotFoundException, IOException {
        copy(srcPath, srcFileName, targetPath, targetFileName, true);
    }

    public void copy(String srcPath, String srcFileName, String targetPath, String targetFileName, boolean createPath)
        throws FileNotFoundException, IOException {
        FileInputStream srcFileInput = null;
        FileOutputStream srcFileOutput = null;
        try {
            targetPath = FileManager.removeTrailingSeperator(targetPath);
            srcFileInput = new FileInputStream(createFullFilePath(srcPath, srcFileName));
            File targetPathFile = new File(targetPath);
            if (createPath && targetPathFile != null && !targetPathFile.exists()) {
                targetPathFile.mkdirs();
            }
            srcFileOutput = new FileOutputStream(createFullFilePath(targetPath, targetFileName));
            IOUtils.copy(srcFileInput, srcFileOutput);
        } finally {
            IOUtils.closeQuietly(srcFileInput);
            IOUtils.closeQuietly(srcFileOutput);

        }
    }

    public byte[] getContent(String realPath)
        throws FileNotFoundException, IOException {
        FileInputStream srcFileInput = null;
        byte[] bytes = null;
        try {
            File base = createBaseFile(realPath);
            srcFileInput = new FileInputStream(base);
            bytes = IOUtils.toByteArray(srcFileInput);
        } finally {
            IOUtils.closeQuietly(srcFileInput);
        }
        return bytes;
    }

    public void writeFile(String realPath, byte[] bs)
        throws IOException {
        writeFile(realPath, bs, true);
    }

    public void writeFile(String realPath, InputStream is)
        throws IOException {
        byte[] bs = IOUtils.toByteArray(is);
        writeFile(realPath, bs, true);
    }

    public void writeFile(String realPath, byte[] bs, boolean createPath)
        throws IOException {
        FileOutputStream srcFileOutput = null;
        try {
            File base = createBaseFile(realPath);
            File parent = base.getParentFile();
            if (createPath && parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            srcFileOutput = new FileOutputStream(base);
            IOUtils.write(bs, srcFileOutput);
        } finally {
            IOUtils.closeQuietly(srcFileOutput);
        }
    }

    public static String extractType(String name) {
        int idx = name.lastIndexOf(".");
        if (name != null && idx != -1) {
            return name.substring(idx + 1, name.length());
        }
        return null;
    }

    public static String removeTrailingSeperator(String folder) {
        if (folder != null && folder.length() > 1 && folder.endsWith("/")) {
            folder = folder.substring(0, folder.length() - 1);
        }
        return folder;
    }

    private static class SingletonHolder {
        public static final FileManager INSTANCE = new FileManager();
    }

    public static FileManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean exists(String path) {
        File file = createBaseFile(path);
        return file.exists();
    }

    public boolean delete(String path) {
        boolean returnVal = true;
        File file = createBaseFile(path);
        if (file.exists()) {
            file.delete();
        } else {
            returnVal = false;
        }
        return returnVal;
    }

    public boolean isFile(String path) {
        File file = createBaseFile(path);
        return file.isFile();

    }

    public boolean isFolder(String path) {
        File file = createBaseFile(path);
        return file.isDirectory();
    }

    public File[] listFiles(String path) {
        File file = createBaseFile(path);
        return file.listFiles(ifile -> ifile.isFile());
    }

    public File[] listFolders(String path) {
        File file = createBaseFile(path);
        return file.listFiles(ifile -> ifile.isDirectory());
    }

    public File[] listAll(String path) {
        File file = createBaseFile(path);
        return file.listFiles();
    }

    public void createPath(String path) {
        createPath(path, false);
    }

    public void createPath(String path, boolean createAll) {
        File file = createBaseFile(path);
        if (createAll) {
            file.mkdirs();
        } else {
            file.mkdir();
        }
    }

    public File getParent(String path) {
        File file = createBaseFile(path);
        return file.getParentFile();
    }

    public File info(String path) {
        File file = new File(path);
        return file;
    }

    private File createBaseFile(String path) {
        return new File(FileManager.getRoot() + File.separator + path);
    }

    public static String getRoot() {
        return ROOT_FOLDER;
    }

    public static String getFileType(String path)
        throws Exception {
        return fileTypeMap.getContentType(path);
    }
}
