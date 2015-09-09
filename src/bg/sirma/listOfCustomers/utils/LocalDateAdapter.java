package bg.sirma.listOfCustomers.utils;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String date) throws Exception {
        return LocalDate.parse(date, DateUtil.DATE_FORMATTER);
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return DateUtil.format(date);
    }
}