package debug;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class RemoveCommentsTest {

	RemoveComments rc = new RemoveComments();
	
	/*
	 * Testing strategy
	 * Input divides into :
	 * 	  source:
	 * 		with // comment / with no // comment
	 * 		with /* comment / with no /* comment
	 * 		comment with new lines / comment in one line
	 * 		string divided by /* 
	 * 		string divided by //
	 * 		
	 */
	
	/*
	 * Covers:
	 * 	Slash comments on a separate line
	 * 	Slash comment follows a sentence
	 * 	no comment
	 */
	@Test
	public void testRemoveSlashComment() {
		String[] withSlash = new String[] {
			"int main()",
			"{",
			"    //declaration",
			"int a, b, c;",
			"a = b+c;",
			"}"
		};
		
		List<String> slashAns = new ArrayList<String>() {{
			add("int main()");
			add("{");
			add("    ");
			add("int a, b, c;");
			add("a = b+c;");
			add("}");
		}};
		
		List<String> slashAns2 = new ArrayList<String>() {{
			add("int main()");
			add("{");
			add("int a, b, c;");
			add("a = b+c;");
			add("}");
		}};
		
		String[] withoutSlash = new String[] {
			"int main()",
			"{",
			"int a, b, c;",
			"a = b+c;",
			"}"
		};
		
		assertEquals(slashAns, rc.removeComments(withSlash));
		assertEquals(slashAns2, rc.removeComments(withoutSlash));
	}
	
	/*
	 * Covers:
	 * 	Asterisk comment single line and multiple line
	 * 	no comment
	 */
	@Test
	public void testRemoveAsteriskComment() {
		String[] withAsterisk = new String[] {
				"/* Test Program */",
				"int main()",
				"{",
				"int a, b, c;",
				"/* multiline",
				"   comment ",
				"   test */",
				"a = b+c;",
				"}"
		};
		
		String[] withoutAsterisk = new String[] {
				"int main()",
				"{",
				"int a, b, c;",
				"a = b+c;",
				"}"
		};
		
		List<String> ans = new ArrayList<String>() {{
			add("int main()");
			add("{");
			add("int a, b, c;");
			add("a = b+c;");
			add("}");
		}};
		
		assertEquals(ans, rc.removeComments(withAsterisk));
		assertEquals(ans, rc.removeComments(withoutAsterisk));
	}
	
	@Test
	public void testRemoveCommentWithNewLine() {
		
	}
	
	/*
	 * Covers:
	 * 	string before comment
	 * 	string after comment
	 * 	string on both sides of block comment, multiple line
	 * 	string on bith sides of block comment, single line
	 */
	@Test
	public void testRemoveCommentDividedString() {
		String[] divide1 = new String[] {
				"a/* comment ",
				" test ",
				" for divided */b",
		};
		
		String[] divide2 = new String[] {
				"a/* comment test */b"
		};
		
		String[] divide3 = new String[] {
				"/* comment ",
				" test */b"
		};
		
		String[] divide4 = new String[] {
				"a/* comment ",
				" test */"
		};
		
		List<String> ans = new ArrayList<String>() {{
			add("ab");
		}};
		
		List<String> ans1 = new ArrayList<String>() {{
			add("a");
		}};
		
		List<String> ans2 = new ArrayList<String>() {{
			add("b");
		}};
		
		assertEquals(ans, rc.removeComments(divide1));
		assertEquals(ans, rc.removeComments(divide2));
		assertEquals(ans1, rc.removeComments(divide4));
		assertEquals(ans2, rc.removeComments(divide3));
	}

}
