/**
 * Wraps a PrintWriter making sure the data printed is formatted.
 */
package org.mzattera.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author mzatt
 *
 */
public final class FormattedPrintWriterWrapper implements AutoCloseable {

	private PrintWriter writer = null;

	private int lineSize = -1;
	private int available = -1;

	public FormattedPrintWriterWrapper(String outputFileName, String encoding, int lineSize)
			throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter(outputFileName, encoding);
		this.lineSize = lineSize;
		if (this.lineSize <= 0)
			throw new IllegalArgumentException("Line size must be > 0: " + this.lineSize);
		available = lineSize;
	}

	public FormattedPrintWriterWrapper print(String s) {
		s = bsFilter(s);
		while (s.length() > 0) {
			if (s.length() <= available) {
				writer.print(s);
				available -= s.length();
				s = "";
			} else {
				writer.print(s.substring(0, available));
				s = s.substring(available);
				available = 0;
			}
			if (available == 0) {
				writer.println();
				available = lineSize;
			}
		}
		return this;
	}

	private static final char[] ALLOWED = { '>', '<', '+', '-', '[', ']', '.', ',' };

	private String bsFilter(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			for (int j = 0; j < ALLOWED.length; ++j)
				if (c == ALLOWED[j]) {
					sb.append(c);
					break;
				}
		}
		return sb.toString();
	}

	@Override
	public void close() {
		if (writer != null)
			writer.close();
	}
}
