package parser;

import constant.Regex;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import exception.UndefinedElementException;

import org.apache.log4j.Logger;

/**.
 * 文本解析器父类，解析文件格式，修改轨道系统的结构，同时解析器还会对文本的 合法性进行检查。
 *
 */
public abstract class Parser<L, E> {
  /**.
   * 检查图输入，检查图输入是否符合格式要求。
   * 
   * @param filePath 待检查文本路径
   * @param io 文本输入IO策略
   * @return 如果文本输入合法，返回文本字符串， 否则返回null
   * @throws NoSuchElementException              缺少元素名称抛出
   * @throws IllegalElementFormatException       标签格式不正确抛出
   * @throws TrackNumberOutOfRangeException      轨道数目不符合要求抛出
   * @throws IncorrectElementLabelOrderException 元素分量次序不合法抛出
   * @throws IncorrectElementDependencyException 元素之间依赖关系不正确抛出
   * @throws ElementLabelDuplicationException    元素标签重复抛出
   * @throws LackOfComponentException            缺少分量抛出
   * @throws UndefinedElementException           使用到未定义元素抛出
   */
  public abstract String parserFile(String filePath, IoStrategy io) throws NoSuchElementException, 
                                                          IllegalElementFormatException,
                                                          TrackNumberOutOfRangeException, 
                                                          IncorrectElementLabelOrderException, 
                                                          IncorrectElementDependencyException,
      LackOfComponentException, ElementLabelDuplicationException, UndefinedElementException;
  

  private Logger logger = Logger.getLogger(Parser.class);

  /**.
   * 根据输入文件路径，获取文本文件的字符串形式
   * 
   * @param filePath 文本文件路径，文本文件为utf-8编码格式
   * @return 如果文本格式合法，返回文本文件的字符串，否则返回null
   */
  public String getText(String filePath, IoStrategy io) {
    String s = io.getText(filePath);
    logger.info("Get the text of " + filePath);
    return s;
  }
  
  /**.
   * 根据输入文件路径，获取文本文件的字符串形式, 读取方式采用默认FileInputStream
   * 
   * @param filePath 文本文件路径，文本文件为utf-8编码格式
   * @return 如果文本格式合法，返回文本文件的字符串，否则返回null
   */
  public String getText(String filePath) {
    return getText(filePath, new FileStreamIo());
  }

  /**.
   * 检查字符串是否符合label构造规则，label要求为由大小写字母或数字构成，不含有空格和其他符号
   * 
   * @param s 待检查字符串
   * @return true如果是label，否则false
   */
  public boolean checkLabel(String s) {
    if (s == null) {
      return false;
    }
    return s.matches(Regex.REGEX_LABEL);
  }

  /**.
   * 检查字符串是否符合word构造规则，word要求为由大小写字母构成，不含有空格
   * 
   * @param s 待检查字符串
   * @return true如果是word，否则false
   */
  public boolean checkWord(String s) {
    if (s == null) {
      return false;
    }
    return s.matches(Regex.REGEX_WORD);
  }

  /**.
   * 检查字符串是否符合sentence构造规则，sentence要求为由大小写字母或数字构成，可含有空格
   * 
   * @param s 待检查字符串
   * @return true如果是sentence，否则false
   */
  public boolean checkSentence(String s) {
    if (s == null) {
      return false;
    }
    return s.matches(Regex.REGEX_SENTENCE);
  }

  /**.
   * 检查字符串是否符合number构造规则，number要求为大于 10000 的数字按科学记数法表示（例如 1.9885e30 表示1.9885 ∗
   * 1030，但 e 之前数字的整数部分必 须在 1 到 9 的范围内，e 之后的 数字只能是大于 3 的正整 数），小于 10000 的数字直接给出（例如
   * 5912，103.193）， 不能用 科学计数法。小数点位数不限制。
   * 
   * @param s 待检查字符串
   * @return true如果是number，否则false
   */
  public boolean checkNumber(String s) {
    if (s == null || "".equals(s)) {
      return false;
    }
    if (!s.matches(Regex.REGEX_NUMBER)) {
      return false;
    }
    double num = Double.valueOf(s);
    if (num <= 10000) {
      // number < 10000 should with no "e"
      if (!s.matches(Regex.REGEX_NUMBER_BELOW1e4)) {
        return false;
      }
      return true;
    } else {
      if (!s.matches(Regex.REGEX_NUMBER_ABOVE1e4)) {
        return false;
      }
      return true;
    }
  }
}
