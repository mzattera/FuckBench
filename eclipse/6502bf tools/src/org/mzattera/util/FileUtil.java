/**
 * 
 */
package org.mzattera.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Various file utilities.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class FileUtil {

	private FileUtil() {
	}

	/**
	 * Write a string into an UTF-8 file.
	 */
	public static void write(String txt, String fileName) throws IOException {
		write(txt, fileName, "UTF-8");
	}

	/**
	 * Write a string into a file with given encoding.
	 */
	public static void write(String txt, String fileName, String encoding) throws IOException {

		try (BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName), encoding))) {
			out.write(txt);
			out.flush();
		}
	}

	/**
	 * Write a list of strings into a file. Uses UTF-8 encoding.
	 */
	public static void write(List<String> txt, String fileName) throws IOException {
		write(txt, fileName, "UTF-8");
	}

	/**
	 * Write a list of strings into a file with given encoding.
	 */
	public static void write(List<String> txt, String fileName, String encoding) throws IOException {

		try (BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName), encoding))) {
			for (String s : txt) {
				out.write(s);
				out.newLine();
			}
			out.flush();
		}
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * It assumes file is in UTF-8 encoding.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(File file) throws IOException {
		return read(file, "UTF-8");
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(File file, String encoding) throws IOException {
		return read(file.getCanonicalPath(), encoding);
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * It assumes file is in UTF-8 encoding.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(String fileName) throws IOException {
		return read(fileName, "UTF-8");
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(String fileName, String encoding) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding))) {
			return in.lines().collect(Collectors.toList());
		}
	}

	/**
	 * Replaces the extension for a file.
	 * 
	 * @param ext
	 *            New extension.
	 * 
	 * @return Original file name, but with its extension changed.
	 * 
	 * @throws IOException 
	 */
	public static String replaceExtension(File f, String ext) throws IOException {
		return replaceExtension(f.getCanonicalPath(), ext);
	}

	/**
	 * Replaces the extension for a file.
	 * 
	 * @param fileName
	 *            Original file name.
	 * @param ext
	 *            New extension.
	 * 
	 * @return Original file name, but with its extension changed.
	 */
	public static String replaceExtension(String fileName, String ext) {
		int p = fileName.lastIndexOf('.');
		String result;

		if (p == -1) {
			result = fileName;
		} else {
			result = fileName.substring(0, p);
		}

		if (ext.startsWith(".") && (ext.length() > 1))
			ext = ext.substring(1);

		return result + '.' + ext;
	}

	/**
	 * Removes the extension for a file.
	 * 	
	 * @param ext
	 *            New extension.
	 * 
	 * @return Original file name, but with its extension removed.
	 * @throws IOException 
	 */
	public static String removeExtension(File f) throws IOException {
		return removeExtension(f.getCanonicalPath());
	}

	/**
	 * Removes the extension for a file.
	 * 
	 * @param fileName
	 *            Original file name.
	 * @param ext
	 *            New extension.
	 * 
	 * @return Original file name, but with its extension removed.
	 */
	public static String removeExtension(String fileName) {
		int p = fileName.lastIndexOf('.');
		if (p == -1) {
			return fileName;
		} else {
			return fileName.substring(0, p);
		}
	}
}
