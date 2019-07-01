package parser;

import static org.junit.Assert.assertEquals;

import centralobject.CentralPoint;
import circularorbit.AtomStructure;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.IncorrectElementLabelOrderException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import org.junit.Test;

import physicalobject.PhysicalObject;

public class AtomStructureParserTest extends ParserTest {
  /*
     * Testing strategy
     *       parserFile()
     *         输入AS_Eception文件夹中所有错误文本，对异常类型进行检查
     *         IllegalElementFormatException()
     *         IncorrectElementDependencyException()
     *         IncorrectElementLabelOrderException()
     *         TrackNumberOutOfRangeException()
     *         NoSuchElementException()
     * 
     *       initialAtomStrucuture()
     *         fileText    从getText()获取的正确文本
     *         co            空系统
     */
  AtomStructureParser asp = new AtomStructureParser();

  @Override
  public Parser<? extends CentralPoint, ? extends PhysicalObject> emptyInstance() {
    return new AtomStructureParser();
  }

  @Test
  public void testInitialAtomStructure() {
    AtomStructureParser asp = new AtomStructureParser();
    AtomStructure as = null;
    try {
      as = asp.initial("src/txt/AtomicStructure_Medium.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (as != null) {
      assertEquals("Er", as.getCentralPoint().getName());
      assertEquals(6, as.getTrackNum());
      assertEquals(8, as.getObjects(1).size());
    }
  }

  @Test
  public void testIllegalElementFormatException() {
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_IllegalElement.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
    }
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_IllegalElementNumber.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
    }
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_DoubleElementNumber.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
    }
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_NegativeElementNumber.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
    }
  }

  @Test
  public void testIncorrectElementDependencyException() {
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_IllegalDependency.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), IncorrectElementDependencyException.class.getName());
    }
  }

  @Test
  public void testIncorrectElementLabelOrderException() {
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_Secquence.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), IncorrectElementLabelOrderException.class.getName());
    }
  }

  @Test
  public void testTrackNumberOutOfRangeException() {
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_IllegalTrackNumber.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), TrackNumberOutOfRangeException.class.getName());
    }
  }

  @Test
  public void testNoSuchElementException() {
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_NoElementName.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), NoSuchElementException.class.getName());
    }
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_NoElementNumber.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), NoSuchElementException.class.getName());
    }
    try {
      asp.parserFile("src/txt/AS_Exception/AtomicStructure_NoTrackNumber.txt");
    } catch (Exception e) {
      assertEquals(e.getClass().getName(), NoSuchElementException.class.getName());
    }
  }
}
