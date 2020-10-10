/**
 * 
 */
package org.mzattera.bf6502.stats;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Reads text files created by hacked version of JaC64 (https://github.com/mzattera/JaC64) 
 * and outputs a count of each opcode.
 * 
 * @author Massimiliano Zattera
 *
 */
public class CountOpcodes {

	// Folders where output from JaC64 are stored. *ALL* *.txt files will be processed.
	private final static File FOLDER = new File("D:\\Users\\mzatt\\Projects\\Ja64 Runtime");

	private final static Map<Integer, Long> counter = new HashMap<Integer, Long>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			for (File file : FOLDER.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File f, String name) {
					return name.toLowerCase().endsWith(".txt");
				}

			})) {
				process(counter, file);
			}

			for (Entry<Integer, Long> e : counter.entrySet()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void process(Map<Integer, Long> count, File file) throws NumberFormatException, IOException {
		for (String line : Files.readAllLines(file.toPath())) {
			int opcode = Integer.parseInt(line);
			count.put(opcode, count.getOrDefault(opcode, 0L) + 1);
		}
	}
}
