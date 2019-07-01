package physicalObject;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 测试Electron工厂类
 *
 */
public class ElectronFactoryTest {

	/*
	 * 输入划分为：正确输入，测试生成对象是否与正确对象相同
	 */
	@Test
	public void testCreateProduct() {
		ElectronFactory af = new ElectronFactory();
		Electron e = (Electron)af.create("electron");
		Electron e1 = new Electron("electron");
		
		assertTrue(e.equals(e1));
	}	

}
