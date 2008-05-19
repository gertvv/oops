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

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.io.IOException;
import java.awt.FontFormatException;

public class FormulaObserverSwing extends FormulaObserver {
	private JFrame d_frame; // root window

	public FormulaObserverSwing() throws IOException, FontFormatException {
		super();
		d_frame = new JFrame("Tableau Observer");
		JScrollPane panel = new JScrollPane(getTree());
		d_frame.add(panel);
		d_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		d_frame.pack();
		d_frame.setSize(800, 600);
		d_frame.setVisible(true);
	}
}
