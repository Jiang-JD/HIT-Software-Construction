package apis;

import circularorbit.CircularOrbit;

public class CircularOrbitHelper {

  /**.
   * 生成轨道系统图像
   * @param c 轨道系统
   */
  public static void visualize(CircularOrbit c) {
    Fx f = new Fx();
    Fx.co = c;
    f.show();
  }
}
