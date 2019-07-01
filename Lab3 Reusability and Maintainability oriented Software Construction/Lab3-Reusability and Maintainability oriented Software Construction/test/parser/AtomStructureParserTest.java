package parser;

import static org.junit.Assert.*;

import org.junit.Test;

import centralObject.CentralPoint;
import circularOrbit.AtomStructure;
import parser.AtomStructureParser;
import parser.Parser;
import physicalObject.PhysicalObject;

public class AtomStructureParserTest extends ParserTest {
	/*
	 * Testing strategy
	 * 	  parserFile()
	 * 		filePath	
	 * 	  initialAtomStrucuture()
	 * 		fileText	从getText()获取的正确文本
	 * 		co			空系统
	 */
	
	@Override
	public Parser<? extends CentralPoint, ? extends PhysicalObject> emptyInstance() {
		return new AtomStructureParser();
	}
	
	@Test
	public void testInitialAtomStructure() {
		AtomStructureParser asp = new AtomStructureParser();
		AtomStructure as = asp.initial("src/txt/AtomicStructure_Medium.txt");
		
		assertEquals("Er", as.getCentralPoint().getName());
		assertEquals(6, as.getTrackNum());
		assertEquals(8, as.getObjects(1).size());
	}
	
	@Test
	public void testParserFile() {
		AtomStructureParser asp = new AtomStructureParser();
		assertFalse(asp.parserFile("src/txt/AtomicStructure_IllegalElement.txt"));
		assertFalse(asp.parserFile("src/txt/AtomicStructure_IllegalTrackNumber.txt"));
		assertFalse(asp.parserFile("src/txt/AtomicStructure_NoElementName.txt"));
		assertFalse(asp.parserFile("src/txt/AtomicStructure_NoElementNumber.txt"));
		assertFalse(asp.parserFile("src/txt/AtomicStructure_NoTrackNumber.txt"));
		assertFalse(asp.parserFile("src/txt/AtomicStructure_Secquence.txt"));
		assertTrue(asp.parserFile("src/txt/AtomicStructure.txt"));
	}
}
