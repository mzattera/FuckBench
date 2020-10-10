/**
 * 
 */
package org.mzattera.bf6502.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Based on opcodes frequencies from CountOpCodes, runs a Montecarlo simulation
 * of 6502 code. It tests different ways to put the (exception == 1) check to
 * see which one minimizes the number of checks being executed.
 * 
 * @author Massimiliano Zattera
 *
 */
public class ReorganizeOpcodes {

	private static final int RUNS = 10_000_000;

	private static final Random rnd = new Random(System.currentTimeMillis());

	private static final double[] F = { 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8 };
	private static final int[] N = { 2, 3, 4, 5, 6 };

	public static class Instruction {
		@Override
		public String toString() {
			return "OpCode [code=" + opcode + ", mnemonic=" + mnemonic + ", addrMode=" + addrMode + ", freq=" + freq
					+ "]";
		}

		public int opcode;
		public double freq; // Frequency this is executed
		public String mnemonic;
		public String addrMode;

		Instruction(int opcode, double freq, String mnemonic, String addrMode) {
			this.opcode = opcode;
			this.freq = freq;
			this.mnemonic = mnemonic;
			this.addrMode = addrMode;
		}
	}

	// 6502 instructions.
	// These will be sorted in order by decreasing frequency.
	// Frequency data has been obtained by running a C64 emulator and logging
	// opcodes for a couple games (see CountOpcodes).
	public final static Instruction[] instructions = { new Instruction(66, 0.0, "SYSCALL", ""),
			new Instruction(0, 0.00000000, "brk", "implied"), new Instruction(1, 0.00000000, "ora", "(ind,x)"),
			new Instruction(5, 0.00039380, "ora", "zp"), new Instruction(6, 0.00004262, "asl", "zp"),
			new Instruction(8, 0.00000004, "php", "implied"), new Instruction(9, 0.00536317, "ora", "imm"),
			new Instruction(10, 0.00386322, "asl", "accum"), new Instruction(13, 0.00011547, "ora", "abs"),
			new Instruction(14, 0.00000000, "asl", "abs"), new Instruction(16, 0.07342364, "bpl", "relative"),
			new Instruction(17, 0.00000000, "ora", "(ind),y"), new Instruction(21, 0.00000000, "ora", "zp,x"),
			new Instruction(22, 0.00000000, "asl", "zp,x"), new Instruction(24, 0.00868059, "clc", "implied"),
			new Instruction(25, 0.00000000, "ora", "abs,y"), new Instruction(29, 0.00021750, "ora", "abs,x"),
			new Instruction(30, 0.00000000, "asl", "abs,x"), new Instruction(32, 0.02372333, "jsr", "abs"),
			new Instruction(33, 0.00000000, "and", "(ind,x)"), new Instruction(36, 0.00000000, "bit", "zp"),
			new Instruction(37, 0.00000618, "and", "zp"), new Instruction(38, 0.00013861, "rol", "zp"),
			new Instruction(40, 0.00000004, "plp", "implied"), new Instruction(41, 0.09620533, "and", "imm"),
			new Instruction(42, 0.00060762, "rol", "accum"), new Instruction(44, 0.01557069, "bit", "abs"),
			new Instruction(45, 0.00005220, "and", "abs"), new Instruction(46, 0.00000000, "rol", "abs"),
			new Instruction(48, 0.03309795, "bmi", "relative"), new Instruction(49, 0.00003407, "and", "(ind),y"),
			new Instruction(53, 0.00000000, "and", "zp,x"), new Instruction(54, 0.00000000, "rol", "zp,x"),
			new Instruction(56, 0.00073287, "sec", "implied"), new Instruction(57, 0.00007333, "and", "abs,y"),
			new Instruction(61, 0.00000219, "and", "abs,x"), new Instruction(62, 0.00000000, "rol", "abs,x"),
			new Instruction(64, 0.00029904, "rti", "implied"), new Instruction(65, 0.00000000, "eor", "(ind,x)"),
			new Instruction(69, 0.00000594, "eor", "zp"), new Instruction(70, 0.00151665, "lsr", "zp"),
			new Instruction(72, 0.00402000, "pha", "implied"), new Instruction(73, 0.00034311, "eor", "imm"),
			new Instruction(74, 0.00604843, "lsr", "accum"), new Instruction(76, 0.00319380, "jmp", "abs"),
			new Instruction(77, 0.00001709, "eor", "abs"), new Instruction(78, 0.00015950, "lsr", "abs"),
			new Instruction(80, 0.00131870, "bvc", "relative"), new Instruction(81, 0.00018031, "eor", "(ind),y"),
			new Instruction(85, 0.00000000, "eor", "zp,x"), new Instruction(86, 0.00000000, "lsr", "zp,x"),
			new Instruction(88, 0.00170582, "cli", "implied"), new Instruction(89, 0.00001902, "eor", "abs,y"),
			new Instruction(93, 0.00000000, "eor", "abs,x"), new Instruction(94, 0.00000040, "lsr", "abs,x"),
			new Instruction(96, 0.02372327, "rts", "implied"), new Instruction(97, 0.00000000, "adc", "(ind,x)"),
			new Instruction(101, 0.00024306, "adc", "zp"), new Instruction(102, 0.00158218, "ror", "zp"),
			new Instruction(104, 0.00402000, "pla", "implied"), new Instruction(105, 0.00129940, "adc", "imm"),
			new Instruction(106, 0.00003095, "ror", "accum"), new Instruction(108, 0.00683388, "jmp", "(abs)"),
			new Instruction(109, 0.00033520, "adc", "abs"), new Instruction(110, 0.00000000, "ror", "abs"),
			new Instruction(112, 0.00000169, "bvs", "relative"), new Instruction(113, 0.00000020, "adc", "(ind),y"),
			new Instruction(117, 0.00003936, "adc", "zp,x"), new Instruction(118, 0.00000000, "ror", "zp,x"),
			new Instruction(120, 0.00093840, "sei", "implied"), new Instruction(121, 0.00001397, "adc", "abs,y"),
			new Instruction(125, 0.00030540, "adc", "abs,x"), new Instruction(126, 0.00000000, "ror", "abs,x"),
			new Instruction(129, 0.00000000, "sta", "(ind,x)"), new Instruction(132, 0.00047174, "sty", "zp"),
			new Instruction(133, 0.01294920, "sta", "zp"), new Instruction(134, 0.00045884, "stx", "zp"),
			new Instruction(136, 0.00726055, "dey", "implied"), new Instruction(138, 0.00736292, "txa", "implied"),
			new Instruction(140, 0.00006301, "sty", "abs"), new Instruction(141, 0.01220369, "sta", "abs"),
			new Instruction(142, 0.00005381, "stx", "abs"), new Instruction(144, 0.00520453, "bcc", "relative"),
			new Instruction(145, 0.00648948, "sta", "(ind),y"), new Instruction(148, 0.00000000, "sty", "zp,x"),
			new Instruction(149, 0.00091290, "sta", "zp,x"), new Instruction(150, 0.00000000, "stx", "zp,y"),
			new Instruction(152, 0.00780794, "tya", "implied"), new Instruction(153, 0.00510715, "sta", "abs,y"),
			new Instruction(154, 0.00000072, "txs", "implied"), new Instruction(157, 0.00244461, "sta", "abs,x"),
			new Instruction(160, 0.00218499, "ldy", "imm"), new Instruction(161, 0.00000000, "lda", "(ind,x)"),
			new Instruction(162, 0.00310974, "ldx", "imm"), new Instruction(164, 0.00101537, "ldy", "zp"),
			new Instruction(165, 0.04647106, "lda", "zp"), new Instruction(166, 0.01180632, "ldx", "zp"),
			new Instruction(168, 0.05060894, "tay", "implied"), new Instruction(169, 0.00911923, "lda", "imm"),
			new Instruction(170, 0.00790536, "tax", "implied"), new Instruction(172, 0.00031011, "ldy", "abs"),
			new Instruction(173, 0.09677488, "lda", "abs"), new Instruction(174, 0.00112523, "ldx", "abs"),
			new Instruction(176, 0.00162856, "bcs", "relative"), new Instruction(177, 0.00226987, "lda", "(ind),y"),
			new Instruction(180, 0.00020778, "ldy", "zp,x"), new Instruction(181, 0.00412640, "lda", "zp,x"),
			new Instruction(182, 0.00000000, "ldx", "zp,y"), new Instruction(184, 0.00030568, "clv", "implied"),
			new Instruction(185, 0.01162410, "lda", "abs,y"), new Instruction(186, 0.00023088, "tsx", "implied"),
			new Instruction(188, 0.00015147, "ldy", "abs,x"), new Instruction(189, 0.01713037, "lda", "abs,x"),
			new Instruction(190, 0.00003801, "ldx", "abs,y"), new Instruction(192, 0.00101976, "cpy", "imm"),
			new Instruction(193, 0.00000000, "cmp", "(ind,x)"), new Instruction(196, 0.00000233, "cpy", "zp"),
			new Instruction(197, 0.04563129, "cmp", "zp"), new Instruction(198, 0.01131339, "dec", "zp"),
			new Instruction(200, 0.00509922, "iny", "implied"), new Instruction(201, 0.01415587, "cmp", "imm"),
			new Instruction(202, 0.01824314, "dex", "implied"), new Instruction(204, 0.00000000, "cpy", "abs"),
			new Instruction(205, 0.01445541, "cmp", "abs"), new Instruction(206, 0.00031590, "dec", "abs"),
			new Instruction(208, 0.16867901, "bne", "relative"), new Instruction(209, 0.00000342, "cmp", "(ind),y"),
			new Instruction(213, 0.00009466, "cmp", "zp,x"), new Instruction(214, 0.00007552, "dec", "zp,x"),
			new Instruction(216, 0.00006951, "cld", "implied"), new Instruction(217, 0.00013509, "cmp", "abs,y"),
			new Instruction(221, 0.00100291, "cmp", "abs,x"), new Instruction(222, 0.00020408, "dec", "abs,x"),
			new Instruction(224, 0.00046514, "cpx", "imm"), new Instruction(225, 0.00000000, "sbc", "(ind,x)"),
			new Instruction(228, 0.00074024, "cpx", "zp"), new Instruction(229, 0.00004707, "sbc", "zp"),
			new Instruction(230, 0.00889951, "inc", "zp"), new Instruction(232, 0.00100557, "inx", "implied"),
			new Instruction(233, 0.00058663, "sbc", "imm"), new Instruction(234, 0.00006955, "nop", "implied"),
			new Instruction(236, 0.00000060, "cpx", "abs"), new Instruction(237, 0.00017757, "sbc", "abs"),
			new Instruction(238, 0.00012354, "inc", "abs"), new Instruction(240, 0.04978075, "beq", "relative"),
			new Instruction(241, 0.00000409, "sbc", "(ind),y"), new Instruction(245, 0.00006063, "sbc", "zp,x"),
			new Instruction(246, 0.00000076, "inc", "zp,x"), new Instruction(248, 0.00000062, "sed", "implied"),
			new Instruction(249, 0.00000000, "sbc", "abs,y"), new Instruction(253, 0.00001948, "sbc", "abs,x"),
			new Instruction(254, 0.00004307, "inc", "abs,x") };
	static {
		// Sort opcodes by decreasing frequency.
		Arrays.sort(instructions, new Comparator<Instruction>() {

			@Override
			public int compare(Instruction o1, Instruction o2) {
				return -Double.valueOf(o1.freq).compareTo(o2.freq);
			}
		});
	}

