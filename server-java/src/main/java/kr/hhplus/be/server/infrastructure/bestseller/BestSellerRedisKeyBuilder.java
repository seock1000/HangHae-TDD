package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSellerBase;
import kr.hhplus.be.server.domain.bestseller.SalesStat;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class BestSellerRedisKeyBuilder {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String getDailyKeyBySalesStat(SalesStat salesStat) {
        return "BESTSELLER_SALES_STAT:ranking:" + dateFormatter.format(LocalDate.now());
    }

    public String getDailyKeyByDate(LocalDate date) {
        return "BESTSELLER_SALES_STAT:ranking:" + dateFormatter.format(date);
    }

    public String getLast3DaysKeyByDate(LocalDate date) {
        return "BESTSELLER_SALES_STAT:ranking:last3days" + dateFormatter.format(date);
    }
}
