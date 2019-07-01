package items;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

public class LaddersTest {

  /*
   * Testing strategy
   *    Ladders()
   *      number,size   correct argument
   *    get()
   *      i   correct argument
   *    indexOf()
   *      i   correct argument
   *    iterator()
   *      observed by indexOf()
   */
  
  @Test
  public void testLadders() {
    Ladders ladders = new Ladders(2,5);
    
    assertEquals(5, ladders.length());
    assertEquals(2, ladders.size());
  }
  
  @Test
  public void testGet() {
    Ladders ladders = new Ladders(2,5);
    
    assertEquals(5, ladders.get(0).size());
  }
  
  @Test
  public void testIndexOf() {
    Ladders ladders = new Ladders(2,5);
    
    assertEquals(1, ladders.indexOf(ladders.get(1)));
  }
  
  @Test
  public void testIterator() {
    Ladders ladders = new Ladders(2,5);
    Iterator<Ladder> it = ladders.iterator();
    it.next();
    assertEquals(1, ladders.indexOf(it.next()));
  }

}
