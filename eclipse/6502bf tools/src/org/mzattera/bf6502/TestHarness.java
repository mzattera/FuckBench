/**
 * This runs the test suite.
 * Under the test folder, each *.a file will be compiled and executed and its output (stored as *.result file) is compared with content
 * of corresponding *.ref file. The test is considered passed if the two match. A test report is generated afterwards.
 * 
 * ** NOTICE IT MUST BE RUN FROM VISULA STUDIO CONSOLE< NOT FORM ECLIPSE *********
 */
package org.mzattera.bf6502;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.Arrays;

import org.mzattera.util.FileUtil;

/**
 * @author Massimliano "Maxi" Zattera
 *
 */
public class TestHarness {

	// TODO make these more parameterized somehow....

	// Folder where test files are stored
	private static final File WORKING_FOLDER = new File("D:\\Users\\mzatt\\Projects\\FBF - 6502bf\\test");

	// Folder where binary (batch) files are stored
	private static final File BIN_FOLDER = new File("D:\\Users\\mzatt\\Projects\\FBF - 6502bf");

	private final static String CODE_NAME = Linker.CODE_NAME;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			boolean compileEmu = true;

			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-x"))
					compileEmu = false;
				else
					throw new IllegalArgumentException("Unrecognized parameter: " + args[i]);
			}

			if (compileEmu) {
				// delete and recompile the 6502 emulator
				execute(BIN_FOLDER, "fbf.bat", CODE_NAME);
				File emu = new File(BIN_FOLDER, CODE_NAME + ".bf");
				System.out.println("Recompiling emulator: " + emu.getCanonicalPath());
				if (!emu.exists())
					throw new Exception("6502 emulator did not compile properly.");
				System.out.println();
			}

			boolean error = false;
			for (File f : WORKING_FOLDER.listFiles(new FileFilter() {

				// TODO add 6502 functional test to this folder (and maybe remove other except
				// those that check SYSCALL
				@Override
				public boolean accept(File arg0) {
					return arg0.getName().toLowerCase().endsWith(".s");
				}

			})) {
				try {
					System.out.print(f.getName() + ":\t");
					if (test(f)) {
						System.out.println("OK");
					} else {
						System.out.println("FAILED");
						error = true;
					}
				} catch (Exception e) {
					System.out.println("FAILED");
					e.printStackTrace();
					error = true;
				}
			}

			if (error)
				System.exit(-1);
			else
				System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
			System.exit(-1);
		}
	}

	/**
	 * Tests a single asm file.
	 * 
	 * @param f File to test.
	 * @return true if test was OK, false otherwise.
	 */
	private static boolean test(File f) throws Exception {
		File out = new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".result"));

		// cleanup stuff from previous runs - MS DOS del does not work
		if (out.exists() && !out.delete())
			throw new IOException("Cannot delete result file: " + out.getCanonicalPath());

		// Assembles the file to test
		execute(BIN_FOLDER, "asm.bat", FileUtil.removeExtension(f.getCanonicalPath()));
		File executable = new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".exe"));
		if (!executable.exists())
			throw new Exception("File \"" + executable.getCanonicalPath() + "\" did not compile properly.");

		// Runs it and saves output to .result file
		ProcessBuilder pb = new ProcessBuilder(executable.getCanonicalPath());
		pb.directory(WORKING_FOLDER);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.to(out));
		int result = pb.start().waitFor();

		// Compare .result and .ref files
		byte[] ref = Files
				.readAllBytes(new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".ref")).toPath());
		byte[] res = Files.readAllBytes(out.toPath());
		if (!Arrays.equals(ref, res))
			throw new Exception("Output does dot match reference file.");

		// Cleanup
		new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".exe")).delete();
		new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".bf")).delete();
		new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".c")).delete();
		new File(WORKING_FOLDER, FileUtil.replaceExtension(f.getName(), ".txt")).delete();

		return (result == 0);
	}

	private static void printUsage() {
		System.out.println();
		System.out.println();
		System.out.println("Usage: java -jar " + TestHarness.class.getSimpleName() + ".jar [-x]");
		System.out.println();
		System.out.println("    -x If provided, does NOT recompile the emulator before running the tests.");
		System.out.println();
	}

	/**
	 * Executes a command and return its exit code.
	 */
	private static int execute(File workingFolder, String cmd, String... params)
			throws InterruptedException, IOException {

		return execute(workingFolder, BIN_FOLDER, cmd, params);
	}

	/**
	 * Executes a command and return its exit code.
	 * 
	 * @param workingFolder Working folder where the command is executed, leave it
	 *                      null to use default folder.
	 * @param binFolder     Folder where the command is, leave it null to use
	 *                      default folder.
	 * @param cmd           Command to execute.
	 * @params params Command parameters.
	 * 
	 * @return Command exit code.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static int execute(File workingFolder, File binFolder, String cmd, String... params)
			throws InterruptedException, IOException {

		String[] cmdArray = new String[params.length + 1];
		if (binFolder == null) {
			cmdArray[0] = cmd;
		} else {
			File cmdFile = new File(binFolder, cmd);
			cmdArray[0] = cmdFile.getCanonicalPath();
		}
		System.arraycopy(params, 0, cmdArray, 1, params.length);

		// System.out.print("Executing: ");
		// for (String p : cmdArray)
		// System.out.print(p + " ");
		// System.out.println("[in folder " + workingFolder.getCanonicalPath() + "]");

		ProcessBuilder pb = new ProcessBuilder(cmdArray);
		if (workingFolder != null)
			pb.directory(workingFolder);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.to(new File(workingFolder, "out.txt")));
		return pb.start().waitFor();
	}
}
