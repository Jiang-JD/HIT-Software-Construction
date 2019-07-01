package debug;

import static org.junit.Assert.*;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FindMedianSortedArraysTest {

	/*
	 * Testing strategy:
	 * 	The input is divided into follows:
	 * 		A & B:	
	 * 			A|B is only has one number/A|B has many numbers
	 * 			A|B is null arrays
	 * 			Two arrays has odd numbers / Two arrays has even numbers
	 * 			The range of numbers are overlaps / not overlaps
	 * 			The median is number of A B / median is calculated
	 * 			
	 * 			
	 */
	
	FindMedianSortedArrays fma = new FindMedianSortedArrays();
	
	/*
	 * Covers:
	 * 	A has one number, B has many numbers
	 * 	A and B has odd and even numbers
	 * 	median is not calculated / calculated
	 */
	@Test
	public void testOneNumber() {
		int[] a = {1};
		int[] b = {2,3};
		int[] c = {3,4,5};
		assertEquals(2.0, fma.findMedianSortedArrays(a, b), 0);
		assertEquals(3.5, fma.findMedianSortedArrays(a, c), 0);
	}
	
	/*
	 * Covers:
	 * 	A is null arrays, B has one or more numbers
	 * 	A and B has even and odd numbers
	 * 	median is not calculated / calculated
	 */
	@Test
	public void testNullArrays() {
		int[] a = {};
		int[] b = {1};
		int[] c = {1,2};
		int[] d = {1,2,3};
		assertEquals(1.0, fma.findMedianSortedArrays(a, b), 0);
		assertEquals(1.5, fma.findMedianSortedArrays(a, c), 0);
		assertEquals(2.0, fma.findMedianSortedArrays(a, d), 0);
		assertEquals(1.0, fma.findMedianSortedArrays(c, b), 0);
	}
	
	/*
	 * Covers:
	 * 	A and B has even numbers (a, b)
	 * 	A and B has odd numbers (a, c)
	 * 	median is not calculated / calculated
	 */
	@Test
	public void testEvenAndOdd() {
		int[] a = {1,1,1};
		int[] b = {2,3,4};
		int[] c = {5,6};
		assertEquals(1.5, fma.findMedianSortedArrays(a, b), 0.0);
		assertEquals(1.0, fma.findMedianSortedArrays(a, c), 0.0);
		assertEquals(4.0, fma.findMedianSortedArrays(b, c), 0.0);
	}
	
	/*
	 * Covers:
	 * 	A and B are overlaps (a, b; a, c)
	 * 	A and B has even numbers (a, c)
	 * 	A and B has odd numbers (a, b)
	 * 	median is not calculated / calculated
	 */
	@Test
	public void testOverlaps() {
		int[] a = {1,3,4,6,9};
		int[] b = {2,3,5,6,10,11};
		int[] c = {2,4,5,7,9};
		assertEquals(5.0, fma.findMedianSortedArrays(a, b), 0);
		assertEquals(4.5, fma.findMedianSortedArrays(a, c), 0);
		assertEquals(5.0, fma.findMedianSortedArrays(b, c), 0);
	}
}
