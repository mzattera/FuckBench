/**
 */
package org.mzattera.bf6502.optimization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.mzattera.util.FileUtil;

/**
 * Tries to misure the extend of the tape area accessed.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class MeasureWorkAreaSize {

	private final static String IN_FILE = "D:\\Users\\mzatt\\Projects\\Git - FuckBench\\wip\\logfuntest.c";
	private final static String OUT_FILE = "D:\\Users\\mzatt\\Projects\\Git - FuckBench\\wip\\logfuntest.1.c";
	private final static String CELL_TYPE = "uint16_t";
	private final static int TAPE_SIZE = 30_000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String inputFileName = IN_FILE;
			String outputFileName = OUT_FILE;
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

		System.out.println(MeasureWorkAreaSize.class.getName() + " tweaking generated code.");
		System.out.println("Input File Name : " + new File(inputFileName).getCanonicalPath());
		System.out.println("Output File Name: " + new File(outputFileName).getCanonicalPath());
		System.out.println("Total tape size : " + tapeSize);

		// Read the emulator .c code
		// String source = new String(Files.readAllBytes(Paths.get(inputFileName)));

		// Original source code
		List<String> source = Files.readAllLines(Paths.get(inputFileName));

		// New source code
		List<String> newCode = new ArrayList<>(source.size());

		// Takes opcode check source code from the emulator.
		int i = 0;

		// Fix header, tape and cell size
		for (i = 0; i < source.size(); ++i) {
			String line = source.get(i);
			if (line.equals("#include <stdint.h>")) {
				newCode.add("#include <stdint.h>");
				newCode.add("#include <stddef.h>");
			} else if (line.equals("static uint8_t m[30000], *p = m;")) {
				newCode.add("static " + CELL_TYPE + " m[" + tapeSize + "], *p = m;");
				newCode.add("size_t _min = " + tapeSize + ", _max = 0;");
				newCode.add("size_t i(size_t s) {");
				newCode.add("	size_t base = p - m;");
				newCode.add("	size_t curr = base + s;");

				newCode.add("	if (curr < _min) _min = curr;");
				newCode.add("	if (curr > _max) _max = curr;");
				newCode.add("	return s;");
				newCode.add("}");
			} else if (line.trim().equals("return 0;")) {
				newCode.add("	printf(\"\\n\\n>>> Min. %lu Max. %lu\\n\", _min, _max);");
				newCode.add("	return 0;");
			} else {
				newCode.add(line.replaceAll("\\[([^\\]]+)\\]", "[i($1)]"));
			}
		}

		FileUtil.write(newCode, outputFileName);
	}

	private static void printUsage() {
		System.out.println();
		System.out.println();
		System.out.println("Usage: java -jar " + MeasureWorkAreaSize.class.getSimpleName()
				+ ".jar [-i <in>] [-o <out>] [-s <size>]");
		System.out.println();
		System.out.println("    <in>   : Input .c file name (defaults to \"" + IN_FILE + "\").");
		System.out.println(
				"    <out>  : Output .c file name; can be same of inupt file (defaults to \"" + IN_FILE + "\").");
		System.out.println("    <size> : Total size of tape (defaults to " + TAPE_SIZE + ").");
		System.out.println();
	}
}
