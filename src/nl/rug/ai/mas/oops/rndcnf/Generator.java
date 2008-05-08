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

import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import nl.rug.ai.mas.oops.formula.*;

public class Generator {
	private Random d_rnd;
	private int d_maxDepth;
	private int d_numClause;
	private int d_numProp;
	private int d_numAgent;
	private DisjunctDist d_disjunctDist;
	private PropRateDist d_propRateDist;
	private Proposition[] d_prop;
	private Agent[] d_agent;

    public static void main(String[] args) {
		if (args.length != 7) {
			argsError("Insufficient arguments");
		}
		int n = 0;
		Generator g = null;
		try {
			n = Integer.parseInt(args[0]);
			int d = Integer.parseInt(args[1]);
			int L = Integer.parseInt(args[2]);
			int N = Integer.parseInt(args[3]);
			int m = Integer.parseInt(args[4]);
			String C = args[5];
			String p = args[6];
			g = new Generator(new Random(), d, L, N, m, C, p);
		} catch (Exception e) {
			argsError(e.getMessage());
		}
        for (int i = 0; i < n; ++i) {
            Formula f = g.generate();
            System.out.println(f);
        }
    }

	private static void argsError(String err) {
		System.out.println("Wrong arguments: " + err);
		System.err.println("Usage: \n\tGenerator n d L N m C p");
		System.exit(1);
	}

	/**
	 * @param rnd Random number generator
	 * @param d Maximal modal depth of generated formula
	 * @param l Number of clauses in the top-level conjunction
	 * @param n Number of propositional variables
	 * @param m Number of agents
	 * @param c Probability distribution of the number of disjuncts
	 * @param p Probability distribution of the propositional/modal rate
	 */
	public Generator(Random rnd, int d, int l, int n, int m,
			String c, String p) {
		this(rnd, d, l, n, m, new DisjunctDist(c), new PropRateDist(p));
	}

	/**
	 * @param rnd Random number generator
	 * @param d Maximal modal depth of generated formula
	 * @param l Number of clauses in the top-level conjunction
	 * @param n Number of propositional variables
	 * @param m Number of agents
	 * @param c Probability distribution of the number of disjuncts
	 * @param p Probability distribution of the propositional/modal rate
	 */
	public Generator(Random rnd, int d, int l, int n, int m,
			DisjunctDist c, PropRateDist p) {
		d_rnd = rnd;
		d_maxDepth = d;
		d_numClause = l;
		d_numProp = n;
		d_numAgent = m;
		d_disjunctDist = c;
		d_propRateDist = p;
		generatePropositions();
		generateAgents();
	}

	/**
	 * Generate a Formula according to the settings given.
	 */
	public Formula generate() {
		Formula f = null;
		ArrayList<Formula> clauses = new ArrayList<Formula>();
		for (int i = 0; i < d_numClause; ++i) {
			Formula clause = rndClause(d_maxDepth);
			while (clauses.contains(clause)) {
				clause = rndClause(d_maxDepth);
			}
			clauses.add(clause);
			if (f == null) {
				f = clause;
			} else {
				f = new Conjunction(f, clause);
			}
		}
		return f;
	}

	private Formula rndClause(int depth) {
		int length = d_disjunctDist.rndLength(d_rnd, depth);
		int propNum = d_propRateDist.rndPropNum(d_rnd, depth, length);

		ArrayList<Formula> clauses = new ArrayList<Formula>();
		do {
			clauses.clear();
			for (int i = 0; i < length; ++i) {
				if (i < propNum) {
					clauses.add(rndSign(rndAtom(0)));
				} else {
					clauses.add(rndSign(rndAtom(depth)));
				}
			}
		} while (repeatedAtoms(clauses));
		Collections.sort(clauses, new FormulaComparator());

		Formula f = null;
		for (Formula clause : clauses) {
			if (f == null) {
				f = clause;
			} else {
				f = new Disjunction(f, clause);
			}
		}

		return f;
	}

	private Formula rndProposition() {
		return d_prop[d_rnd.nextInt(d_numProp)];
	}

	private Formula rndModalF(int depth) {
		Formula f = rndClause(depth - 1);
		Agent a = d_agent[d_rnd.nextInt(d_numAgent)];
		return new MultiBox(a, f);
	}

	private Formula rndAtom(int depth) {
		if (depth == 0) {
			return rndProposition();
		}
		return rndModalF(depth);
	}

	private Formula rndSign(Formula f) {
		if (d_rnd.nextBoolean()) {
			return new Negation(f);
		}
		return f;
	}

	private boolean repeatedAtoms(List<Formula> l) {
		ArrayList<Formula> atoms = new ArrayList<Formula>();
		for (Formula f : l) {
			if (f instanceof Negation) {
				Negation n = (Negation) f;
				f = n.getRight();
			}
			if (atoms.contains(f)) {
				return true;
			}
			atoms.add(f);
		}
		return false;
	}

	private void generatePropositions() {
		PropositionMap map = new PropositionMap();
		d_prop = new Proposition[d_numProp];

		for (int i = 0; i < d_numProp; ++i) {
			String name = "p" + new Integer(i + 1).toString();
			d_prop[i] = map.getOrCreate(name);
		}
	}

	private void generateAgents() {
		AgentIdMap map = new AgentIdMap();
		d_agent = new Agent[d_numAgent];

		for (int i = 0; i < d_numAgent; ++i) {
			String name = new Integer(i + 1).toString();
			d_agent[i] = map.getOrCreate(name);
		}
	}
}
