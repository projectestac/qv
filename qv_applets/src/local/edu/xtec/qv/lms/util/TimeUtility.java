package edu.xtec.qv.lms.util;

public class TimeUtility{

	public static String addTime(String time1, String time2){
		String time = time1;
		try{
			int h1 = Integer.parseInt(time1.substring(0,2));
			int m1 = Integer.parseInt(time1.substring(3,5));
			int s1 = Integer.parseInt(time1.substring(6,8));
			int h2 = Integer.parseInt(time2.substring(0,2));
			int m2 = Integer.parseInt(time2.substring(3,5));
			int s2 = Integer.parseInt(time2.substring(6,8));
			
			int s3 = s1+s2;
			int m3 = m1+m2;
			int h3 = h1+h2;
			
			if (s3>59){
				s3=s3-60;
				m3=m3+1;
			}
			if (m3>59){
				m3=m3-60;
				h3=h3+1;
			}
			
			String s4 = "";
			if (s3<10){
				s4 = "0"+s3;
			} else {
				s4 = s3+"";
			}
			String m4 = "";
			if (m3<10){
				m4 = "0"+m3;
			} else {
				m4 = m3+"";
			}
			String h4 = "";
			if (h3<10){
				h4 = "0"+h3;
			} else {
				h4 = h3+"";
			}
			time = h4+":"+m4+":"+s4;
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}

		return time;
	}
	
	public static String getDuration(String time){
		String duration = "";
		int y = 0;
		int mt = 0;
		int d = 0;
		int h = 0;
		int m = 0;
		int s = 0;
		try{
			y = 0;
			mt = 0;
			d = 0;
			h = Integer.parseInt(time.substring(0,2));
			m = Integer.parseInt(time.substring(3,5));
			s = Integer.parseInt(time.substring(6,8));
			if (h>23){
				d = (int)h/24;
				h = h-(d*24);
			}
			if (d>30){
				mt = (int)d/30;
				d = d-(mt*30);
			}
			if (mt>11){
				y = (int)mt/12;
				mt = mt-(y*12);
			}
		} catch (Exception ex){
			System.out.println("time:"+time);
			ex.printStackTrace(System.err);
		}
		duration = "P"+y+"Y"+mt+"M"+d+"DT"+h+"H"+m+"M"+s+"S";
		return duration;
	}

}