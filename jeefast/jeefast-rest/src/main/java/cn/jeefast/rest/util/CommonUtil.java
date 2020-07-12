package cn.jeefast.rest.util;

import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

	public static int weekByDate(Date d) {
		
		Calendar cal = Calendar.getInstance();

		cal.setTime(d);

		int weekDay = cal.get(Calendar.DAY_OF_WEEK);

		return weekDay;
	}
}
