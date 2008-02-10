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

package nl.rug.ai.mas.oops.render;

import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.formula.Agent;
import nl.rug.ai.mas.oops.formula.NullAgent;
import nl.rug.ai.mas.oops.formula.Formula;

import java.util.Stack;

/**
 * Visit a Formula in order to generate an HTML description from it.
 */
class LabelHtml implements LabelVisitor {
	public static String ALPHA = "&#945;";
	public static String EMPTY = "&empty;";
	public static String LCEIL = "&lceil;";
	public static String RCEIL = "&rceil;";
	public static String AGENT(Agent a) {
		try {
			NullAgent na = (NullAgent) a;
			return "<sub>" + EMPTY + "</sub>";
		} catch (ClassCastException e) {
		}
		return "<sub>" + a.toString() + "</sub>";
	}

	private String d_html;

	public LabelHtml() {
		d_html = "";
	}

	public String getHtml() {
		return d_html;
	}

	public void visitLabelInstance(LabelInstance l) {
		World w = l.getWorld();
		String world = null;
		try {
			WorldInstance wi = (WorldInstance)w;
			Formula f = wi.getFormula();
			if (f == null) {
				world = ALPHA;
			} else {
				FormulaHtml fh = new FormulaHtml();
				f.accept(fh);
				world = LCEIL + fh.getHtml() + RCEIL;
			}
		} catch (ClassCastException e) {
			world = w.toString();
		}

		d_html += "." + world + AGENT(l.getAgent());
	}

	public void visitLabelReference(LabelReference l) {
		d_html = l.toString();
	}

	public void visitNullLabel(NullLabel l) {
		d_html = "&empty;";
	}
}

