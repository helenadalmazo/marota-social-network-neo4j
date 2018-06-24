package main.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataUtil {

	public static DataUtil instance = new DataUtil();

	public GregorianCalendar stringToGregorianCalendar(String strData) throws Exception {

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date data = formato.parse(strData);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(data);

		return gc;
	}

	public String gregorianCalendarToString(GregorianCalendar gcData) throws Exception {

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String data = formato.format(gcData.getTime());

		return data;
	}

}
