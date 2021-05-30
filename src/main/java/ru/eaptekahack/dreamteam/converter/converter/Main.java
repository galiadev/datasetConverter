package ru.eaptekahack.dreamteam.converter.converter;

import ru.eaptekahack.dreamteam.converter.output.ProductOutputData;

import java.io.IOException;
import java.util.List;

import static ru.eaptekahack.dreamteam.converter.converter.DatasetConverter.getOutputDataSet;
import static ru.eaptekahack.dreamteam.converter.converter.ExcelUtil.saveToExcel;

public class Main {
    public static void main(String[] args) throws IOException {

        List<ProductOutputData> outputDataset = getOutputDataSet().subList(0,1000);

        saveToExcel(outputDataset);
    }
}
