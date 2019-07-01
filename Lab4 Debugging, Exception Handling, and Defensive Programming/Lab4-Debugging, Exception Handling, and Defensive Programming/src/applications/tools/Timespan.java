package applications.tools;

import java.time.Instant;



/**
 * 这是一个{@code immutable} 类型，表示一段时间间隔，一段时间间隔由一个起始时间点和一个
 * 不早于起始时间的终止时间点组成。时间间隔包含其终止时间点。
 *
 */
public class Timespan {	
	private final Instant start;
    private final Instant end; 
    
    /*
     * AF
     * 	AF(start, end) = 一个时间段
     *  
     * Rep invariant: 
     *  	start <= end. 
     * 
     *  Safety from exposure:
     *  	所有域是private final的，不提供Mutator，客户端无法拿到直接引用
     */
    
    private void checkRep() {
    	assert !start.isAfter(end) : "start point is after end point";
    }
    
    /**
     * Make a Timespan.
     * 
     * @param start
     *            starting date/time
     * @param end
     *            ending date/time. Requires end >= start.
     */
    public Timespan(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("requires start <= end");
        }
        this.start = start;
        this.end = end;
        checkRep();
    }
    
    public Timespan(Timespan timespan) {
        this.start = timespan.getStart();
        this.end = timespan.getEnd();
        checkRep();
    }

    /**
     * @return the starting point of the interval
     */
    public Instant getStart() {
        return start;
    }

    /**
     * @return the ending point of the interval
     */
    public Instant getEnd() {
        return end;
    }
    
    /**
     * 判断当前时间段是否与其他时间段相交，相交的定义为时间段有重合部分，特别的，
     * 如果时间段之间仅仅有时间点重合，则判定为不相交，例如，t1的终止时间点与t2的
     * 起始时间点重合，t1与t2不相交。
     * 
     * @param ts 待判断时间段
     * @return 如果时间段相交，返回{@code true} ， 否则返回{@code false}
     */
    public boolean overlap(Timespan ts) {
    	if(ts == null) return false;
    	if(ts.getEnd().isBefore(start)) return false;
    	if(ts.getStart().isAfter(end)) return false;
    	
    	if((start.isBefore(ts.getStart()) || start.equals(ts.getStart()))
    			&& end.isAfter(ts.getStart())) return true;
    	
    	if(((end.isAfter(ts.getEnd())|| end.equals(ts.getEnd())) 
    			&& start.isBefore(ts.getEnd()))) return true;
		if(ts.getStart().isBefore(start) && ts.getEnd().isAfter(end)) return true;
		
		return false;
    }
    
    /**
     * 生成描述时间段的字符串，格式为 {@code [<timespan>...<timespan>]}
     */
    @Override public String toString() {
        return "[" + this.getStart()
                + "..." + this.getEnd()
                + "]";
    }

    /*
     * @see Object.equals()
     */
    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Timespan)) {
            return false;
        }

        Timespan that = (Timespan) thatObject;
        return this.start.equals(that.start) 
                && this.end.equals(that.end);
    }

    /*
     * @see Object.hashCode()
     */
    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + start.hashCode();
        result = prime * result + end.hashCode();
        return result;
    }
}
