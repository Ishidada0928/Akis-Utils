package com.aki.akisutils.apis.util.filesystems;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;

/**
 * ファイルシステムのファイル,jar(zip)内のファイル、リソースのファイルを扱うクラスです。 対象とするファイルは下記のように指定出来ます。
 * <dl>
 * <dt>ファイルシステムのパス</dt>
 * <dd>(Windows) C:\temp\sample.txt</dd>
 * <dd>(UNIX/Linux)/temp/sample.txt</dd>
 * <dt>jar(zip)内ファイルのURL</dt>
 * <dd>(Windows) jar:file:/C:\temp\sample.zip!/sample.txt</dd>
 * <dd>(UNIX/Linux)jar:file:/temp/sample.zip!/sample.txt</dd>
 * <dt>リソース内のパス</dt>
 * <dd>/sample/sample.txt
 * </dl>
 */

/**
 * <a href="https://qiita.com/ys1230415/items/e4561bc61f664eabf51f">...</a>
 * Thanks, @ys1230415
 * */
public class InsideJarZipUtils {
    /**
     * 通常のファイルシステム内のファイル、jar（zip）内のファイル、リソース内のファイルをコピーします。
     *
     * @param src
     *            ファイルのパス、Jar内のファイルへのURL、リソースのパス
     * @param dst
     *            コピー先のパス
     * @return コピー先のPath
     */
    public static Path copy(String src, String dst) {
        Path path = null;
        try {
            if (isInFileSystem(src) || isResourceInFileSystem(src)) {
                path = Files.copy(Paths.get(getURI(src)), Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
            } else if (isInJarFile(src) || isResourceInJarFile(src)) {
                path = copyFromInJar(getURI(src), dst);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * jar(zip)内にあるファイルをファイルシステムにコピーします。
     *
     * @param uri
     *            コピー元となるjar(zip)内のファイルを指定したURI
     * @param dst
     *            コピー先のパス
     * @return コピー先のPath
     * @throws IOException
     */
    private static Path copyFromInJar(URI uri, String dst) throws IOException {
        Path path = null;
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try (FileSystem jarFs = FileSystems.newFileSystem(URI.create(uri.toString().split("!")[0]), env)) {
            Path pathInJarFile = jarFs.getPath(uri.toString().split("!")[1]);
            path = Files.copy(pathInJarFile, Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
        }
        return path;
    }

    /**
     * 通常のファイルシステム内のファイル、jar（zip）内のファイル、リソース内のファイルからbyte[]を読み込みます。
     *
     * @param src
     *            ファイルのパス、Jar内のファイルへのURL、リソースのパス
     */
    public static byte[] getReadAllBytes(String src) {
        byte[] bytes = null;
        try {
            if (isInFileSystem(src) || isResourceInFileSystem(src)) {
                bytes = Files.readAllBytes(Paths.get(getURI(src)));
            } else if (isInJarFile(src) || isResourceInJarFile(src)) {
                bytes = getReadAllBytesInJar(getURI(src));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * jar(zip)内にあるファイルからbyte[]を読み込みます。
     *
     * @param uri
     *            Jar(zip)内のファイルを指定したURI
     * @throws IOException
     */
    private static byte[] getReadAllBytesInJar(URI uri) throws IOException {

        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try (FileSystem jarfs = FileSystems.newFileSystem(URI.create(uri.toString().split("!")[0]), env)) {
            return Files.readAllBytes(jarfs.getPath(uri.toString().split("!")[1]));
        }
    }

    /**
     * 通常のファイルシステム内のファイル、jar（zip）内のファイル、リソース内のファイルから行を一行ずつ取得したListを取得します。
     *
     * @param src
     *            ファイルのパス、Jar内のファイルへのURL、リソースのパス
     */
    public static List<String> getReadAllLines(String src) {
        List<String> lines = new ArrayList<String>();
        try {
            if (isInFileSystem(src) || isResourceInFileSystem(src)) {
                lines = Files.readAllLines(Paths.get(getURI(src)));
            } else if (isInJarFile(src) || isResourceInJarFile(src)) {
                lines = getReadAllLinesInJarFile(getURI(src));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * ファイル・システム、jar(zip)、リソース内のファイルいずれかのURLを取得します。
     *
     * @param src
     *            ファイルシステムのパス、jar()内ファイルのURL、リソースのパスのいずれか
     * @return
     */
    private static URL getURL(String src) {
        if (isInFileSystem(src)) {
            return getURLInFileSystem(src);
        } else if (isInJarFile(src)) {
            return getURLInJarFile(src);
        } else if (isResource(src)) {
            return getURLInResource(src);
        }
        return null;
    }

    /**
     * jar(zip)内にあるファイルから読み込んだ行のListを取得します。
     *
     * @param uri
     *            Jar(zip)内のファイルを指定したURI
     * @return ファイルを一行ずつ読み込んだList
     * @throws IOException
     */
    private static List<String> getReadAllLinesInJarFile(URI uri) throws IOException {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try (FileSystem Jarfs = FileSystems.newFileSystem(URI.create(uri.toString().split("!")[0]), env)) {
            return Files.readAllLines(Jarfs.getPath(uri.toString().split("!")[1]));
        }
    }

    /**
     * URIを作成します。
     *
     * @param src
     *            ファイルシステムのパス、jar()内ファイルのURL、リソースのパスのいずれか
     * @return
     */
    private static URI getURI(String src) {
        // URLにバックスラッシュが含まれている場合は/に置換
        // jar内にあるURLを利用する場合にurisyntaxexceptionが発生するため。
        URI uri = URI.create(getURL(src).toString().replace(File.separator, "/"));
        return uri;
    }

    /**
     * jar(zip)内にあるファイルを確認し、ファイルが存在する場合はそのURLを取得します。
     *
     * @param src
     *            jar(zip)内のファイルを示すURL
     * @return jar(zip)内にあるファイルのURLを返します。 {@code null} ファイルが見つからなかった場合
     */
    private static URL getURLInJarFile(String src) {
        try {
            URL url = new URL(src);
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            connection.connect();
            return url;
            // jar(zip)内のURLを指定していない場合はClassCastExceptionが発生する
        } catch (IOException | ClassCastException e) {
            return null;
        }
    }

    /**
     * ファイルシステム内にあるファイルのURLを取得します。
     *
     * @param src
     *            ファイルシステムのパス
     * @return ファイルシステムのURL {@code null} ファイルが見つからなかった場合
     */
    private static URL getURLInFileSystem(String src) {
        File file = new File(src);
        try {
            return file.toURI().toURL();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * リソースのURLを取得します。
     *
     * @param src
     *            リソース内のパスを指定します。 <br>
     *            例) /samlpe/sample.txt<br>
     *            例) sample.txtt
     * @return リソースのURL {@code null} ファイルが見つからなかった場合
     */
    private static URL getURLInResource(String src) {
        // "."から始まっているとjarにパッケージした際にうまく取得出来ないため"."から始まる場合は頭の"."を削除
        if (src.startsWith("." + File.separator)) {
            src = src.substring(1);
        }
        // "/"で始まっているファイル場合と、そうでない場合に備えてgetResourceは2パターン使う
        URL url = InsideJarZipUtils.class.getResource(src);
        if (!Optional.ofNullable(url).isPresent()) {
            url = InsideJarZipUtils.class.getClassLoader().getResource(src);
        }
        return url;
    }

    /**
     * ファイルシステムに存在するかを確認します。
     *
     * @param src
     *            ファイルシステムのパス
     * @return 存在する場合は true
     */
    private static boolean isInFileSystem(String src) {
        File file = new File(src);
        {
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * jar(zip)内にファイルが存在するかを確認します。
     *
     * @param src
     *            jar(zip)内のファイルを示すURL
     * @return 存在する場合は true
     */
    private static boolean isInJarFile(String src) {
        if (Optional.ofNullable(getURLInJarFile(src)).isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * リソースにファイルが存在するかを確認します。
     *
     * @param src
     *            リソースのパス
     * @return 存在する場合は true
     */
    private static boolean isResource(String src) {
        if (Optional.ofNullable(getURLInResource(src)).isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * リソースのファイルがファイルシステムに存在するかを確認します。
     *
     * @param src
     *            リソースのパス
     * @return 存在する場合は true
     */
    private static boolean isResourceInFileSystem(String src) {
        if (isResource(src)) {
            if (!isInJarFile(getURLInResource(src).toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * リソースのファイルがjar内に存在するかを確認します。
     *
     * @param src
     *            リソースのパス
     * @return 存在する場合は true
     */
    private static boolean isResourceInJarFile(String src) {
        if (isResource(src)) {
            if (isInJarFile(getURLInResource(src).toString())) {
                return true;
            }
        }
        return false;
    }

    public static void FileStreamCopy(FileInputStream inputStream, FileOutputStream outputStream) {
        try {
            FileChannel inputChannel = inputStream.getChannel();
            FileChannel outputChannel = outputStream.getChannel();

            ByteBuffer bf = ByteBuffer.allocateDirect((int) inputChannel.size());
            bf.clear();
            inputChannel.read(bf);

            bf.flip();
            outputChannel.write(bf);

            bf.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}