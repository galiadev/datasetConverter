package ru.eaptekahack.dreamteam.converter.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.eaptekahack.dreamteam.converter.output.ProductOutputData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {

    private static final String EXCEL_FILE = "output/demo.xlsx";

    private static final String EXCEL_HEADERS [] = {"Name", "Price", "InStock", "analog", "analogprice",
            "desc", "link", "analogdesc", "analoglink", "analogannotation", "annotation"};

    protected static void saveToExcel(List<ProductOutputData> outputDataset)  throws IOException {
        FileOutputStream out = new FileOutputStream(EXCEL_FILE);
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("capsule");

        int rowNum = 0;

        int cellNum = 0;

        Row row = sheet.createRow(rowNum++);
        for(String header : EXCEL_HEADERS) {
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(header);
        }

        for (ProductOutputData product : outputDataset) {
            row = sheet.createRow(rowNum++);
            cellNum = 0;
            Cell cell = row.createCell(cellNum);
            cell.setCellValue(product.getName());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getPrice());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.isInStock());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getCheapestAnalogName());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getCheapestAnalogPrice());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getDescription());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getLink());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getCheapestAnalogDescription());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getCheapestAnalogLink());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getCheapestAnalogAnnotation());
            cellNum++;

            cell = row.createCell(cellNum);
            cell.setCellValue(product.getAnnotation());
        }
        workbook.write(out);
        out.close();
        workbook.close();
    }
}
