package js.tinyvm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

import js.tools.ListReader;

/**
 * Processes classes.db & signatures.db file (under javavm C source folder) and
 * creates a set of jtools & javavm source file that contains list of "special"
 * classess and native method signatures.
 * 
 * b2fJ added automated creation and handling of "magic number" to verify that
 * JVM and jtools are synchronized. This was handled manually before.
 * 
 * @author tinyvm
 *
 */
public class GenerateConstants {
	static String iTinyVMHome = System.getProperty("tinyvm.home");
	static final String CLASSES = "SpecialClassConstants";
	static final String SIGNATURES = "SpecialSignatureConstants";

	public static void fatal(String aMsg) {
		System.err.println(aMsg);
		System.exit(1);
	}

	public static String insert_(String s, String sep) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer tokens = new StringTokenizer(s, sep, true);
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token.length() == 1 && sep.indexOf(token) != -1)
				sb.append("_");
			sb.append(token);
		}
		return sb.toString();
	}

	public static String hclassName(String aClassName) {
		return aClassName.replace('/', '_').toUpperCase();
	}

	public static String hsignatureName(String aSig) {
		aSig = insert_(aSig.replace('_', '-'), "-[;/()<>");
		return aSig.replace('-', '0').replace('[', '1').replace(';', '2').replace('/', '3').replace('(', '4')
				.replace(')', '5').replace('<', '6').replace('>', '7');
	}

	public static void generateClassConstants(Vector<String> aVec, File aHeaderFile, File aJavaFile) throws Exception {
		PrintWriter pHeaderOut = new PrintWriter(new FileWriter(aHeaderFile));
		PrintWriter pJavaOut = new PrintWriter(new FileWriter(aJavaFile));

		pHeaderOut.println("/**");
		pHeaderOut.println(" * Machine-generated file. Do not modify.");
		pHeaderOut.println(" */");
		pHeaderOut.println();
		pHeaderOut.println("#ifndef _SPECIALCLASSES_H");
		pHeaderOut.println("#define _SPECIALCLASSES_H");

		pJavaOut.println("package js.tinyvm;");
		pJavaOut.println("/**");
		pJavaOut.println(" * Machine-generated file. Do not modify.");
		pJavaOut.println(" */");
		pJavaOut.println("");
		pJavaOut.println("public interface " + CLASSES + " {");
		pJavaOut.println("  static final String[] CLASSES = {");
		try {
			int pSize = aVec.size();
			for (int i = 0; i < pSize; i++) {
				String pClassName = aVec.elementAt(i);

				pJavaOut.print("    \"" + pClassName + "\"");
				if (i < pSize - 1)
					pJavaOut.println(",");
				else
					pJavaOut.println();

				pHeaderOut.println("#define " + hclassName(pClassName) + " " + i);
			}
		} finally {
			pJavaOut.println("  };");
			pJavaOut.println("}");
			pJavaOut.close();

			pHeaderOut.println("#endif // _SPECIALCLASSES_H");
			pHeaderOut.close();
		}
	}

	public static void generateSignatureConstants(Vector<String> aVec, File aHeaderFile, File aJavaFile)
			throws Exception {
		PrintWriter pHeaderOut = new PrintWriter(new FileWriter(aHeaderFile));
		PrintWriter pJavaOut = new PrintWriter(new FileWriter(aJavaFile));

		pHeaderOut.println("/**");
		pHeaderOut.println(" * Machine-generated file. Do not modify.");
		pHeaderOut.println(" */");
		pHeaderOut.println();
		pHeaderOut.println("#ifndef _SPECIALSIGNATURES_H");
		pHeaderOut.println("#define _SPECIALSIGNATURES_H");

		pJavaOut.println("package js.tinyvm;");
		pJavaOut.println("/**");
		pJavaOut.println(" * Machine-generated file. Do not modify.");
		pJavaOut.println(" */");
		pJavaOut.println("");
		pJavaOut.println("public interface " + SIGNATURES + " {");
		pJavaOut.println("  static final String[] SIGNATURES = {");
		try {
			int pSize = aVec.size();
			for (int i = 0; i < pSize; i++) {
				String pSignature = aVec.elementAt(i);

				pJavaOut.print("    \"" + pSignature + "\"");
				if (i < pSize - 1)
					pJavaOut.println(",");
				else
					pJavaOut.println();

				pHeaderOut.println("#define " + hsignatureName(pSignature) + " " + i);
			}
		} finally {
			pJavaOut.println("  };");
			pJavaOut.println("}");
			pJavaOut.close();

			pHeaderOut.println("#endif // _SPECIALSIGNATURES_H");
			pHeaderOut.close();
		}
	}

	private static void generateMagicNumber(File aHeaderFile) throws IOException {
		PrintWriter pHeaderOut = new PrintWriter(new FileWriter(aHeaderFile));

		pHeaderOut.println("/**");
		pHeaderOut.println(" * Machine-generated file. Do not modify.");
		pHeaderOut.println(" */");
		pHeaderOut.println();
		pHeaderOut.println("#ifndef _VERSION_H");
		pHeaderOut.println("#define _VERSION_H");
		pHeaderOut.println();
		pHeaderOut.println("#define VERSION \"" + TinyVMConstants.VERSION + "\"");
		pHeaderOut.println("#define MAGIC_MASK " + String.format("0x%04X", TinyVMConstants.MAGIC_MASK));
		pHeaderOut.println();
		pHeaderOut.println("#endif // _VERSION_H");
		pHeaderOut.close();
	}

	public static void main(String[] aArg) throws Exception {
		if (iTinyVMHome == null)
			fatal("Error: Property tinyvm.home undefined");
		File pHome = new File(iTinyVMHome);
		if (!pHome.exists())
			fatal("Error: " + iTinyVMHome + " does not exist.");
		if (!pHome.isDirectory())
			fatal("Error: " + iTinyVMHome + " is not a directory.");
		File pVmSrc = new File(pHome, "javavm");
		File pClasses = new File(pVmSrc, "classes.db");
		File pSignatures = new File(pVmSrc, "signatures.db");
		File pClassIndexH = new File(pVmSrc, "specialclasses.h");
		File pSignaturesH = new File(pVmSrc, "specialsignatures.h");
		File pMagicH = new File(pVmSrc, "version.h");
		File pTools = new File(pHome, "java/tools");
		File pToolsJs = new File(pTools, "js");
		File pToolsJsTinyVM = new File(pToolsJs, "tinyvm");
		File pJavaClass = new File(pToolsJsTinyVM, CLASSES + ".java");
		File pJavaSig = new File(pToolsJsTinyVM, SIGNATURES + ".java");

		Vector<String> pClassVec = ListReader.loadStrings(pClasses);
		Vector<String> pSignatureVec = ListReader.loadStrings(pSignatures);

		generateClassConstants(pClassVec, pClassIndexH, pJavaClass);
		generateSignatureConstants(pSignatureVec, pSignaturesH, pJavaSig);
		generateMagicNumber(pMagicH);
	}
}