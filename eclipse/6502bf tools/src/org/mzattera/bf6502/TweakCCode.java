/**
 * This modifies .c files created by Esotope BrainFuck to C compiler so that it has enough memory on tape reserved and
 * cells are 16 bit wide.
 */
package org.mzattera.bf6502;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Massimliano "Maxi" Zattera
 *
 */
public class TweakCCode {

	private final static String CODE_NAME = Linker.CODE_NAME + ".c";

	private final static int TAPE_SIZE = Linker.SKIP + 2 * 65536 + 3;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String inputFileName = CODE_NAME;
			String outputFileName = CODE_NAME;
			int tapeSize = TAPE_SIZE;

			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-i"))
					inputFileName = args[++i];
				else if (args[i].equals("-o"))
					outputFileName = args[++i];
				else if (args[i].equals("-s"))
					tapeSize = Integer.parseInt(args[++i]);
				else
					throw new IllegalArgumentException("Unrecognized parameter: " + args[i]);
			}

			execute(inputFileName, outputFileName, tapeSize);
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
			System.exit(-1);
		}
	}

	public static void execute(String inputFileName, String outputFileName, int tapeSize) throws IOException {

		// TODO Add parameters error checking

		System.out.println(TweakCCode.class.getName() + " tweaking generated code.");
		System.out.println("Input File Name : " + new File(inputFileName).getCanonicalPath());
		System.out.println("Output File Name: " + new File(outputFileName).getCanonicalPath());
		System.out.println("Total tape size : " + tapeSize);

		// Read the emulator .c code
		List<String> in = Files.readAllLines(new File(inputFileName).toPath(), Charset.forName("ASCII"));

		// Write tweaked code
		// TODO surely this can be optimized
		try (PrintWriter writer = new PrintWriter(outputFileName, "ASCII")) {
			for (String line : in)
				if (line.trim().equals("static uint8_t m[30000], *p = m;"))
					writer.println("static uint16_t m[" + tapeSize + "], *p = m;");
				else
					writer.println(line);
		}
	}

	private static void printUsage() {
		System.out.println();
		System.out.println();
		System.out.println("Usage: java -jar " + TweakCCode.class.getSimpleName() + ".jar [-i <in>] [-o <out>] [-s <size>]");
		System.out.println();
		System.out.println("    <in>   : Input .c file name (defaults to \"" + CODE_NAME + "\").");
		System.out.println(
				"    <out>  : Output .c file name; can be same of inupt file (defaults to \"" + CODE_NAME + "\").");
		System.out.println("    <size> : Total size of tape (defaults to " + TAPE_SIZE + ").");
		System.out.println();
	}
}
