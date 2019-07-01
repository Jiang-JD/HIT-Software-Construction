package circularorbit;

import java.util.Objects;

import track.Track;

/**.
 * 表示轨道物体的位置，一个轨道物体的位置包括其所属的轨道（其中就含有了半径这个属性）
 * 和初始角度（如果轨道物体存在这个属性）。某些应用中，位置表示物体在轨道上的绝对位置。 在某些应用中，位置表示物体所处的轨道。
 *
 */
class Position {
  private final Track track;
  private final double angle;

  /*
   * Abstract Function 
   *    AF(track, angle) = 一个轨道上物体的位置
   * 
   * Representation invariant 
   *    track 轨道不为空且轨道在系统中存在 
   *    angle angle等于-1 或者 0 <= angle < 360
   * 
   * Safety from rep exposure 
   *    所有的rep都是private final的，不提供mutator
   */

  private void checkRep() {
    assert track != null : "track为null";
    assert (angle == -1) || (0 <= angle && angle < 360) : "angle不合法";
  }

  /**.
   * 初始化一个位置对象，包含轨道和初始角度，如果物体在轨道上没有初始角度，则角度设置为-1， 否则，一个物体的初始角度为极坐标中的角度，其范围为 0 <=
   * angle < 360
   * 
   * @param track 物体所属轨道对象，要求轨道不为空对象
   * @param angle 物体的初始角度，范围为 0 <= angle < 360，若物体没有角度属性，设置为-1
   */
  public Position(Track track, double angle) {
    this.track = track;
    this.angle = angle;
    checkRep();
  }

  /**.
   * 获取所在轨道，要求Track不为空
   * 
   * @return 所在的轨道
   */
  public Track getTrack() {
    return track;
  }

  /**.
   * 获取物体所处角度，该角度为极坐标下角度， 使用弧度制，0 <= angle < 360 或 -1
   * 
   * @return 物体的初始角度
   */
  public double getAngle() {
    return angle;
  }

  /**.
   * 将一个对象与当前对象比较，若其初始角度和轨道相同，则判定为两个对象相等
   */
  @Override
  public boolean equals(Object otherobject) {
    if (this == otherobject) {
      return true;
    }
    if (otherobject == null) {
      return false;
    }
    if (getClass() != otherobject.getClass()) {
      return false;
    }

    Position other = (Position) otherobject;

    return this.track.equals(other.getTrack()) && this.angle == other.angle;
  }

  /**.
   * 输出描述位置的字符串
   */
  @Override
  public String toString() {
    return "[Position  Track:" + track.toString() + ", Angle:" + angle + "]";
  }

  /**.
   * 获得位置哈希码
   */
  @Override
  public int hashCode() {
    return Objects.hash(track, angle);
  }
}
