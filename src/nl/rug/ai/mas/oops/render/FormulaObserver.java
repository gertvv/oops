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

import nl.rug.ai.mas.oops.tableau.Tableau;
import nl.rug.ai.mas.oops.tableau.TableauEvent;
import nl.rug.ai.mas.oops.tableau.TableauObserver;
import nl.rug.ai.mas.oops.tableau.NodeAddedEvent;
import nl.rug.ai.mas.oops.tableau.Node;
import nl.rug.ai.mas.oops.tableau.Label;

import nl.rug.ai.mas.oops.formula.Formula;

import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontFormatException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

public class FormulaObserver implements TableauObserver {
	private JPanel d_panel;
	private Font d_font;
	private static String s_font = "/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf";

	public FormulaObserver() throws IOException, FontFormatException {
		JFrame frame = new JFrame();
		d_panel = new JPanel();
		d_panel.setLayout(new BoxLayout(d_panel, BoxLayout.PAGE_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 250);
		frame.getContentPane().add(d_panel);
		frame.setVisible(true);

		File ff = new File(s_font);
		d_font = Font.createFont(Font.TRUETYPE_FONT, ff);
		d_font = d_font.deriveFont((float)12);
	}

	public void update(Tableau t, TableauEvent e) {
		try {
			NodeAddedEvent event = (NodeAddedEvent)e;
			Formula f = event.getNode().getFormula();
			Label l = event.getNode().getLabel();

			// get HTML for formula & label
			FormulaHtml fh = new FormulaHtml();
			f.accept(fh);
			LabelHtml lh = new LabelHtml();
			l.accept(lh);
			String html = "<html>" + lh.getHtml() + 
				"&nbsp;" + fh.getHtml() + "</html>";

			JLabel label = new JLabel(html);
			label.setFont(d_font);
			d_panel.add(label);
			d_panel.revalidate();
		} catch (ClassCastException ex) {
		}
	}
}
