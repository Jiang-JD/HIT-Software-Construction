package P1;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

/**
 * 本程序演示如何判断幻方
 * 
 * @author hit
 *
 */
public class MagicSquare {
	public static void main(String[] args) throws IOException {
		generateMagicSquare(5);
		System.out.println(isLegalMagicSquare("src/P1/txt/6.txt"));
		
		System.out.println(isLegalMagicSquare("src/P1/txt/1.txt"));
		System.out.println(isLegalMagicSquare("src/P1/txt/2.txt"));
		System.out.println(isLegalMagicSquare("src/P1/txt/3.txt"));
		System.out.println(isLegalMagicSquare("src/P1/txt/4.txt"));
		System.out.println(isLegalMagicSquare("src/P1/txt/5.txt"));
	}

	/**
	 * 判断是否为MagicSquare
	 * 
	 * @param fileName 文件路径
	 * @return boolean 是否为MS
	 * @throws IOException 抛出IO异常
	 */
	public static boolean isLegalMagicSquare(String fileName) throws IOException {
		File file = new File(fileName);

		// 按行读取
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String myLine = bf.readLine();

		// 判断是否存在分割符不为\t，思路为判断分割后字符串是否含空白符
		String[] row = myLine.split("\t");
		if (!isDevidedByTab(row)) {
			System.out.println("分隔符不为\\t，请检查输入");
			return false;
		}

		// 取到第一行的数字个数，往下判断每一行是否数量相同，判断是否行数等于列数
		int columnlen = row.length; // 记录列数
		int rowlen = 0; // 记录行数
		int[][] square = new int[columnlen][columnlen];
		int rowi = 0; // 数组下标
		for (String s : row) {
			if (!isNumeric(s)) {
				System.out.println("非正整数，请检查输入");
				return false;
			}
			square[0][rowi] = Integer.valueOf(s);
			rowi++;
		}
		rowlen++;  //记录行数

		while ((myLine = bf.readLine()) != null) {
			rowi = 0;
			row = myLine.split("\t");
			if (!isDevidedByTab(row)) {
				System.out.println("分隔符不为\\t，请检查输入");
				return false;
			}
			if (columnlen != row.length) {
				System.out.println("不符合矩阵形式，行缺少数字，请检查输入");
				return false;
			}
			for (String s : row) {
				if (!isNumeric(s)) {
					System.out.println("非正整数，请检查输入");
					return false;
				}
				square[rowlen][rowi] = Integer.valueOf(s);
				rowi++;
			}
			rowlen++;
		}
		bf.close();
		if (rowlen != columnlen) {
			System.out.println("行列数不相等，请检查输入");
			return false;
		}

		// 检查行相加是否相等
		int sum = 0;
		int tsum = 0;
		for (int i = 0; i < rowlen; i++) {
			sum += square[0][i];
		}
		for (int i = 0; i < rowlen; i++) {
			for (int j = 0; j < rowlen; j++) {
				tsum += square[i][j];
			}
			if (tsum != sum)
				return false;
			tsum = 0;
		}

		// 检查列相等
		for (int i = 0; i < rowlen; i++) {
			for (int j = 0; j < rowlen; j++) {
				tsum += square[j][i];
			}
			if (tsum != sum)
				return false;
			tsum = 0;
		}

		// 检查对角线
		for (int j = 0; j < rowlen; j++) {
			tsum += square[j][j];
		}
		if (tsum != sum)
			return false;
		tsum = 0;

		return true;

	}
	
	/**
	 * 检查字符串是否为正整数，使用正则表达式
	 * @param string
	 * @return true false
	 */
	public static boolean isNumeric(String string) {
		Pattern pat = Pattern.compile("[0-9]*");
		return pat.matcher(string).matches();
	}
	
	/**
	 * 检查字符串数组中是否存在空格
	 * @param string[]
	 * @return 存在空格返回false，否则返回true
	 */
	public static boolean isDevidedByTab(String[] string) {
		for (String s : string)
			if (!s.equals("") && s.indexOf(" ") != -1) {
				return false;
			}
		return true;
	}
	
	/**
	 * 输出矩阵
	 * @param m 矩阵大小
	 */
	public static void checkSquare(int[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * 生成奇数幻方
	 * @param n 矩阵规模
	 * @return 是否生成成功
	 */
	public static boolean generateMagicSquare(int n) {
		//此处为扩展内容，判断是否为奇数和偶数
		if(n < 0)
		{
			System.out.println("输入数字为负数，请重新输入");
			return false;
		}
		if(n % 2==0)
		{
			System.out.println("输入数字为偶数，请重新输入");
			return false;
		}
		
		//生成矩阵
		/* n奇数幻方口诀：
		 * 1. 数字1放在第一行中间
		 * 2. 依次放在上一个数的右上角
		 * 2.1如果右边出去了就回到左边
		 * 2.2 如果上面出去了就放下面
		 * 2.3 如果右上角有了就放在这个数的下面
		 * 根据口诀可以写出相应算法
		 */ 
		int magic[][] = new int[n][n];
		int row = 0, col = n / 2, i, j, square = n * n; 
		//square范围1-n*n为填入幻方的值
		//row=0，col=n/2为第一行中间，即口诀中”数字1放在第一行中间“
		for (i = 1; i <= square; i++) {
			magic[row][col] = i;
			//每个幻方中n的倍数位置的下面一格一定跟的是下一个数字，所以直接下移一行
			if (i % n == 0)
				row++;
			//如果不是n倍数，则分情况判断
			else {
				//行
				if (row == 0)
					row = n - 1;  //若为第一行，则右上角为空，移至最下面一行
				else
					row--;  //正常上移一行
				//列
				if (col == (n - 1))
					col = 0;  //若为最右列，则右上角为空，移至最左
				else 
					col++;  //正常右移一列
			}
		}
		//输出幻方
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++)
				System.out.print(magic[i][j] + "\t");
			System.out.println();
		}
		
		//写入文件
		try {
			File file = new File("src/P1/txt/6.txt");
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(i = 0; i < n; i++)
			{
				for(j = 0; j < n-1; j++) {
					bw.write(magic[i][j]+"\t");
				}
				bw.write(magic[i][j]+"\n");
			}
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		} 

		return true;
	}
}
