/**
 * 
 */
package org.mzattera.util;

import java.util.Arrays;

/**
 * Generic String Utilities
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class StringUtil {

	/**
	 * @return a string of n character c.
	 */
	public static String chars(int n, char c) {
		char[] r = new char[n];
		Arrays.fill(r, c);
		return new String(r);
	}

	/**
	 * @return a string of n spaces.
	 */
	public static String spaces(int n) {
		return chars(n, ' ');
	}

	/**
	 * @return a string of n tabs.
	 */
	public static String tabs(int n) {
		return chars(n, '\t');
	}
}
