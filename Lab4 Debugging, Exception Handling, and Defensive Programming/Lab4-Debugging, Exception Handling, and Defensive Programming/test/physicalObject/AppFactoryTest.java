package physicalObject;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * APP工厂测试类
 *
 */
public class AppFactoryTest {
	
	/*
	 * 输入划分为：正确输入，测试生成对象是否与正确对象相同
	 */
	@Test
	public void testCreateProduct() {
		AppFactory af = new AppFactory();
		App a = new App("QQ", "Tencent", "29.2","\"The second popular social networking App in China\"" , "\"Social network\"");
		String s = "App ::= <QQ,Tencent,29.2,\"The second popular social networking App in China\",\"Social network\">";
		App b = (App) af.create(s);
		
		assertTrue(a.equals(b));
	}	

}
