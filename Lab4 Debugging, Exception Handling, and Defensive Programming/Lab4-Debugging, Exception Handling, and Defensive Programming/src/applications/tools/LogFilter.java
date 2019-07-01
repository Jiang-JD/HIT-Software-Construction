package applications.tools;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constant.Regex;

/**
 * Log Filter to filter the logs in the condition
 *
 */
public class LogFilter {
	
	/**
	 * Filter the logs by time
	 * @param t timespan
	 * @param logs logs
	 * @return logs in the timespan
	 */
	public List<String> filterByTime(Timespan t , List<String> logs) {
		if(t == null) return logs;
		List<String> flog = new ArrayList<String>();
		Matcher m;
		String regex = "("+Regex.APPECO_DATE+") ("+Regex.APPECO_TIME+")";
		Pattern p = Pattern.compile(regex);
		for(String log : logs) {
			m = p.matcher(log);
			m.find();
			Instant i = Instant.parse(m.group(1)+"T"+m.group(4)+"Z");
			if(t.overlap(new Timespan(i,i))) {
				flog.add(log);
			}
		}
		return flog;
	}
	
	/**
	 * Filter the logs by Class
	 * @param classname class name
	 * @param logs logs
	 * @return logs hanppend in the class
	 */
	public List<String> filterByClass(String classname, List<String> logs) {
		if(classname == null) return logs;
		if(classname.equals("None")) return logs;
		List<String> flog = new ArrayList<String>();
		Matcher m;
		String regex = Regex.APPECO_DATE+" "+Regex.APPECO_TIME+" \\[\\w+\\] \\(\\S+\\) \\["+classname+"\\]";
		Pattern p = Pattern.compile(regex);
		for(String log : logs) {
			m = p.matcher(log);
			if(m.find()) 
				flog.add(log);
		}
		return flog;
	}
	
	/**
	 * Filter the logs by menthod
	 * @param methodname method name
	 * @param logs logs
	 * @return logs happend in this method
	 */
	public List<String> filterByMethod(String methodname, List<String> logs) {
		if(methodname == null) return logs;
		if(methodname.equals("None")) return logs;
		List<String> flog = new ArrayList<String>();
		Matcher m;
		String regex = Regex.APPECO_DATE+" "+Regex.APPECO_TIME+" \\[\\w+\\] \\(\\S+\\) \\[[^\\]]+\\]-\\["+methodname+"\\]";
		Pattern p = Pattern.compile(regex);
		for(String log : logs) {
			m = p.matcher(log);
			if(m.find()) 
				flog.add(log);
		}
		return flog;
	}
	
	/**
	 * Filter the log by Operation
	 * @param operation operation name
	 * @param logs logs
	 * @return logs in this operation
	 */
	public List<String> filterByOperation(String operation, List<String> logs) {
		if(operation == null) return logs;
		if(operation.equals("None")) return logs;
		return logs;
	}
	
	/**
	 * Filter the log by type (For exception)
	 * @param type exception type
	 * @param logs logs
	 * @return specify exception logs 
	 */
	public List<String> filterByType(String type, List<String> logs) {
		if(type == null) return logs;
		if(type.equals("None")) return logs;
		List<String> flog = new ArrayList<String>();
		Matcher m;
		String regex = Regex.APPECO_DATE+" "+Regex.APPECO_TIME+" \\[ERROR] \\(\\S+\\) \\[[^\\]]+\\]-\\[[^\\]]+\\] \\[(\\w+.)*\\]";
		Pattern p = Pattern.compile(regex);
		for(String log : logs) {
			m = p.matcher(log);
			if(m.find())
				if(m.group(6).equals(type))
					flog.add(log);
		}
		return flog;
	}
}
