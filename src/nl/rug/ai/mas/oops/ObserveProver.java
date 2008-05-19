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
import nl.rug.ai.mas.oops.render.FormulaObserverSwing;
import nl.rug.ai.mas.oops.render.FormulaObserverSVG;
import nl.rug.ai.mas.oops.model.S5nModel;
import nl.rug.ai.mas.oops.model.ModelConstructingObserver;

import java.io.OutputStream;
import java.io.FileOutputStream;

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
		p.getTableau().attachObserver(new FormulaObserverSwing());
		//OutputStream os = new FileOutputStream("out.svg");
		//p.getTableau().attachObserver(new FormulaObserverSVG(os));
		
		S5nModel model = new S5nModel(c.getAgentIdMap().getAgentSet());
		p.getTableau().attachObserver(new ModelConstructingObserver(model));

		System.out.println("Context agents: " + c.getAgentIdMap());

		try {
			System.out.println(p.provable(f));
			//System.out.println(model);
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
