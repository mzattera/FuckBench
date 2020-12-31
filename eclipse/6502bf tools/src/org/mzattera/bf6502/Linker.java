/**
 * This takes a 6502 compiled code, translates it into BrainFuck commands necessary to write
 * to write it into 6502bf emulator mem table. Then, it appends the code at the beginning of 6502 emulator BrainFuck code, 
 * creating a single BrainFuck source that contains both the 6502bf emulator and the code to run on the emulated CPU.
 */
package org.mzattera.bf6502;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.mzattera.util.FormattedPrintWriterWrapper;

/**
 * @author Massimliano "Maxi" Zattera
 *
 */
public class Linker {

	// TODO when merging, check of all the values appearing in the 6502 binary,
	// then remove from emulator code the check for those opcode not found in the
	// code.

	// Name of emulator source file (without extension)
	public final static String CODE_NAME = "6502bf";

	// Cells on the BF tape to skip before finding 6502 memory (start of mem[] array on tape)
	public final static int SKIP = 124;

	// Start address of 6502 program
	public final static int START_ADDRESS = 0x0200;

	// Address where to load binary inside 6502 memory
	public final static int LOAD_ADDRESS = 0x0200;

	public static final int RESET_VECTOR = 0xfffc;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String inputFileName = null, outputFileName = null;
			int skip = SKIP; // cells to skip before writing 6502 memory content
			int loadAddress = LOAD_ADDRESS; // Address in 6502 memory where to load binary file
			int startAddress = START_ADDRESS; // Address in 6502 memory where code starts
			String emuCodeFileName = CODE_NAME + ".bf"; // Name of 6502 BF emulator

			// Process CLI parameters
			inputFileName = args[0];
			outputFileName = args[1];

			for (int i = 2; i < args.length; i++) {
				if (args[i].equals("-s"))
					skip = Integer.parseInt(args[++i]);
				else if (args[i].equals("-i"))
					emuCodeFileName = args[++i];
				else if (args[i].equals("-a"))
					startAddress = Integer.parseInt(args[++i]);
				else if (args[i].equals("-l"))
					loadAddress = Integer.parseInt(args[++i]);
				else
					throw new IllegalArgumentException("Unrecognized parameter: " + args[i]);
			}

			execute(inputFileName, outputFileName, emuCodeFileName, skip, startAddress, loadAddress);
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
			System.exit(-1);
		}
	}

	/**
	 * Merge 6502 code and emulator source.
	 * 
	 * @param inputFileName   Binary file with executable 6502 code.
	 * @param outputFileName  Name of resulting BrainFuck file.
	 * @param emuCodeFileName Name of BrainFuck code for the 6502 emulator.
	 * @param skip            Number of cells to skip on tape before writing the
	 *                        6502 code. It is wher the 6502 memory starts on tape.
	 * @param startAddress    Address in 6502 memory where the program start. Use -1
	 *                        if the code sets up start (reset vector) address
	 *                        itself.
	 * @param loadAddress     Address in 6502 memory where to load the code.
	 * @throws IOException
	 */
	public static void execute(String inputFileName, String outputFileName, String emuCodeFileName, int skip,
			int startAddress, int loadAddress) throws IOException {

		// TODO Add parameters error checking
		// TODO check that pos stays within 6502 memory

		System.out.println(Linker.class.getName() + " linking BF code to 6502bf emulator.");
		System.out.println("Input File Name (6502 Code)              : " + new File(inputFileName).getCanonicalPath());
		System.out
				.println("Input File Name (6502 Emu.)              : " + new File(emuCodeFileName).getCanonicalPath());
		System.out.println("Output File Name                         : " + new File(outputFileName).getCanonicalPath());
		System.out.println("6502bf mem[] position on tape            : " + skip);
		System.out.println("6502 code load address in emulated memory: " + loadAddress);
		System.out.println("6502 program start address               : " + startAddress);

		// How many cells to skip on the tape before writing 6502 code
		// We need to skip the variables the FBF emulator uses and locate ourself at the
		// beginning
		// of the code, taking in consideration that 6502 memory is a table after all
		// other variables;
		// the table uses 2*n+3 cells, where n is table size.
		int toSkip = skip + (loadAddress * 2 + 3);
		byte[] code = Files.readAllBytes(Paths.get(inputFileName));

		// Writes 6502 code at the beginning of the BrainFuck file.
		try (FormattedPrintWriterWrapper writer = new FormattedPrintWriterWrapper(outputFileName, "ASCII", 72)) {
			
			// Skip variables at the beginning of the tape
			for (int i = 0; i < toSkip; ++i)
				writer.print(">");

			// Current position on tape
			int pos = toSkip;

			// Write 6502 code on tape
			for (int i = 0; i < code.length; ++i) {
				if (i > 0) {
					writer.print(">>"); // Remember each 6502 memory location uses 2 cells on tape
					pos += 2;
				}
				for (int j = 0; j < Byte.toUnsignedInt(code[i]); ++j)
					writer.print("+");
			}

			// Write reset vector, so that CPU will jump to start address on reset
			if (startAddress >= 0) {
				// Position of reset vector on tape
				int vpos = skip + (RESET_VECTOR * 2 + 3);

				// Go to reset vector
				while (pos < vpos) {
					writer.print(">");
					pos++;
				}
				while (pos > vpos) {
					writer.print("<");
					pos--;
				}

				// Writes reset vector notice we cannot assume now it is 0 as we loaded code
				writer.print("[-]");
				for (int i = 0; i < startAddress % 256; ++i) {
					writer.print("+");
				}
				writer.print(">>[-]");
				for (int i = 0; i < startAddress / 256; ++i) {
					writer.print("+");
				}
				pos += 2;
			}

			// Go back to 0 position
			while (pos > 0) {
				writer.print("<");
				pos--;
			}

			// Copy the emulator code to the file
			// TODO probably can be optimized and made robust for HUGE files.
			for (String line : Files.readAllLines(new File(emuCodeFileName).toPath(), Charset.forName("ASCII")))
				writer.print(line);
		}
	}

	private static void printUsage() {
		System.out.println();
		System.out.println();
		System.out.println("Usage: java -jar " + Linker.class.getSimpleName()
				+ ".jar <code> <out> [-i <emu>] [-a <addr>] [-l <ldaddr>] [-s <skip>]");
		System.out.println();
		System.out.println("    <code>  : Name of binary file with 6502 code.");
		System.out.println("    <out>   : Name of output BF file.");
		System.out.println("    <emu>   : Name of 6502bf emulator (defaults to \"" + CODE_NAME + ".bf\").");
		System.out.println(
				"    <addr>  : Starting address of 6502 code in 6502 memory (defaults to " + START_ADDRESS + ").");
		System.out.println(
				"    <ldaddr>: Address in 6502 memory where to load the binaries (defaults to " + LOAD_ADDRESS + ").");
		System.out.println(
				"              Use -1 to NOT overwrite 6502 reset vector. Emulator code must then set it up correctly.");
		System.out.println(
				"    <skip>  : How many cells to skip before writing 6502 code onto tape (defaults to " + SKIP + ").");
		System.out.println("              This is the position on tape where mem[] array containg 6502 memory starts.");
		System.out.println();
	}
}
