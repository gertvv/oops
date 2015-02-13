package nl.rug.ai.mas.oops.model.test;

import static org.junit.Assert.*;

import java.util.Collections;

import nl.rug.ai.mas.oops.formula.AgentId;
import nl.rug.ai.mas.oops.model.Arrow;
import nl.rug.ai.mas.oops.model.ConfigurableModel;
import nl.rug.ai.mas.oops.model.ConfigurableModel.Relation;
import nl.rug.ai.mas.oops.model.KripkeModel;
import nl.rug.ai.mas.oops.model.Valuation;
import nl.rug.ai.mas.oops.model.World;

import org.junit.Test;

public class ConfigurableModelTest {

	private AgentId d_a1 = new AgentId("1", 1);
	private World d_w1 = new World("w1", new Valuation());
	private World d_w2 = new World("w2", new Valuation());

	@Test
	public void testSerialModel() {
		KripkeModel model = new ConfigurableModel(Collections.singleton(d_a1), Collections.singleton(Relation.SERIAL));

		model.addWorld(d_w1);
		model.closeModel();
		assertEquals(
				Collections.singleton(new Arrow(d_a1, d_w1, d_w1)),
				model.getArrowsFrom(d_a1, d_w1));
		
		model = model.newModel();
		model.addWorld(d_w1);
		model.addWorld(d_w2);
		model.addArrow(d_a1, d_w1, d_w2);
		model.closeModel();
		assertEquals(
				Collections.singleton(new Arrow(d_a1, d_w1, d_w2)),
				model.getArrowsFrom(d_a1, d_w1));
		assertEquals(
				Collections.singleton(new Arrow(d_a1, d_w2, d_w2)),
				model.getArrowsFrom(d_a1, d_w2));

	}
	
}
