package br.com.virtualsistemas.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Simplificar acesso aos metos relacionados a arquivos
 * 
 * @author juniorlatalisa
 */
public class FileUtils {

	/**
	 * @see File#lastModified()
	 */
	public static Date getLastModified(File file) {
		long lastModified;
		return ((lastModified = file.lastModified()) > 0) ? new Date(lastModified) : null;
	}

	/**
	 * @see URLConnection#getContentType()
	 * @see IOException
	 */
	public static String getContentType(URL file) {
		try {
			return file.openConnection().getContentType();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see #getContentType(URL)
	 * @see MalformedURLException
	 */
	public static String getContentType(File file) {
		try {
			return getContentType(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see Paths#get(String, String...)
	 * @see Files#createDirectories(Path, java.nio.file.attribute.FileAttribute...)
	 * @see IOException
	 */
	public static Path createDirectories(String root, String... more) {
		try {
			return Files.createDirectories(Paths.get(root, more));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static File createDirectories(File root, String... more) {
		File retorno = root;
		for (String dir : more) {
			retorno = new File(retorno, dir);
		}
		retorno.mkdirs();
		return retorno;
	}

	/**
	 * @see Path#resolve(String)
	 * @see Files#createDirectories(Path, java.nio.file.attribute.FileAttribute...)
	 */
	public static Path createDirectories(Path root, String... more) {
		Path retorno = root;
		for (String dir : more) {
			retorno = retorno.resolve(dir);
		}
		try {
			return Files.createDirectories(retorno);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see Files#readAllBytes(Path)
	 * @see IOException
	 */
	public static byte[] read(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] read(File file) {
		return read(file.toPath());
	}

	/**
	 * @see Files#write(Path, byte[], OpenOption...)
	 * @see IOException
	 */
	public static void write(Path path, byte[] bytes, OpenOption... options) {
		try {
			Files.write(path, bytes, options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void write(File file, byte[] bytes, OpenOption... options) {
		write(file.toPath(), bytes, options);
	}
}