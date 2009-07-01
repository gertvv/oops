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

package nl.rug.ai.mas.oops.model;

import nl.rug.ai.mas.oops.formula.AgentId;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

/**
 * A Kripke Model. This is a set of Worlds, a set of Agents, for every Agent a
 * set of Arrows between worlds and for every World a Valuation function from
 * propositions to truth values.
 */
public class KripkeModel {
	Set<AgentId> d_agents;
	HashMap<AgentId, DefaultDirectedGraph<World, Arrow>> d_graphs;
	World d_mainWorld;

	public KripkeModel(Set<AgentId> agents) {
		d_agents = agents;
		d_graphs = new HashMap<AgentId, DefaultDirectedGraph<World, Arrow>>();
		for (AgentId a : d_agents) {
			d_graphs.put(a,
				new DefaultDirectedGraph<World, Arrow>(new DummyEdgeFactory()));
		}
		d_mainWorld = null;
	}

	/**
	 * @return true if the World was not already in the model.
	 */
	public boolean addWorld(World w) {
		for (DefaultDirectedGraph<World, Arrow> graph :
				d_graphs.values()) {
			if (!graph.addVertex(w)) { // world already exists
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if the Arrow was not already in the model.
	 */
	public boolean addArrow(Arrow r) {
		DefaultDirectedGraph<World, Arrow> g = d_graphs.get(r.getAgent());
		return g.addEdge(r.getSource(), r.getTarget(), r);
	}

	public boolean addArrow(AgentId agent, World source, World target) {
		return addArrow(new Arrow(agent, source, target));
	}

	public Set<Arrow> getArrowsFrom(AgentId agent, World source) {
		Set<Arrow> arrows = new HashSet<Arrow>();
		for (Arrow r : d_graphs.get(agent).edgesOf(source)) {
			if (r.getSource().equals(source)) {
				arrows.add(r);
			}
		}
		return arrows;
	}

	public Set<Arrow> getArrowsTo(AgentId agent, World target) {
		Set<Arrow> arrows = new HashSet<Arrow>();
		for (Arrow r : d_graphs.get(agent).edgesOf(target)) {
			if (r.getTarget().equals(target)) {
				arrows.add(r);
			}
		}
		return arrows;
	}

	public Set<World> getWorlds() {
		if (d_agents.isEmpty()) {
			return new HashSet<World>();
		}
		return d_graphs.get(d_agents.iterator().next()).vertexSet();
	}
	
	public World getMainWorld() {
		return d_mainWorld;
	}
	
	public void setMainWorld(World w) {
		d_mainWorld = w;
	}

	public DirectedMultigraph<World, Arrow> constructMultigraph() {
		DirectedMultigraph<World, Arrow> graph =
			new DirectedMultigraph<World, Arrow>(new DummyEdgeFactory());

		for (World w : getWorlds()) {
			graph.addVertex(w);
		}

		for (DefaultDirectedGraph<World, Arrow> g : d_graphs.values()) {
			for (Arrow r : g.edgeSet()) {
				graph.addEdge(r.getSource(), r.getTarget(), r);
			}
		}

		return graph;
	}

	
	/**
	 * Construct an empty Kripke Model having a set of agents. In effect, the
	 * current model is used as a prototype.
	 * @return A new (empty) model of the same type.
	 */
	public KripkeModel newModel() {
		return new KripkeModel(d_agents);
	}
	
	public String toString() {
		String str = this.getClass().getSimpleName() + ":\n";
		for (Map.Entry<AgentId, DefaultDirectedGraph<World, Arrow>> e :
				d_graphs.entrySet()) {
			str += e.getKey().toString() + " -> " + e.getValue().toString() +
				"\n";
		}
		return str;
	}

	private class DummyEdgeFactory implements EdgeFactory<World, Arrow> {
		public Arrow createEdge(World s, World t) {
			return null;
		}
	}
}
