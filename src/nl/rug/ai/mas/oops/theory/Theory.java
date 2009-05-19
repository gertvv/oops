package nl.rug.ai.mas.oops.theory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.rug.ai.mas.oops.formula.Conjunction;
import nl.rug.ai.mas.oops.formula.Formula;

/**
 * Represent a logical theory. For now, it is a set of formulas.
 */
public class Theory {
	private Set<Formula> d_formulas = new HashSet<Formula>();
	
	/**
	 * Get the formulas in this Theory. Returns an unmodifiable view on the Set contained in the Theory.
	 * @return The set of formulas contained by this theory.
	 */
	public Set<Formula> getFormulas() {
		return Collections.unmodifiableSet(d_formulas);
	}
	
	/**
	 * Add a formula to this Theory.
	 * @param f The formula to add.
	 */
	public void add(Formula f) {
		d_formulas.add(f);
	}
	
	/**
	 * Remove a formula from this Theory.
	 * @param f The formula to remove.
	 */
	public boolean remove(Formula f) {
		return d_formulas.remove(f);
	}
	
	/**
	 * Convert the theory into a formula.
	 * TODO: Let this return a balanced binary tree or something like that.
	 */
	public Formula asFormula() {
		Formula result = null;
		for (Formula f : d_formulas) {
			if (result == null) {
				result = f;
			} else {
				result = new Conjunction(result, f);
			}
		}
		return result;
	}
	
	public String toString() {
		return d_formulas.toString();
	}
}