	// Maps opcode into corresponding Instruction
	public final static Map<Integer, Instruction> instructionMap = new HashMap<>();
	static {
		for (Instruction i : instructions)
			instructionMap.put(i.opcode, i);
	}

	// cumulated[i] holds the accumulated frequencies of instructions[0-i].
	private final static double[] cumulated = new double[instructions.length];
	static {
		// compute accumulated frequencies.
		double cf = 0.0;
		for (int i = 0; i < instructions.length; ++i) {
			cf += instructions[i].freq;
			cumulated[i] = cf;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			double best = Double.MAX_VALUE;
			int bestN = -1;
			double bestF = -1.0;
			String bestPattern = null;

			// Simulate execution woth different thresholds
			for (int i = 0; i < N.length; ++i) {
				for (int j = 0; j < F.length; ++j) {
					String checkPattern = split(0, instructions.length - 1, N[i], F[j]);
					double d = simulate(checkPattern);
					System.out.println(N[i] + ";" + F[j] + ";" + d);
					if (d < best) {
						best = d;
						bestN = N[i];
						bestF = F[j];
						bestPattern = checkPattern;
					}
				}
			}

			System.out.println("\n\nBest min=" + bestN + " threshold=" + bestF);
			System.out.println("Best check pattern:");
			System.out.println(bestPattern);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	//
	/**
	 * Based on opcode frequencies, decides where to check if the opcode to execute
	 * was found (exception ==1). This is a recursive function.
	 * 
	 * @param begin     position where to start the split of the 6502 instruction
	 *                  array (first call should use 0).
	 * @param end       position where to stop the split of the 6502 instruction
	 *                  array (first call should use index of last instruction).
	 * @param min       minimum number of instructions in a [] block.
	 * @param threshold starting from begin, check opcodes until you have reached
	 *                  this percentage threshold of execution frequencies, then put
	 *                  a [.
	 * @return a string that lists opcodes to execute and [ ] representing
	 *         if(exception==1)then{ } blocks.
	 */
	private static String split(int begin, int end, int min, double threshold) {
		if ((end - begin) < min)
			return flat(begin, end);

		// total execution frequency for opcodes in range
		double tf = 0.0;
		for (int i = begin; i <= end; ++i)
			tf += instructions[i].freq;

		double pf = 0.0; // cumulated execution frequency of instructions so far
		for (int i = begin; i <= end - 3; ++i) {
			pf += instructions[i].freq;
			if (pf > tf * threshold) {
				// we reached threshold, time to insert a (execution==1) check
				return split(begin, i, min, threshold) + "[," + split(i + 1, end, min, threshold) + "],";
			}
		}

		return flat(begin, end);
	}

	/**
	 * Used by split() to create a list of opcodes to execute.
	 * 
	 * @param begin starting point in instruction array.
	 * @param end   end starting point in instruction array.
	 * @return a string suitable for split() with all opcodes between begin and end.
	 */
	private static String flat(int begin, int end) {
		StringBuffer sb = new StringBuffer();
		for (int i = begin; i <= end; ++i)
			sb.append(instructions[i].opcode).append(',');
		return sb.toString();
	}

	/**
	 * Simulate execution in case 6502 opcodes are arranges as provided in split
	 * 
	 * @param split Representation of 6502 instruction, including check points for
	 *              (exception==1), as returned by split()
	 * @return average number of checks performed to execute RUNS opcodes.
	 */
	private static double simulate(String split) {

		String[] steps = split.split(",");

		// steps to execute for the simulation.
		// each steps is either a) one 6502 opcode
		// b) Integer.MIN_VALUE to denote ] and
		// c) -N a replacer for [ meaning that if the IF closes at step N
		List<Integer> l = new ArrayList<>();

		Stack<Integer> stack = new Stack<>();

		// "compiles"
		for (int i = steps.length - 1; i >= 0; --i) {
			if (steps[i].equals("]")) {
				// push in the stack the closing address for the loop
				stack.push(i);
				l.add(0, Integer.MIN_VALUE);
			} else if (steps[i].equals("[")) {
				// pop the closing address of this IF from the stack
				l.add(0, -stack.pop());
			} else {
				// put the opcode
				l.add(0, Integer.parseInt(steps[i]));
			}
		}

		int[] isteps = new int[l.size()];
		for (int i = 0; i < isteps.length; ++i) {
			isteps[i] = l.get(i);
		}

		// calculates average number of checks over RUNS executions
		double t = 0.0;
		for (int i = 0; i < RUNS; ++i)
			t += run(isteps);

		return t / RUNS;
	}

	/**
	 * Executes one run of isteps, using random opcode as created from simulate()
	 * and returns number of check performed.
	 */
	private static double run(int[] isteps) {
		// Pick randomly an opcode that should be executed
		int opcode = 66;
		double d = rnd.nextDouble();
		for (int i = 0; i < cumulated.length; ++i) {
			if (cumulated[i] >= d) {
				opcode = instructions[i].opcode;
				break;
			}
		}

		boolean executed = false;
		int n = 0;
		for (int i = 0; i < isteps.length;) {
			if (isteps[i] < 0) {
				if (isteps[i] == Integer.MIN_VALUE) { // ] do nothing
					++i;
					continue;
				} else { // [
					++n;
					if (executed) {
						// the IF fails, jump to corresponding ]
						i = -isteps[i];
					} else {
						// the IF succeeds, continue
						++i;
					}
				}
			} else {
				// this is a check for an opcode
				++n;
				if (!executed) {
					executed = (opcode == isteps[i]);
				}
				++i;
			}
		}

		return n;
	}
}
