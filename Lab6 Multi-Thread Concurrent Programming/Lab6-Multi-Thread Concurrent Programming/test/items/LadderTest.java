package items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tool.Selector5;

public class LadderTest {

  /*
   * Testing Strategy
   *    Ladder()
   *      index, size     correct input
   *      observed by size()
   *      
   *    move()
   *      m         monkey on the ladder
   *      step      after moving still on the ladder/ after moving leave the ladder
   *      
   *    occupy()
   *      m         monkey on the ladder
   *      direction R->L / L->R
   *      
   *    contain()
   *      m         monkey on the ladder / not on the ladder
   *      
   *    contain()
   *      position  monkey on the position / not on the position
   *      
   *    leftmost()
   *      one monkey on the ladder / two monkey on the ladder / no monkey
   *      
   *    rightmost()
   *      one monkey on the ladder / two monkey on the ladder / no monkey
   *    
   *    getPosition()
   *      m         monkey on the ladder / not on the ladder
   *      
   *    add()
   *      m         monkey not on the ladder
   *      observed by contain()
   *      
   *    toString()
   *   
   */
  
  @Test
  public void testLadder() {
    Ladder ladder = new Ladder(0, 5);
    
    assertEquals(5, ladder.size());
  }
  
  @Test
  public void testMove() {
    Ladders ladders = new Ladders(1,5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    ladder.add(m1);
    assertEquals(2, ladder.move(m1, 2));

    assertEquals(-1, ladder.move(m1, 3));
  }
  
  @Test
  public void testOccupy() {
    Ladders ladders = new Ladders(1, 5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    ladder.add(m1);
    assertEquals(-1, ladder.occupy(m1, m1.getDirection()));
    
    ladder.add(m2);
    assertEquals(4, ladder.occupy(m1, m1.getDirection()));
  }
  
  @Test
  public void testContain() {
    Ladders ladders = new Ladders(1, 5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    assertEquals(false, ladder.contain(m1));
    ladder.add(m1);
    assertTrue(ladder.contain(m1));
    
    assertFalse(ladder.contain(1));
    assertTrue(ladder.contain(0));
  }
  
  @Test
  public void testLeftmost() {
    Ladders ladders = new Ladders(1, 5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    assertNull(ladder.leftmost());
    ladder.add(m1);
    assertEquals(m1, ladder.leftmost());
  }
  
  @Test
  public void testRightmost() {
    Ladders ladders = new Ladders(1, 5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    assertNull(ladder.rightmost());
    ladder.add(m1);
    assertEquals(m1, ladder.rightmost());
  }
  
  @Test
  public void testGetPosition() {
    Ladders ladders = new Ladders(1, 5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    assertEquals(-1, ladder.indexOf(m1));
    ladder.add(m1);
    assertEquals(0, ladder.indexOf(m1));
  }
  
  @Test
  public void testToString() {
    Ladders ladders = new Ladders(1, 5);
    Ladder ladder = new Ladder(0, 5);
    Monkey m1 = new Monkey(0, "L->R", 6, ladders);
    m1.setSelector(new Selector5());
    Monkey m2 = new Monkey(0, "R->L", 6, ladders);
    m2.setSelector(new Selector5());
    
    ladder.add(m1);
    assertEquals("[ladder 0 rungs: 5, monkeys: 1]", ladder.toString());
  }

}
