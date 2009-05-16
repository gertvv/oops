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

package nl.rug.ai.mas.oops;

import java.util.*;
import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.render.TableauObserverSwing;
import nl.rug.ai.mas.oops.model.S5nModel;
import nl.rug.ai.mas.oops.model.ModelConstructingObserver;

import javax.swing.JFrame;
import nl.rug.ai.mas.oops.model.World;
import nl.rug.ai.mas.oops.model.Arrow;
import org.jgraph.JGraph;
import org.jgrapht.ext.JGraphModelAdapter;

/**
 * Test class for visual observation of the prover.
 */
public class ObserveProver {
	public static void main(String [] args)
	throws java.io.IOException, java.awt.FontFormatException {
		if (args.length != 1) {
			System.out.println("Please supply a formula on the command line.");
			return;
		}

		Context c = new Context();

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build(c);
		rules.addAll(ModalRuleFactory.build(c));

		Prover p = new Prover(rules, c);
		Formula f = null;
		try {
			f = p.parse(args[0]);
		} catch (TableauErrorException e) {
			System.out.println(e);
			System.exit(1);
		}

		//p.getTableau().attachObserver(new SystemOutObserver());
		p.getTableau().attachObserver(new TableauObserverSwing());
		//OutputStream os = new FileOutputStream("out.svg");
		//p.getTableau().attachObserver(new TableauObserverSVG(os));
		
		S5nModel model = new S5nModel(c.getAgentIdMap().getAgentSet());
		p.getTableau().attachObserver(new ModelConstructingObserver(model));

		System.out.println("Context agents: " + c.getAgentIdMap());

		try {
			System.out.println(p.provable(f));
			//System.out.println(model);
			JGraphModelAdapter<World, Arrow> graphModel =
				new JGraphModelAdapter<World, Arrow>(
					model.constructMultigraph());
			JGraph jgraph = new JGraph(graphModel);
			JFrame frame = new JFrame("Model Observer");
			frame.add(jgraph);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setSize(800, 600);
			frame.setVisible(true);

			System.out.println(model.constructMultigraph());
			for (nl.rug.ai.mas.oops.model.World w : model.getWorlds()) {
				System.out.println(w + ": " + w.getValuation());
			}
		} catch (TableauErrorException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
}
