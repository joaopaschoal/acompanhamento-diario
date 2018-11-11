package joaopaulo.acompanhamentodiario.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UtilDate {
	public static String dateToBrStringLocalTime(long time) {
		SimpleDateFormat sdfBr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		return sdfBr.format(new Date(time));
	}
	
	public static String dateToShortBrStringGmtTime(long time, boolean flg2digits) {
		String strDateFormat = flg2digits ? "dd/MM/yy" : "dd/MM/yyyy";
        SimpleDateFormat sdfBr = new SimpleDateFormat(strDateFormat);
		sdfBr.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdfBr.format(new Date(time));
	}

    public static void removeTimePart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
