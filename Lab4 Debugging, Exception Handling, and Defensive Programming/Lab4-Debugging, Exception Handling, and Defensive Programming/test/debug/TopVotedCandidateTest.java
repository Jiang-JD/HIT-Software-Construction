package debug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;


public class TopVotedCandidateTest {
	/*
	 * Testing strategy:
	 * The input is divided as follows:
	 * 	  TopVotedCandidate()
	 * 		persons:  one candidate / one more candidates
	 * 		times:	  increasing array
	 * 		
	 * 	  q()
	 * 		t:		no candidate in times / one candidate in times / more candidates in times
	 * 				candidates votes number are equal / votes number not equal
	 */
	
	/*
	 * Covers:
	 * 	  one candidate
	 * 	  one candidate in search time
	 */
	@Test
	public void testOneCandidate() {
		int[] persons = {0,0,0};
		int[] times = {5,10,15};
		TopVotedCandidate tvc = new TopVotedCandidate(persons, times);
		
		assertEquals(0, tvc.q(10));
		assertEquals(0, tvc.q(14));
	}
	
	/*
	 * Covers:
	 * 	  more candidate
	 * 	  more candidate in search time
	 * 	  equal votes
	 */
	@Test
	public void testTwoCandidate() {
		int[] persons = {0,1,0};
		int[] times = {5,10,15};
		TopVotedCandidate tvc = new TopVotedCandidate(persons, times);
		
		assertEquals(1, tvc.q(10));
		assertEquals(0, tvc.q(15));
	}
	
	/*
	 * Covers
	 * 	  more candidates
	 * 	  Unequal votes in search time
	 */
	@Test
	public void testMoreCandidate() {
		int[] persons = {0,1,0,2,1,3,3,0};
		int[] times = {5,10,15,16,18,20,25,30};
		TopVotedCandidate tvc = new TopVotedCandidate(persons, times);
		
		assertEquals(0, tvc.q(15));
		//Unequal votes
		assertEquals(0, tvc.q(16));
		assertEquals(0, tvc.q(30));
	}
	
	/*
	 * Covers:
	 * 	  One candidate
	 * 	  No candidate in search time
	 */
	@Test
	public void testNoCandidateInTime() {
		int[] persons = {0,0,0};
		int[] times = {5,10,15};
		TopVotedCandidate tvc = new TopVotedCandidate(persons, times);
		
		assertEquals(-1, tvc.q(4));
	}
	
	/*
	 * Covers:
	 * 	  more candidates
	 * 	  Unequal votes in search time
	 */
	@Test
	public void testEqulsVotes() {
		int[] persons = {0,1,0,2,1,3,3,0};
		int[] times = {5,10,15,16,18,20,25,30};
		TopVotedCandidate tvc = new TopVotedCandidate(persons, times);
		
		//Equal votes
		assertEquals(1, tvc.q(10));
		assertEquals(1, tvc.q(18));
		assertEquals(3, tvc.q(25));
	}

}
