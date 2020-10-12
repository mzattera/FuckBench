/**
 * Checks that given file exists and its length is not null.
 * 
 * This is used to verify a specific compilation stet
 */
package org.mzattera.bf6502;

import java.io.File;

/**
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class CheckFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				throw new IllegalArgumentException("Wrong number of parameters.");
			}

			File f = new File(args[0]);
			if (isOK(f))
				System.exit(0);
			else
				System.exit(-1);

		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
			System.exit(-1);
		}
	}
	
	/**
	 * @return true if f exists and is not empty.
	 */
	public static boolean isOK(File f) {
		return (f.exists() && f.length() > 0);		
	}

	private static void printUsage() {
		System.out.println();
		System.out.println();
		System.out.println("Usage: java -jar " + CheckFile.class.getSimpleName() + ".jar <file>");
		System.out.println();
		System.out.println("    <file>: Name of input file; it will check file exists and it is not empty.");
		System.out.println();
	}
}
