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

import nl.rug.ai.mas.oops.tableau.Tableau;
import nl.rug.ai.mas.oops.tableau.TableauEvent;
import nl.rug.ai.mas.oops.tableau.TableauFinishedEvent;
import nl.rug.ai.mas.oops.tableau.TableauStartedEvent;

import java.io.IOException;
import java.awt.FontFormatException;

public class TableauObserverSwing extends TableauObserverBase {
	private JFrame d_frame; // root window
	private int d_defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE;

	public TableauObserverSwing() throws IOException, FontFormatException {
		super();
	}

	private void initFrame() {
		d_frame = new JFrame("Tableau Observer");
		JScrollPane panel = new JScrollPane(getTree());
		d_frame.add(panel);
		d_frame.pack();
		d_frame.setSize(800, 600);
		d_frame.setVisible(true);
		d_frame.setDefaultCloseOperation(d_defaultCloseOperation);
	}

	public void setDefaultCloseOperation(int action) {
		d_defaultCloseOperation = action;
		if (d_frame != null) {
			d_frame.setDefaultCloseOperation(d_defaultCloseOperation);
		}
	}
	
	@Override
	public void update(Tableau t, TableauEvent e) {
		if (e instanceof TableauStartedEvent) {
			initFrame();
		} else if (e instanceof TableauFinishedEvent) {
			d_frame = null;
		}
		
		super.update(t, e);
	}
}
