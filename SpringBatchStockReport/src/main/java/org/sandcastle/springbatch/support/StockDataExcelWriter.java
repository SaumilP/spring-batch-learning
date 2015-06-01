package org.sandcastle.springbatch.support;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.sandcastle.springbatch.models.StockData;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.batch.runtime.StepExecution;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Stock Data to Excel Writer
 */
@Component("stockDataExcelWriter")
@Scope("step")
public class StockDataExcelWriter implements ItemWriter<StockData> {

    private static final String FILE_NAME = "file:excel/StockData";
    private static final String[] HEADERS = { "Symbol", "Name", "Last Sale",
                                              "Market Cap", "ADR TSO", "IPO Year", "Sector", "Industry",
                                              "Summary URL" };
    private String outputFileName;
    private Workbook workbook;
    private CellStyle dataCellStyle;
    private int currRow = 0;

    private static final int FONT_SIZE = 10;
    private static final String ARIAL = "Arial";

    private void addHeaders(Sheet sheet){
        Workbook wb = sheet.getWorkbook();
        CellStyle style = wb.createCellStyle();

        Font font = wb.createFont();
        font.setFontHeightInPoints((short)FONT_SIZE);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);

        Row row = sheet.createRow(2);
        int column = 0;
        for(String header : HEADERS ){
            Cell cell = row.createCell(column);
            cell.setCellValue(header);
            cell.setCellStyle(style);
            column++;
        }
        currRow++;
    }

    private void addTitleToSheet(Sheet sheet){
        Workbook wb = sheet.getWorkbook();
        CellStyle style = wb.createCellStyle();

        Font font = wb.createFont();
        font.setFontHeightInPoints((short) (FONT_SIZE + 4));
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName(ARIAL);

        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);

        Row row = sheet.createRow(currRow);
        row.setHeightInPoints(16);

        String currDate = DateFormatUtils.format(Calendar.getInstance(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("Stock Data as of " + currDate);
        cell.setCellStyle(style);

        CellRangeAddress range = new CellRangeAddress(0,0,0,7);
        sheet.addMergedRegion(range);
        currRow++;
    }

    @AfterStep public void afterStep(StepExecution stepExecution) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFileName);
        workbook.write(fos);
        fos.close();
    }

    @BeforeStep public void beforeStep(StepExecution stepExecution){
        System.out.println("Calling BeforeStep...");
        String dateTime = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMdd_HHmmss");
        outputFileName = FILE_NAME + "_" + dateTime + ".xlsx";

        workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Testing");
        sheet.createFreezePane(0,3,0,3);
        sheet.setDefaultColumnWidth(20);
        addHeaders(sheet);
        initDataStyle();
    }

    private void initDataStyle(){
        dataCellStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) FONT_SIZE);
        font.setFontName(ARIAL);

        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        dataCellStyle.setFont(font);
    }

    @Override public void write(List<? extends StockData> items) throws Exception {
        Sheet sheet = workbook.getSheetAt(0);
        for (StockData data : items) {
            for(int i =0; i < 100; i++){
                currRow++;
                Row row = sheet.createRow(currRow);
                createStringCell(row,data.getSymbol(),0);
                createStringCell(row,data.getName(),1);
                createNumericCell(row, data.getLastSale().doubleValue(), 2);
                createNumericCell(row, data.getMarketCap().doubleValue(), 3);
                createStringCell(row,data.getAdrTso(),4);
                createStringCell(row,data.getIpoYear(),5);
                createStringCell(row,data.getSector(),6);
                createStringCell(row,data.getIndustry(),7);
                createStringCell(row,data.getSummaryUrl(),8);
            }
        }
    }

    private void createNumericCell(Row row, Double value, int column) {
        Cell cell = row.createCell(column);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    private void createStringCell(Row row, String val, int column) {
        Cell cell = row.createCell(column);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(val);
    }
}
