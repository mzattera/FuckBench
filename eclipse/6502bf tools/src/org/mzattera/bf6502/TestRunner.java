/**
 * This will rebuild assets and run all available tests.
 */
package org.mzattera.bf6502;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.Arrays;

import org.mzattera.util.FileUtil;

/**
 * @author Massimliano "Maxi" Zattera
 *
 */
public class TestRunner {

	// Home folder for FuckBench
	private File fbHome = null;

	// bin folder for FuckBench
	private File fbBin = null;

	// cc65 folder for FuckBench
	private File fbLib = null;

	// test folder for FuckBench
	private File fbTest = null;

	// 6502 test folder for FuckBench
	private File fb6502Test = null;

	// Should we build the emulator before the tests?
	private boolean compileEmu = true;

	// Should we build the cc65 libraries before the tests?
	private boolean compileLib = true;

	// Should we run 6502 functional tests?
	private boolean runFuncTest = true;

	// Should we run tests in test folder?
	private boolean runTests = true;

	// Should we print output?
	private boolean verbose = false;

	public TestRunner(File fbHome2, boolean compileEmu2, boolean compileLib2, boolean runFuncTest2, boolean runTests2, boolean verbose2) {
		fbHome = fbHome2;
		fbBin = new File(fbHome, "bin");
		fbTest = new File(fbHome, "test");
		fb6502Test = new File(fbHome, "\\test\\ca65");
		fbLib = new File(fbHome, "cc65");
		compileEmu = compileEmu2;
		compileLib = compileLib2;
		runFuncTest = runFuncTest2;
		runTests = runTests2;
		verbose = verbose2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Should we build the emulator before the tests?
			boolean compileEmu = true;

			// Should we build the cc65 libraries before the tests?
			boolean compileLib = true;

			// Should we run 6502 functional tests?
			boolean runFuncTest = true;

			// Should we run tests in test folder?
			boolean runTests = true;

			// Should we print output?
			boolean verbose = false;

			if (args.length < 1) {
				throw new IllegalArgumentException("Too few parameters.");
			}
			for (int i = 1; i < args.length; i++) {
				if (args[i].equals("-e"))
					compileEmu = false;
				else if (args[i].equals("-l"))
					compileLib = false;
				else if (args[i].equals("-f"))
					runFuncTest = false;
				else if (args[i].equals("-t"))
					runTests = false;
				else if (args[i].equals("-v"))
					verbose = true;
				else
					throw new IllegalArgumentException("Unrecognized parameter: " + args[i]);
			}

			TestRunner instance = new TestRunner(new File(args[0]), compileEmu, compileLib, runFuncTest, runTests, verbose);
			if (instance.execute()) {
				System.out.println("Success.");
			} else {
				System.out.println("Test failed.");
			}

		} catch (Exception e) {
			System.out.println("Test failed (" + e.getMessage() + ")");
			System.exit(-1);
		}
	}

	private boolean execute() throws Exception {
		boolean ok = true;

		if (compileEmu) {
			// Rebuild 6502bf emulator
			File emu = new File(fbHome, Linker.CODE_NAME + ".bf");
			System.out.println("Rebuilding " + emu.getCanonicalPath() + " 6502 emulator...");
			executeCmd("FB_build_emu.bat");
			if (!CheckFile.isOK(emu))
				throw new Exception("6502 emulator did not build properly.");
			System.out.println("Success.");
			System.out.println();
		}

		if (runFuncTest) {
			// Build and run 6502 functional test
			File ft = new File(fb6502Test, "6502_functional_test.exe");
			System.out.println("Rebuilding " + ft.getCanonicalPath() + " funtional test...");
			executeCmd("FB_build_6502test.bat", "6502_functional_test");
			if (!CheckFile.isOK(ft))
				throw new Exception("6502 functional test did not build properly.");
			System.out.println("\tRunning 6502 funtional test...");
			if (!test(ft)) {
				throw new Exception("6502 functional test did not execute properly.");
			}
			System.out.println("Success.");
			System.out.println();
		}

		if (compileLib) {
			// Rebuild cc65 libraries
			File lib = new File(fbLib, Linker.CODE_NAME + ".lib");
			System.out.println("Rebuilding " + lib.getCanonicalPath() + " cc65 lbraries...");
			executeCmd("FB_build_lib.bat");
			if (!CheckFile.isOK(lib))
				throw new Exception("cc65 libraries did not build properly.");
			System.out.println("Success.");
			System.out.println();
		}

		if (runTests) {
			// Run tests in each of test subfolders
			System.out.println("Running tests in " + fbTest.getCanonicalPath() + "...");
			ok &= runTests(new File(fbTest, "bf"), ".bf", "FB_bf.bat");
			ok &= runTests(new File(fbTest, "fbf"), ".fbf", "FB_fbf.bat");
			ok &= runTests(new File(fbTest, "asm"), ".s", "FB_asm.bat");
			ok &= runTests(new File(fbTest, "c"), ".c", "FB_cl.bat");
			ok &= runTests(new File(fbTest, "java"), ".java", "FB_java.bat");
		}

		return ok;
	}

	/**
	 * Run all tests in a folder
	 * 
	 * @param folder folder where tests are located/
	 * @param ext    extension for test files
	 * @param filter buildCmd command used to build test file
	 * 
	 * @return true if all tests executed correctly
	 */
	private boolean runTests(File folder, String ext, String buildCmd) {
		boolean ok = true;

		for (File f : folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().toLowerCase().endsWith(ext) &&
				// Special case for auto-generated .c files
				!arg0.getName().toLowerCase().endsWith("_c.c");
			}
		})) {
			try {
				System.out.print(f.getName() + ":\t");
				if (runTest(buildCmd, f)) {
					System.out.println("Success");
				} else {
					System.out.println("Failed");
					ok = false;
				}
			} catch (Exception e) {
				System.out.println("Failed");
				e.printStackTrace(System.out);
				ok = false;
			}
		}

		return ok;
	}

	/**
	 * Builds and executes a single test.
	 * 
	 * @param buildCmd Command to build the test file
	 * @param f        test file itself
	 * 
	 * @return true if test executed correctly.
	 * @throws Exception
	 */
	private boolean runTest(String buildCmd, File f) throws Exception {
		File exe = new File(f.getParentFile(), FileUtil.replaceExtension(f.getName(), ".exe"));

		// Build test
		executeCmd(fbHome, fbBin, null, buildCmd, FileUtil.removeExtension(f.getCanonicalPath()));

		// Executes it
		if (!CheckFile.isOK(exe))
			throw new Exception("Cannot build test file.");
		return test(exe);
	}

	/**
	 * Tests a single .exe file.
	 * 
	 * @param f File to test.
	 * @return true if test was OK, false otherwise.
	 */
	private boolean test(File f) throws Exception {
		File out = new File(f.getParentFile(), FileUtil.replaceExtension(f.getName(), ".result"));
		File ref = new File(f.getParentFile(), FileUtil.replaceExtension(f.getName(), ".ref"));

		// cleanup stuff from previous runs - MS DOS del does not work
		if (out.exists() && !out.delete())
			throw new IOException("Cannot delete result file: " + out.getCanonicalPath());

		// Runs file and saves output to .result file
		executeCmd(out, f.getCanonicalPath());

		// Compare .result and .ref files
		byte[] refC = Files.readAllBytes(ref.toPath());
		byte[] outC = Files.readAllBytes(out.toPath());
		if (!Arrays.equals(refC, outC))
			return false;

		return true;
	}

	/**
	 * Executes a command and return its exit code.
	 * 
	 * @param workingFolder Working folder where the command is executed, leave it
	 *                      null to use default folder.
	 * @param binFolder     Folder where the command is, leave it null to use
	 *                      default folder.
	 * @param output        If not null, redirect output to this file.
	 * @param cmd           Command to execute.
	 * @params params Command parameters.
	 * 
	 * @return Command exit code.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int executeCmd(String cmd, String... params) throws InterruptedException, IOException {
		return executeCmd(null, null, null, cmd, params);
	}

	/**
	 * Executes a command and return its exit code.
	 * 
	 * @param workingFolder Working folder where the command is executed, leave it
	 *                      null to use default folder.
	 * @param binFolder     Folder where the command is, leave it null to use
	 *                      default folder.
	 * @param output        If not null, redirect output to this file.
	 * @param cmd           Command to execute.
	 * @params params Command parameters.
	 * 
	 * @return Command exit code.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int executeCmd(File output, String cmd, String... params) throws InterruptedException, IOException {
		return executeCmd(null, null, output, cmd, params);
	}

	/**
	 * Executes a command and return its exit code.
	 * 
	 * @param workingFolder Working folder where the command is executed, leave it
	 *                      null to use default folder.
	 * @param binFolder     Folder where the command is, leave it null to use
	 *                      default folder.
	 * @param output        If not null, redirect output to this file. Otherwise
	 *                      prints to System.out.
	 * @param cmd           Command to execute.
	 * @params params Command parameters.
	 * 
	 * @return Command exit code.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int executeCmd(File workingFolder, File binFolder, File output, String cmd, String... params)
			throws InterruptedException, IOException {

		String[] cmdArray = new String[params.length + 1];
		if (binFolder == null) {
			cmdArray[0] = cmd;
		} else {
			File cmdFile = new File(binFolder, cmd);
			cmdArray[0] = cmdFile.getCanonicalPath();
		}
		System.arraycopy(params, 0, cmdArray, 1, params.length);

		ProcessBuilder pb = new ProcessBuilder(cmdArray);
		if (workingFolder != null)
			pb.directory(workingFolder);

		pb.redirectErrorStream(true);
		if (output != null) {
			// Redirect output to file
			pb.redirectOutput(Redirect.to(output));
		} else if (verbose) {
			Process p = pb.start();

			// Capture and print output (output from batch file is otherwise lost try
			try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}

			return p.waitFor();
		}

		return pb.start().waitFor();
	}
}