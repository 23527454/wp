package com.thinkgem.jeesite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

	@Test
	public void Test(){
		Calendar date = Calendar.getInstance();

		String year = String.valueOf(date.get(Calendar.YEAR));
		System.out.println(year);
	}

	@Test
	public void contextLoads() throws ParseException {
		/*Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int day=aCalendar.getActualMaximum(Calendar.DATE);*/

		StringBuffer sb=new StringBuffer("");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Integer year=2020;
		Date date = new Date();
		for(int i=0;i<12;i++){
			sb=new StringBuffer("");
			String str=year+"-"+(i+1)+"-"+1;
			Calendar c = Calendar.getInstance();
			date = format.parse(str);
			c.setTime(date);
			int week_index = c.get(Calendar.DAY_OF_WEEK) - 1;
			if(week_index<=0){
				week_index = 7;
			}
			System.out.println(year+"年"+(i+1)+"月第一天是星期："+week_index);
			int day=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			System.out.println(year+"年"+(i+1)+"月日历信息：");
			boolean isFirst=true;
			System.out.println("一\t二\t三\t四\t五\t六\t日");
			for(int j=0;j<day;j++){
				str=year+"-"+(i+1)+"-"+(j+1);
				date=format.parse(str);
				c.setTime(date);

				int week_index2 = c.get(Calendar.DAY_OF_WEEK) - 1;
				if(week_index2<=0){
					week_index2 = 7;
				}

				if(isFirst){
					isFirst=false;
					for(int l=1;l<week_index2;l++){
						System.out.print("\t");
					}
				}
				System.out.print(j+1);
				if(week_index2==7){
					System.out.print("\n");
				}else{
					System.out.print("\t");
				}

				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
					sb.append("0");
				} else{
					sb.append("1");
				}
			}

			System.out.println("\n本月的工作日信息："+sb.toString());

			System.out.println();
		}

		/*for(int i=1;i<=day;i++){
			String str="2020/7/"+i;
			Date bdate = format1.parse(str);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bdate);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				sb.append("0");
			} else{
				sb.append("1");
			}
		}

		int num=0;
		for(int i=1;i<=sb.length();i++){
			String str=sb.substring(i-1,i);
			System.out.print(i+"\t");
			if(str.equals("0")){
				num++;
			}
			if(num==2){
				System.out.println();
				num=0;
			}

		}*/
	}

}
