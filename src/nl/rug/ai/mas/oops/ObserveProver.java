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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.render.TableauObserverSwing;
import nl.rug.ai.mas.oops.model.KripkeModel;
import nl.rug.ai.mas.oops.model.S5nModel;
import nl.rug.ai.mas.oops.model.ModelConstructingObserver;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nl.rug.ai.mas.oops.model.World;
import nl.rug.ai.mas.oops.model.Arrow;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
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

		SimpleProver p = SimpleProver.build();
		Formula f = null;
		try {
			f = p.parse(args[0]);
		} catch (TableauErrorException e) {
			System.out.println(e);
			System.exit(1);
		}

		//p.getTableau().attachObserver(new SystemOutObserver());
		TableauObserverSwing observer = new TableauObserverSwing();
		observer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.getTableau().attachObserver(new TableauObserverSwing());
		//OutputStream os = new FileOutputStream("out.svg");
		//p.getTableau().attachObserver(new TableauObserverSVG(os));
		
		Context c = p.getContext();
		S5nModel model = new S5nModel(c.getAgentIdMap().getAgentSet());
		p.getTableau().attachObserver(new ModelConstructingObserver(model));

		System.out.println("Context agents: " + c.getAgentIdMap());

		try {
			System.out.println(p.provable(f));
			//System.out.println(model);
			showModel(model);
		} catch (TableauErrorException e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public static void showModel(KripkeModel model) {
		if (model.getWorlds().isEmpty()) {
			JOptionPane.showMessageDialog(null, "No model was constructed");
			return;
		}
		
		JGraphModelAdapter<World, Arrow> graphModel =
			new JGraphModelAdapter<World, Arrow>(
				model.constructMultigraph());
		JGraph jgraph = new JGraph(graphModel);
		
		highlightMainWorld(model, graphModel, jgraph);
		
		JFrame frame = new JFrame("Model Observer");
		frame.add(jgraph);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	private static void highlightMainWorld(KripkeModel model,
			JGraphModelAdapter<World, Arrow> graphModel, JGraph jgraph) {
		if (model.getMainWorld() != null) {
			Map<GraphCell, Map<Object, Object>> nested =
				new HashMap<GraphCell, Map<Object, Object>>();
			Map<Object, Object> attributeMap = new HashMap<Object, Object>();
			GraphConstants.setBackground(attributeMap, Color.BLUE);
			nested.put(
					graphModel.getVertexCell(model.getMainWorld()),
					attributeMap);
			jgraph.getGraphLayoutCache().edit(nested);
		}
	}
}
