package track;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 测试RaceTrack类
 *
 */
public class RaceTrackTest {

	/*
	 * 测试策略
	 * 	getNumber()
	 * 		输入正确参数，测试生成对象的编号是否正确
	 * 		track = produced by RaceTrack()
	 */
	@Test
	public void testGetNumber() {
		Track t1 = new RaceTrack(1);
		assertEquals(1,t1.getNumber());
	}

}
