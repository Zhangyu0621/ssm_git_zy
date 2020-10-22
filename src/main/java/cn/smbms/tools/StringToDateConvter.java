package cn.smbms.tools;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConvter implements Converter<String, Date> {


    private String pattn;

    public StringToDateConvter(String pattn) {
        this.pattn = pattn;
    }

    @Override
    public Date convert(String source) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(pattn);
        try {
            date = format.parse(source);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
