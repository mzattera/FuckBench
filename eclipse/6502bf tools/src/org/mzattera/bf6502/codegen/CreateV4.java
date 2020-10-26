/**
 * 
 */
package org.mzattera.bf6502.codegen;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Based on result from ReorganizeOpcode re-writes 6502bf execute_opcode.
 * 
 * It was used to move from V1_1 to V4 version of the emulator.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class CreateV4 {

	// How to reorganize code, from ReorganizeOpcode
	private static final String BLUEPRINT = "208,173,41,[,16,168,240,165,[,197,48,32,96,[,202,189,44,205,201,133,[,141,166,185,198,169,230,[,24,170,152,138,136,108,[,145,74,9,144,153,[,200,181,72,104,10,[,76,162,157,177,160,[,88,176,102,70,80,105,[,174,192,164,232,221,[,120,149,228,56,42,[,233,132,224,134,5,[,73,109,206,172,184,125,[,64,101,186,29,180,[,222,81,237,78,[,188,38,217,238,[,13,213,214,57,[,234,216,140,245,[,142,45,229,[,254,6,117,[,190,49,[,106,253,[,89,77,[,121,[,37,69,[,241,209,[,196,61,[,112,[,246,154,[,248,[,236,[,94,[,113,[,8,[,40,[,66,0,1,14,17,21,22,25,30,33,36,46,53,54,62,65,85,86,93,97,110,118,126,129,148,150,161,182,193,204,225,249,],],],],],],],],],],],],],],],],],],],],],],],],],],],],],],],],],],],]";

	private static final Pattern INDENT_BEGIN = Pattern.compile("(ifeq|ifneq|uneq)\\s+");
	private static final Pattern INDENT_END = Pattern.compile("end(\\s*|\\n)");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			// Re-builds code based on BLUEPRINT
			StringBuffer code = new StringBuffer();
			for (String step : BLUEPRINT.split(",")) {
				if (step.equals("[")) {
					code.append("ifeq exception 1\n");
				} else if (step.equals("]")) {
					code.append("end\n");
				} else {
					int opcode = Integer.parseInt(step);
					code.append("ifeq IR ").append(opcode).append('\n');
					code.append(CreateV1_1.getBlockName(opcode)).append('\n');
					code.append("end").append('\n');
				}
			}

			System.out.println(format(code.toString()));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static String format(String s) {
		StringBuffer code = new StringBuffer("#block execute_opcode\n");
		int indent = 2;

		for (String step : s.split("\\n")) {
			Matcher m = INDENT_BEGIN.matcher(step);
			if (m.find()) {
				code.append(spaces(indent)).append(step.trim()).append("\n");
				indent += 2;
			} else {
				m = INDENT_END.matcher(step);
				if (m.find())
					indent -= 2;
				code.append(spaces(indent)).append(step.trim()).append("\n");
			}
		}

		code.append("#endblock");
		return code.toString();
	}

	private static String spaces(int indent) {
		char[] c = new char[indent];
		Arrays.fill(c, ' ');
		return new String(c);
	}
}