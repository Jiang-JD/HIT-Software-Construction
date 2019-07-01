package parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import centralObject.CentralPoint;
import physicalObject.PhysicalObject;

/**
 * 解析器测试抽象类类，主要测试检查文本格式的方法
 *
 */
public abstract  class ParserTest {

	/*
	 * 输入划分为：
	 * 	checkLabel()
	 * 		符合构造规则/不符合构造规则
	 * 	checkWord()
	 * 		符合构造规则/不符合构造规则（含数字，空格）
	 * 	checkSentence()
	 * 		符合构造规则/不符合构造规则（包含其他符号）
	 * 	checkNumber()
	 * 		10000以内数字用科学记数法/10000以内数字不用科学记数法
	 * 		10000以上数字用科学记数法/10000以上数字不用科学记数法
	 * 		科学记数法整数部分不在1-9之间/在1-9之间
	 * 		科学记数法e后数字小于等于3/大于3
	 * 	
	 */
	abstract public Parser<? extends CentralPoint,? extends PhysicalObject> emptyInstance();
	
	/*
	 * Covers
	 * 	符合构造规则/不符合构造规则
	 * 
	 * input is kjlkjlk990jkjlKJLKJ， output is true
	 * input is lkjoijIJO90 KJLKJ, output is false
	 * input is ;lk;ljlkjoio890, output is false
	 * 	
	 */
	@Test
	public void testCheckLabel() {
		String correct = "kjlkjlk990jkjlKJLKJ";
		String withSpace = "lkjoijIJO90 KJLKJ";
		String withOther = ";lk;ljlkjoio890";
		
		Parser<? extends CentralPoint, ? extends PhysicalObject> p = emptyInstance();
		
		assertTrue(p.checkLabel(correct));
		assertFalse(p.checkLabel(withSpace));
		assertFalse(p.checkLabel(withOther));
	}
	
	/*
	 * covers
	 * 	符合构造规则/不符合构造规则（含数字，空格）
	 * 
	 * input is Lkiij, output is true
	 * input is kj;i8kL, output is false
	 * input is lkjOI I, output is false
	 */
	@Test
	public void testCheckWord() {
		String correct = "LKiij";
		String withNum = "kjli8kL";
		String withSpace = "lkjOI I";
		
		Parser<? extends CentralPoint, ? extends PhysicalObject> p = emptyInstance();
		
		assertTrue(p.checkWord(correct));
		assertFalse(p.checkWord(withSpace));
		assertFalse(p.checkWord(withNum));
	}
	
	/*
	 * covers
	 * 		10000以内数字用科学记数法/10000以内数字不用科学记数法
	 * 		10000以上数字用科学记数法/10000以上数字不用科学记数法
	 * 		科学记数法整数部分不在1-9之间/在1-9之间
	 * 		科学记数法e后数字小于等于3/大于3
	 * 
	 * input is 98465.26546, true
	 * input is 3.2168465e67, true
	 * input is 1.235e3, false
	 * input is 13216565.22, false
	 * input is 0.23165e5, false
	 * input is 3.3321e2, false
	 */
	@Test
	public void testCheckNumber() {
		String correct1 = "9846.26546";
		String correct2 = "3.2168465e67";
		String correct3 = "10000";
		String correct4 = "1";
		String withe = "1.235e3";
		String withoute = "13216565.22";
		String intnum = "0.23165e5";
		String eNum = "3.3321e2";
		
		Parser<? extends CentralPoint, ? extends PhysicalObject> p = emptyInstance();
		
		assertTrue(p.checkNumber(correct1));
		assertTrue(p.checkNumber(correct2));
		assertTrue(p.checkNumber(correct3));
		assertTrue(p.checkNumber(correct4));
		assertFalse(p.checkNumber(withe));
		assertFalse(p.checkNumber(withoute));
		assertFalse(p.checkNumber(intnum));
		assertFalse(p.checkNumber(eNum));
	}
}
