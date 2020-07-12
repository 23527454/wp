package cn.jeefast.modules.equipment.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	
	public static int weekByDate(Date d) {
		
		Calendar cal = Calendar.getInstance();

		cal.setTime(d);

		int weekDay = cal.get(Calendar.DAY_OF_WEEK);

		return weekDay;
	}
}
