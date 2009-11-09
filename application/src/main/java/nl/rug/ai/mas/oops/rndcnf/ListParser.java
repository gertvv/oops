/**
  * This program (working title: MAS Prover) is an automated tableaux prover
  * for epistemic logic (S5n).
  * Copyright (C) 2007  Elske van der Vaart and Gert van Valkenhoef

  * This program is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version 2 as published
  * by the Free Software Foundation.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.

  * You should have received a copy of the GNU General Public License along
  * with this program; if not, write to the Free Software Foundation, Inc.,
  * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  */

package nl.rug.ai.mas.oops.rndcnf;

import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class ListParser {
	private static Pattern s_rx = Pattern.compile("(\\[|\\]|,|[0-9]+)");

	public static List<Integer> parseList1(String str)
	throws IllegalArgumentException {
		Matcher tk = tokenizer(str);
		List<Integer> list = parseList1(tk, 0);
		if (tk.find()) {
			throw new IllegalArgumentException(
				"Trailing characters: " + tk.group());
		}
		return list;
	}
	
	public static List<List<Integer>> parseList2(String str) {
		Matcher tk = tokenizer(str);
		List<List<Integer>> list = parseList2(tk, 0);
		if (tk.find()) {
			throw new IllegalArgumentException(
				"Trailing characters: " + tk.group());
		}
		return list;
	}

	public static List<List<List<Integer>>> parseList3(String str) {
		Matcher tk = tokenizer(str);
		List<List<List<Integer>>> list = parseList3(tk, 0);
		if (tk.find()) {
			throw new IllegalArgumentException(
				"Trailing characters: " + tk.group());
		}
		return list;
	}

	private static Matcher tokenizer(String str) {
        return s_rx.matcher(str);
	}

	private static List<Integer> parseList1(Matcher tk, int level)
	throws IllegalArgumentException {
		List<Integer> list = new ArrayList<Integer>();
		boolean start = false;
		boolean sep = false;
        while (tk.find()) {
			if (level == 0) {
				if (tk.group().equals("[")) {
					level++;
				} else {
					throw new IllegalArgumentException(
						"Unexpected token " + tk.group() + ", expected [");
				}
			} else {
				if (tk.group().equals("[")) {
					throw new IllegalArgumentException(
						"Unexpected token [, nesting too deep");
				} else if (tk.group().equals("]")) {
					if (sep == false) {
						return list;
					} else {
						throw new IllegalArgumentException(
							"Unexpected token ], expected a number");
					}
				} else if (tk.group().equals(",")) {
					if (start == true && sep == false) {
						sep = true;
					} else {
						throw new IllegalArgumentException(
							"Unexpected token \",\", expected a number");
					}
				} else {
					Integer i = new Integer(tk.group());
					if (start == true && sep == false) {
						throw new IllegalArgumentException(
							"Unexpected token " + tk.group() +
							", expected \",\"");
					}
					start = true;
					sep = false;
					list.add(i);
				}
			}
        }
		throw new IllegalArgumentException("Unexpected end of String");
	}

	private static List<List<Integer>> parseList2(Matcher tk, int level)
	throws IllegalArgumentException {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		boolean start = false;
		boolean sep = false;
        while (tk.find()) {
			if (level == 0) {
				if (tk.group().equals("[")) {
					level++;
				} else {
					throw new IllegalArgumentException(
						"Unexpected token " + tk.group() + ", expected [");
				}
			} else {
				if (tk.group().equals("[")) {
					if (start == true && sep == false) {
						throw new IllegalArgumentException(
							"Unexpected token [, expected \",\"");
					}
					start = true;
					sep = false;
					list.add(parseList1(tk, 1));
				} else if (tk.group().equals("]")) {
					if (sep == false) {
						return list;
					} else {
						throw new IllegalArgumentException(
							"Unexpected token ], expected a list");
					}
				} else if (tk.group().equals(",")) {
					if (start == true && sep == false) {
						sep = true;
					} else {
						throw new IllegalArgumentException(
							"Unexpected token \",\", expected a list");
					}
				} else {
					throw new IllegalArgumentException(
							"Unexpected token " + tk.group());
				}
			}
        }
		throw new IllegalArgumentException("Unexpected end of String");
	}

	private static List<List<List<Integer>>> parseList3(Matcher tk, int level)
	throws IllegalArgumentException {
		List<List<List<Integer>>> list = new ArrayList<List<List<Integer>>>();
		boolean start = false;
		boolean sep = false;
        while (tk.find()) {
			if (level == 0) {
				if (tk.group().equals("[")) {
					level++;
				} else {
					throw new IllegalArgumentException(
						"Unexpected token " + tk.group() + ", expected [");
				}
			} else {
				if (tk.group().equals("[")) {
					if (start == true && sep == false) {
						throw new IllegalArgumentException(
							"Unexpected token [, expected \",\"");
					}
					start = true;
					sep = false;
					list.add(parseList2(tk, 1));
				} else if (tk.group().equals("]")) {
					if (sep == false) {
						return list;
					} else {
						throw new IllegalArgumentException(
							"Unexpected token ], expected a list");
					}
				} else if (tk.group().equals(",")) {
					if (start == true && sep == false) {
						sep = true;
					} else {
						throw new IllegalArgumentException(
							"Unexpected token \",\", expected a list");
					}
				} else {
					throw new IllegalArgumentException(
							"Unexpected token " + tk.group());
				}
			}
        }
		throw new IllegalArgumentException("Unexpected end of String");
	}
}
