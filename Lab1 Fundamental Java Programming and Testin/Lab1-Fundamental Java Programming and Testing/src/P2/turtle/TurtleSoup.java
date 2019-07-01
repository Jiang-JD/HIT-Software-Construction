/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class TurtleSoup {

	/**
	 * Draw a square.
	 * 
	 * @param turtle     the turtle context
	 * @param sideLength length of each side
	 */
	public static void drawSquare(Turtle turtle, int sideLength) {
		for (int i = 0; i < 4; i++) {
			turtle.forward(sideLength);
			turtle.turn(90);
		}

		// throw new RuntimeException("implement me!");
	}

	/**
	 * Determine inside angles of a regular polygon.
	 * 
	 * There is a simple formula for calculating the inside angles of a polygon; you
	 * should derive it and use it here.
	 * 
	 * @param sides number of sides, where sides must be > 2
	 * @return angle in degrees, where 0 <= angle < 360
	 */
	public static double calculateRegularPolygonAngle(int sides) {
		if (sides <= 2) {
			System.out.println("边数不能少于3，请重新输入");
			return 0;
		}
		return 180 - (double) 360 / sides;
		// throw new RuntimeException("implement me!");
	}

	/**
	 * Determine number of sides given the size of interior angles of a regular
	 * polygon.
	 * 
	 * There is a simple formula for this; you should derive it and use it here.
	 * Make sure you *properly round* the answer before you return it (see
	 * java.lang.Math). HINT: it is easier if you think about the exterior angles.
	 * 
	 * @param angle size of interior angles in degrees, where 0 < angle < 180
	 * @return the integer number of sides
	 */
	public static int calculatePolygonSidesFromAngle(double angle) {
		return (int)Math.round(360.0 / (180.0 - angle));  //round四舍五入
		//throw new RuntimeException("implement me!");
	}

	/**
	 * Given the number of sides, draw a regular polygon.
	 * 
	 * (0,0) is the lower-left corner of the polygon; use only right-hand turns to
	 * draw.
	 * 
	 * @param turtle     the turtle context
	 * @param sides      number of sides of the polygon to draw
	 * @param sideLength length of each side
	 */
	public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
		double angle = 180 - calculateRegularPolygonAngle(sides);
		for (int i = 0; i < sides; i++) {
			turtle.turn(angle);
			turtle.forward(sideLength);
		}
		// throw new RuntimeException("implement me!");
	}

	/**
	 * Given the current direction, current location, and a target location,
	 * calculate the Bearing towards the target point.
	 * 
	 * The return value is the angle input to turn() that would point the turtle in
	 * the direction of the target point (targetX,targetY), given that the turtle is
	 * already at the point (currentX,currentY) and is facing at angle
	 * currentBearing. The angle must be expressed in degrees, where 0 <= angle <
	 * 360.
	 *
	 * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
	 * 
	 * @param currentBearing current direction as clockwise from north
	 * @param currentX       current location x-coordinate
	 * @param currentY       current location y-coordinate
	 * @param targetX        target point x-coordinate
	 * @param targetY        target point y-coordinate
	 * @return adjustment to Bearing (right turn amount) to get to target point,
	 *         must be 0 <= angle < 360
	 */
	public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY, int targetX,
			int targetY) {
		// 以<0,1>为正北方向，利用向量的余弦公式计算夹角余弦，再转换为度数
		int northX = 0, northY = 1;
		int X = targetX - currentX;
		int Y = targetY - currentY;
		double cosNorth = (X * northX + Y * northY) / ((Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2)))
				* (Math.sqrt(Math.pow(northX, 2) + Math.pow(northY, 2)))); // 余弦公式

		double cosAngle = Math.toDegrees(Math.acos(cosNorth));

		if (targetX < currentX)
			cosAngle = 360 - cosAngle; //如果目标角度小于当前角（与正北夹角），则顺时针转
		double adjustmentAngle = Math.abs(cosAngle - currentBearing);
		if (currentBearing > cosAngle)
			adjustmentAngle = 360 - adjustmentAngle;  //两种情况判断，是否取夹角差
		return adjustmentAngle;
		// throw new RuntimeException("implement me!");
	}

	/**
	 * Given a sequence of points, calculate the Bearing adjustments needed to get
	 * from each point to the next.
	 * 
	 * Assumes that the turtle starts at the first point given, facing up (i.e. 0
	 * degrees). For each subsequent point, assumes that the turtle is still facing
	 * in the direction it was facing when it moved to the previous point. You
	 * should use calculateBearingToPoint() to implement this function.
	 * 
	 * @param xCoords list of x-coordinates (must be same length as yCoords)
	 * @param yCoords list of y-coordinates (must be same length as xCoords)
	 * @return list of Bearing adjustments between points, of size 0 if (# of
	 *         points) == 0, otherwise of size (# of points) - 1
	 */
	public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
		List<Double> adjustment = new ArrayList<>();
		int currentX = 0, currentY = 0, targetX = 0, targetY = 0;
		double currentBearing = 0;
		for (int i = 0; i < xCoords.size() - 1; i++) {
			currentX = xCoords.get(i);
			currentY = yCoords.get(i);
			targetX = xCoords.get(i + 1);
			targetY = yCoords.get(i + 1);
			adjustment.add(calculateBearingToPoint(currentBearing, currentX, currentY, targetX, targetY));
			currentBearing = (currentBearing + adjustment.get(i)) % 360;  //计算当前角度时取模，防止溢出360
		}

		return adjustment;
		// throw new RuntimeException("implement me!");
	}

	/**
	 * Given a set of points, compute the convex hull, the smallest convex set that
	 * contains all the points in a set of input points. The gift-wrapping algorithm
	 * is one simple approach to this problem, and there are other algorithms too.
	 * 
	 * @param points a set of points with xCoords and yCoords. It might be empty,
	 *               contain only 1 point, two points or more.
	 * @return minimal subset of the input points that form the vertices of the
	 *         perimeter of the convex hull
	 */
	public static Set<Point> convexHull(Set<Point> points) {
		// 0,1,2点直接返回
		if (points.size() == 0 || points.size() == 1 || points.size() == 2) {
			return points;
		}

		List<Point> plist = new ArrayList<Point>(points);
		
		Set<Point> subset = new HashSet<Point>();
		
		// 找到最左且最下的点
		double x1 = 0, y1 = 0;
		double x2 = 0, y2 = 0;
		int index = 0;
		x1 = plist.get(0).x();
		y1 = plist.get(0).y();
		for (int i = 0; i < plist.size(); i++) {
			x2 = plist.get(i).x();
			y2 = plist.get(i).y();
			if (x2 < x1) {
				x1 = x2;
				y1 = y2;
				index = i;
			} else if (x2 == x1) {
				if (y2 < y1) {
					x1 = x2;
					y1 = y2;
					index = i; //index记录起始点下标
				}
			}
		}
		
		int start = index;
		int matchindex = 0;
		subset.add(plist.get(start));
		//循环，直到下一个凸点是起始点为止
		do {
			matchindex = 0;
			for(int i = 0; i < points.size(); i++) {
				if(matchindex == index || isOuter(plist.get(index),plist.get(matchindex),plist.get(i))) {
					matchindex = i;
				}
			}
			
			index = matchindex;
			subset.add(plist.get(index));
		}while(start != index);
		
		return subset;  //返回子集
		}
		
		// throw new RuntimeException("implement me!");
	
	/**
	 * 辅助函数：判断检查点与凸点向量是否要比当前点与凸点向量更加靠边（顺时针旋转才能与检查点向量共线）
	 * @param base 基准点，已经为凸点
 	 * @param current 当前点
	 * @param check 比较点
	 * @return
	 */
	public static boolean isOuter(Point base, Point current, Point check) {
		double x1,x2,y1,y2;
		x1 = current.x() - base.x();
		x2 = check.x() - base.x();
		y1 = current.y() - base.y();
		y2 = check.y() - base.y();
		double crossmul = x1 * y2 - x2 * y1;  //向量叉乘，若A向量与B叉乘为负，则A需要顺时针旋转才与B共线（<180）
		if(crossmul < 0) return true;
		//若共线，则比较检查点是否离凸点更远
		if(crossmul == 0 && isLonger(base, current, check)) return true;
		return false;
	}
	/**
	 * Determine the distant between <x1,y1> and <x2,y2>
	 * 辅助函数：判断检查点向量的模是否要比当前点向量的模要长
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 是否长于当前点
	 */
	public static boolean isLonger(Point base, Point current, Point check) {
		double x1,x2,x3,y1,y2,y3;
		x1 = base.x();
		x2 = current.x();
		x3 = check.x();
		y1 = base.y();
		y2 = current.y();
		y3 = check.y();
		
		double dis1 = Math.sqrt((Math.pow((x2 - x1), 2)) + (Math.pow((y2 - y1), 2)));
		double dis2 = Math.sqrt((Math.pow((x3 - x1), 2)) + (Math.pow((y3 - y1), 2)));
		return dis2 > dis1;
	}

	/**
	 * Draw your personal, custom art.
	 * 
	 * Many interesting images can be drawn using the simple implementation of a
	 * turtle. For this function, draw something interesting; the complexity can be
	 * as little or as much as you want.
	 * 
	 * @param turtle the turtle context
	 */
	public static void drawPersonalArt(Turtle turtle) {
		int sides = 21;
		turtle.forward(0);
		turtle.turn(90);
		turtle.forward(80);
		turtle.turn(180);
		//turtle.turn(90);
		
		sides = 90;
		for (int i = 0; i < sides; i++) {
			turtle.color(PenColor.BLACK);
			turtle.forward(220);
			turtle.turn(165);
			turtle.color(PenColor.RED);
			turtle.forward(300);
			turtle.turn(130);
			turtle.color(PenColor.ORANGE);
			turtle.forward(277);
			turtle.turn(134);
			turtle.color(PenColor.YELLOW);
			turtle.forward(280);
			turtle.turn(140);
		}
		// throw new RuntimeException("implement me!");
	}

	/**
	 * Main method.
	 * 
	 * This is the method that runs when you run "java TurtleSoup".
	 * 
	 * @param args unused
	 */
	public static void main(String args[]) {
		DrawableTurtle turtle = new DrawableTurtle();

		// drawSquare(turtle, 40);
		// drawRegularPolygon(turtle, 3, 100);
		drawPersonalArt(turtle);
		// draw the window
		turtle.draw();
	}

}
