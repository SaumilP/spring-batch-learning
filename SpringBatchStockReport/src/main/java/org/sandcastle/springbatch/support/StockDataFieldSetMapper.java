package org.sandcastle.springbatch.support;

import org.sandcastle.springbatch.models.StockData;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.math.BigDecimal;

/**
 * Field Mapper
 */
@Component ("stockDataFieldMapper")
public class StockDataFieldSetMapper implements FieldSetMapper<StockData> {

    private static final String NOT_AVAILABLE = "n/a";

    @Override public StockData mapFieldSet(FieldSet fs) throws BindException {
        StockData data = new StockData();
        data.setSymbol(fs.readString(0));
        data.setName(fs.readString(1));
        String lastSaleValue = fs.readString(2);
        if(NOT_AVAILABLE.equals(lastSaleValue)){
            data.setLastSale(BigDecimal.ZERO);
        } else {
            data.setLastSale(new BigDecimal(lastSaleValue));
        }
        data.setMarketCap(fs.readBigDecimal(3));
        data.setAdrTso(fs.readString(4));
        data.setIpoYear(fs.readString(5));
        data.setSector(fs.readString(6));
        data.setIndustry(fs.readString(7));
        data.setSummaryUrl(fs.readString(8));
        return data;
    }
}
