package debug;

/**
 * Given two ordered integer arrays nums1 and nums2, with size m and n Find out
 * the median (double) of the two arrays. You may suppose nums1 and nums2 cannot
 * be null at the same time.
 *
 * Example 1: nums1 = [1, 3] nums2 = [2] The output would be 2.0
 * 
 * Example 2: nums1 = [1, 2] nums2 = [3, 4] The output would be 2.5
 * 
 * Example 3: nums1 = [1, 1, 1] nums2 = [5, 6, 7] The output would be 3.0
 * 
 * Example 4: nums1 = [1, 1] nums2 = [1, 2, 3] The output would be 1.0
 */

public class FindMedianSortedArrays {

  public double findMedianSortedArrays(int[] A, int[] B) {
    int m = A.length;
    int n = B.length;
    if (m > n) {
      int[] temp = A;
      A = B;
      B = temp;
      int tmp = m;
      m = n;
      n = tmp;
    }
    /*
     * 1. 将m+n修改为m+n+1 why: i表示A中分界下标，j表示B中分界下标，两个数组的中位数条件为
     * 左边的数字和右边的数字个数应该相等，不考虑中间。所以为 i+j = m-i + n-j + 1 i+j = (m+n+1) / 2 j = hafLen
     * - i
     */
    int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
    while (iMin <= iMax) {
      /*
       * 2. 将imin+imax+1 修改为 imin+imax why: i表示下标，下标的计算不需要+1
       */
      int i = (iMin + iMax) / 2; // spotbug标记这里会发生计算溢出，不修改
      int j = halfLen - i;
      if (i < iMax && B[j - 1] > A[i]) {
        iMin = i + 1;
      } else if (i > iMin && A[i - 1] > B[j]) {
        iMax = i - 1;
      } else {
        int maxLeft = 0;
        if (i == 0) {
          maxLeft = B[j - 1];
        } else if (j == 0) {
          maxLeft = A[i - 1];
        } else {
          maxLeft = Math.max(A[i - 1], B[j - 1]);
        }
        /*
         * 3. 将m+n+1 修改为 (m+n) % 2 != 0 (SpotBug提示) why:
         * 这里是判断A和B合并的数组数字个数是否为奇数，若为奇数，则maxLeft就 表示中间的数字，直接返回即可。这里m和n表示AB数量，不应该+1
         */
        if ((m + n) % 2 != 0) {
          return maxLeft;
        }
        int minRight = 0;
        if (i == m) {
          minRight = B[j];
        } else if (j == n) {
          minRight = A[i];
        } else {
          minRight = Math.min(B[j], A[i]);
        }
        return (maxLeft + minRight) / 2.0;
      }
    }
    return 0.0;
  }

}
