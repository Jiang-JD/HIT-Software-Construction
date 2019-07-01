package debug;
/**
 * This program is used for removing all the comments in a program code.
 *
 * Example 1:
 * Input: 
 * source = ["/*Test program /", "int main()", "{ ", "  // variable declaration ", "int a, b, c;", "/* This is a test", "   multiline  ", "   comment for ", "   testing /", "a = b + c;", "}"]
 *
 * The line by line code is visualized as below:
 * 
 * /*Test program 
 * int main()
 * { 
 * // variable declaration 
 * int a, b, c;
 * /* This is a test
 *  multiline  
 *  comment for 
 *  testing 
 * a = b + c;
 * }
 *
 * Output: ["int main()","{ ","  ","int a, b, c;","a = b + c;","}"]
 *
 * The line by line code is visualized as below:
 * 
 * int main()
 * { 
 * 
 * int a, b, c;
 * a = b + c;
 * }
 *
 * Explanation: 
 * The string /* denotes a block comment, including line 1 and lines 6-9. The string // denotes line 4 as comments.
 * 
 * Example 2:
 * 
 * Input: 
 * source = ["a/*comment", "line", "more_comment/b"]
 * 
 * Output: ["ab"]
 * 
 * Explanation: 
 * The original source string is "a/*comment\nline\nmore_comment/b", where we have bolded the newline characters.  
 * After deletion, the implicit newline characters are deleted, leaving the string "ab", which when delimited by newline characters becomes ["ab"].
 * 
 * 
 * Note:
 * 
 * The length of source is in the range [1, 100].
 * The length of source[i] is in the range [0, 80].
 * Every open block comment is eventually closed.
 * There are no single-quote, double-quote, or control characters in the source code.
 *
 **/

import java.util.ArrayList;
import java.util.List;

class RemoveComments {
  public List<String> removeComments(String[] source) {
    boolean inBlock = false;
    /*
     * 1. 添加变量 pre 表示/* 注释前存在字符串 why:
     * 前序字符串表示/*注释前存在的字符串。可能存在/*注释前存在字符串需要和注释后字符串合并的情况，添加变量表示是否存在前序字符串
     */
    boolean pre = false;
    StringBuilder newline = null;
    /*
     * 2. 添加前序字符串的暂存变量 tmpline why: /*注释中间可能存在很多行，每次都会new一个newline，所以需要一个变量保存前序字符串
     */
    StringBuilder tmpline = new StringBuilder();
    /*
     * 3. 添加了 ArrayList why: Not specify the specified List type 缺少具体的List实现类
     */
    List<String> ans = new ArrayList<>();
    for (String line : source) {
      int i = 0;
      char[] chars = line.toCharArray();
      /*
       * 4. 移除判断inBlock的条件语句 why: 如果添加条件语句，之前inBlock是true，则永远无法将inBlock修改为false
       */
      newline = new StringBuilder();
      while (i < line.length()) {
        if (!inBlock && i + 1 <= line.length() && chars[i] == '/' && chars[i + 1] == '*') {
          if (i != 0) {
            pre = true; // 存在前序字符串
            tmpline = new StringBuilder(newline);
            newline.delete(0, newline.length());
          }
          inBlock = true;
          i++;
        } else if (inBlock && i + 1 <= line.length() && chars[i] == '*' && chars[i + 1] == '/') {
          inBlock = false;
          i++;
          /*
           * 5. 添加条件判断，判断//注释，如果存在//注释跳出循环 why: 没有考虑到//注释的情况
           */
        } else if (!inBlock && i + 1 <= line.length() && chars[i] == '/' && chars[i + 1] == '/') {
          break;
        } else if (!inBlock) {
          newline.append(chars[i]);
        }
        i++;
      }
      /*
       * 6. 将inBloc修改为 !inBlock why: inBlock should be false to add new line.
       * inBlock应该为false，否则无法添加新的行
       * 
       * 7. 添加成立条件，!inblock && pre why: 如果 /* 注释前有字符串且注释结束行没有字符串（不满足newline.length() >
       * 0，为了使得/*前字符串能够 读入，添加新条件，pre为true，存在前序字符串
       */
      if ((!inBlock && newline.length() > 0) || (!inBlock && pre)) {
        if (pre) {
          newline.insert(0, tmpline);
        }
        ans.add(new String(newline));
        pre = false; // 将前序字符串状态修改为不存在
        tmpline.delete(0, tmpline.length()); // 清空前序字符串缓存
      }
    }
    return ans;
  }
}