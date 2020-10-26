/**
 * 
 */
package org.mzattera.bf6502.codegen;

import org.mzattera.bf6502.stats.ReorganizeOpcodes;
import org.mzattera.util.FileUtil;

/**
 * Prints a set of IF...THEN...ELSE statements to execute opcodes using proper
 * blocks.
 * 
 * This in an attempt to speed up execution, since we do not have switch
 * statement in FBF.
 * 
 * The result is used to create V3 version.
 * 
 * @author Massimiliano Zattera
 *
 */
public class CreateV3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			FileUtil.write(ifThenElse(0, 0), "D:\\out.txt", "ASCII");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * 
	 * @param i
	 * @param indent indentation level
	 * @return The if_then_else statement for the i-th opcode in
	 *         ReorganizeOpcodes.instructions[]
	 */
	private static String ifThenElse(int i, int indent) {
		if (i >= ReorganizeOpcodes.instructions.length)
			return "-- Unrecognized opcode\nset exception 1\n";
		else {
			StringBuffer result = new StringBuffer();
			int opcode = ReorganizeOpcodes.instructions[i].opcode;

//			result.append(spaces(indent)).append("set _ifCond ").append(opcode).append('\n');
//			result.append(spaces(indent)).append("_if_IR_eq_cond\n");
//			result.append(spaces(indent + 1)).append(CreateV1_1.getBlockName(opcode)).append('\n');
//			result.append(spaces(indent)).append("_else\n");
//			result.append(ifThenElse(i + 1, indent + 2));
//			result.append(spaces(indent)).append("_endif\n");
			result.append("set _ifCond ").append(opcode).append('\n');
			result.append("_if_IR_eq_cond\n");
			result.append("  ").append(CreateV1_1.getBlockName(opcode)).append('\n');
			result.append("_else\n");
			result.append(ifThenElse(i + 1, indent + 2));
			result.append("_endif\n");

			return result.toString();
		}
	}

//	private static String spaces(int indent) {
//		char[] c = new char[indent];
//		Arrays.fill(c, ' ');
//		return new String(c);
//	}
}