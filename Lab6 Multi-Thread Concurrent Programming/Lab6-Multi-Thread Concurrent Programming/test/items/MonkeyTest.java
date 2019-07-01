package items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tool.Selector5;

public class MonkeyTest {

  /*
   * Testing Strategy
   *    Monkey()
   *      correct argument
   *      observed by getDirection(), getId(), getSpeed()
   *    toString()
   *      correct argument
   *    equals()
   *      equal / not equal 
   */
  
  Ladders ladders = new Ladders(1, 5);
  
  @Test
  public void testMokey() {
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    assertEquals(0, m1.getID());
    assertEquals("L->R", m1.getDirection());
    assertEquals(6, m1.getSpeed());
  }
  
  @Test
  public void testToString() {
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    
    assertEquals("[monkey: 0, direction: L->R, speed: 6, selector: Selector5]", m1.toString());
  }
  
  @Test
  public void testEquals() {
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    assertTrue(m1.equals(m1));
    assertFalse(m1.equals(m2));
  }

}
