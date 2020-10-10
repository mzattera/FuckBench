/**
 * 
 */
package org.mzattera.bf6502.codegen;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mzattera.bf6502.stats.ReorganizeOpcodes;
import org.mzattera.bf6502.stats.ReorganizeOpcodes.Instruction;

/**
 * Takes original (V1) version of the emulator, extracts code for each opcode
 * from the body of "ifeq IR NN ... end" statements, creates a block out of it,
 * replace the code in the ifeq statement with a call to the block, finally
 * prints the new code, beginning with new blocks.
 * 
 * @author Massimiliano Zattera
 *
 */
public class ExtractOpcodesCode {

	private static final File SOURCE = new File("D:\\Users\\mzatt\\Projects\\FBF - 6502bf\\6502bf_v1.fbf");

	private static final Pattern IF_BEGIN = Pattern.compile("(\\s*)ifeq\\s+IR\\s+([0-9]+)");
	private static final Pattern IF_END = Pattern.compile("(\\s*)end");

	private static final Pattern INDENT_BEGIN = Pattern.compile("^\\s*(ifeq|ifnoteq|uneq|#block)\\s+");
	private static final Pattern INDENT_END = Pattern.compile("^\\s*(end|#endblock)(\\s*|\\n)");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			// FBF sources for all #block corresponding to opcodes
			StringBuilder blocks = new StringBuilder();

			// Original source code
			List<String> original = Files.readAllLines(SOURCE.toPath());

			// New source code
			List<String> newCode = new ArrayList<>(original.size());

			// Takes opcode check source code from the emulator.
			int i = 0;
			for (i = 0; i < original.size(); ++i) {
				Matcher m = IF_BEGIN.matcher(original.get(i));

				if (m.find()) {
					// found one opcode check
					List<String> c = new ArrayList<>();
					int tab = m.group(1).length(); // we assume the end is aligned with the if
					int opcode = Integer.parseInt(m.group(2));

					// copy all text down to matching end into sb
					for (++i; i < original.size(); ++i) {
						m = IF_END.matcher(original.get(i));
						if (m.matches() && (m.group(1).length() == tab)) {
							// this is the code for current opcode, let's save it
							blocks.append(blockCode(opcode, c)).append("\n\n");

							// the new code will have only an if and an invocation to a code block for the
							// code
							newCode.add(spaces(tab) + "ifeq IR " + opcode);
							newCode.add(spaces(tab) + "  " + getBlockName(opcode));
							newCode.add(spaces(tab) + "end");
							break;
						} else {
							c.add(original.get(i));
						}
					}
				} else {
					// not an opcode row, just add it to the output
					newCode.add(original.get(i));
				}
			}
			
			// Outputs FBF code for each opcode as separate #block
			System.out.println(blocks.toString());

			// Outputs new FBF code for the emulator (if bodies replaced by calls to corresponding blocks)
			System.out.println(format(newCode));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Returns a FBF block with code for given opcode.
	 * 
	 * @param key 6502 opcode
	 * @param value the FBF code to emulate corresponding instruction
	 */
	private static String blockCode(Integer key, List<String> value) {
		StringBuffer result = new StringBuffer("#block ");
		result.append(getBlockName(key)).append("\n");
		result.append(format(value, 2));
		result.append("#endblock");

		return result.toString();
	}

	/**
	 * Returns name of FBF code block for one given opcode.
	 */
	private static String getBlockName(int opcode) {
		Instruction i = ReorganizeOpcodes.instructionMap.get(opcode);
		return i.mnemonic.toUpperCase() + "_"
				+ i.addrMode.toLowerCase().replace("\\(", "").replace("\\)", "").replace(',', '_');
	}

	private static String format(List<String> code) {
		return format(code, 0);
	}

	/**
	 * Properly indents FBF source code.
	 * 
	 * @param code   The code as a list fo rows.
	 * @param indent How much is the indentation for the whole code.
	 * @return a String with the given code, properly indented.
	 */
	private static String format(List<String> code, int indent) {
		StringBuffer result = new StringBuffer();

		for (String line : code) {
			Matcher m = INDENT_BEGIN.matcher(line);
			if (m.find()) {
				result.append(spaces(indent)).append(line.trim()).append("\n");
				indent += 2;
			} else {
				m = INDENT_END.matcher(line);
				if (m.find())
					indent -= 2;
				result.append(spaces(indent)).append(line.trim()).append("\n");
			}
		}

		return result.toString();
	}

	private static String spaces(int indent) {
		char[] c = new char[indent];
		Arrays.fill(c, ' ');
		return new String(c);
	}
}