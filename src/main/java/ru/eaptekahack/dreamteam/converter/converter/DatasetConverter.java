package ru.eaptekahack.dreamteam.converter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.eaptekahack.dreamteam.converter.input.AnalogActiveSubstance;
import ru.eaptekahack.dreamteam.converter.input.Basket;
import ru.eaptekahack.dreamteam.converter.input.Product;
import ru.eaptekahack.dreamteam.converter.output.Analog;
import ru.eaptekahack.dreamteam.converter.output.ProductOutputData;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DatasetConverter {

    private static final String BASKET_JSON = "dataset/basket.json";
    private static final String ANALOGS_JSON = "dataset/productToMNN.json";
    private static final String PRODUCTS_JSON = "dataset/products.json";


    private static Map<Long, Basket> convertBasketListToMap(List<Basket> basketList) {
        Set<Basket> basketSet = new HashSet<>(basketList);
        Map<Long, Basket> map = basketSet.stream()
                .collect(Collectors.toMap(Basket::getProductId, Function.identity()));
        return map;
    }

    private static Map<Long, Product> convertProductListToMap(List<Product> products) {
        Set<Product> basketSet = new HashSet<>(products);
        Map<Long, Product> map = basketSet.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        return map;
    }

    private static Map<Long, AnalogActiveSubstance> convertAnalogListToMap(List<AnalogActiveSubstance> analogList) {
        List<AnalogActiveSubstance> data = analogList.stream().filter(item -> item.getProductId() != null).collect(Collectors.toList());
        Set<AnalogActiveSubstance> basketSet = new HashSet<>(data);
        Map<Long, AnalogActiveSubstance> map = basketSet.stream()
                .collect(Collectors.toMap(AnalogActiveSubstance::getProductId, Function.identity()));
        return map;
    }

    private static Map<String, List<AnalogActiveSubstance>> convertToAnalogsByActiveSubstanceMap(Map<Long, AnalogActiveSubstance> analogsMap,
                                                                                                 Map<Long, Basket> basketMap) {
        Map<String, List<AnalogActiveSubstance>> result = new HashMap<>();
        analogsMap.forEach((k, v) -> {
            String activeSubstanceCode = v.getActiveSubstanceCode();

            AnalogActiveSubstance analogWithPrice = new AnalogActiveSubstance();
            analogWithPrice.copyParametersAndSetPrice(v, basketMap);

            if(result.containsKey(activeSubstanceCode)) {
                result.get(activeSubstanceCode).add(analogWithPrice);
                result.put(activeSubstanceCode, result.get(activeSubstanceCode));
            }
            else {
                List analogs = new ArrayList<>();
                analogs.add(analogWithPrice);
                result.put(activeSubstanceCode, analogs);
            }
        });
        return result;
    }

    public static List<ProductOutputData> getOutputDataSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<Basket> baskets = Arrays.asList(mapper.readValue(Paths.get(BASKET_JSON).toFile(), Basket[].class));

        Map<Long, Basket> productsFromOrderWithPriceMap = convertBasketListToMap(baskets);

        List<AnalogActiveSubstance> allAnalogs = Arrays.asList(mapper.readValue(Paths.get(ANALOGS_JSON).toFile(), AnalogActiveSubstance[].class));

        Map<Long, AnalogActiveSubstance> productsWithAnalogsMap = convertAnalogListToMap(allAnalogs);

        Map<String, List<AnalogActiveSubstance>> analogsByActiveSubstance = convertToAnalogsByActiveSubstanceMap(productsWithAnalogsMap, productsFromOrderWithPriceMap);

        List<Product> products = Arrays.asList(mapper.readValue(Paths.get(PRODUCTS_JSON).toFile(), Product[].class));

        Map<Long, Product> productMap = convertProductListToMap(products);

        List<ProductOutputData> outputData = new ArrayList<>();

        Set<String> cityUrls = new HashSet<>();

        products.forEach(product -> {
            String analogData = productsWithAnalogsMap.containsKey(product.getId()) ? productsWithAnalogsMap.get(product.getId()).getActiveSubstanceCode() : "";
            System.out.println(product.getId() + " " + product.getName() + " " + productsFromOrderWithPriceMap.get(product.getId()).getPrice() + " " + analogData);

            ProductOutputData outputProduct = new ProductOutputData();
            outputProduct.setId(product.getId());
            outputProduct.setName(product.getName());
            outputProduct.setPrice(productsFromOrderWithPriceMap.containsKey(product.getId()) ? productsFromOrderWithPriceMap.get(product.getId()).getPrice() : null);
            // TODO
            outputProduct.setDescription("");
            outputProduct.setAnnotation("");
            outputProduct.setInStock(true);

            //TODO
            String url = productsFromOrderWithPriceMap.get(product.getId()).getUrl();
            if(url != null) {
                int end = url.indexOf("id");
                if (end == -1) end = url.length() - 1;
                if (!cityUrls.contains(url)) {
                    String cityUrl = url.substring(0, end);
                    if(!cityUrl.equals( "/goods/")) cityUrls.add(cityUrl);
                }

                outputProduct.setLink("https://www.eapteka.ru" + url);
                String activeSubstanceName = productsWithAnalogsMap.containsKey(product.getId()) ? productsWithAnalogsMap.get(product.getId()).getActiveSubstanceName() : null;
                String activeSubstanceCode = productsWithAnalogsMap.containsKey(product.getId()) ? productsWithAnalogsMap.get(product.getId()).getActiveSubstanceCode() : null;
                if (activeSubstanceName != null && analogsByActiveSubstance.containsKey(activeSubstanceCode)) {
                    List<AnalogActiveSubstance> analogs = analogsByActiveSubstance.get(activeSubstanceCode).stream()
                            .filter(item ->
                                    item.getProductId() != null && !item.getProductId().equals(product.getId()))
                            .collect(Collectors.toList());
                    analogs.forEach(item ->
                            item.setName(productMap.containsKey(item.getProductId()) ? productMap.get(item.getProductId()).getName() : null));
                    analogs.stream().filter(item -> item.getName() != null).collect(Collectors.toList());
                    List<Analog> analogList = Analog.convert(analogs);
                    outputProduct.setActiveSubstance(activeSubstanceName);
                    outputProduct.setAnalogs(analogList);

                    if(!analogList.isEmpty()) {
                        DoubleSummaryStatistics statistics = analogList.stream()
                                .filter(a -> a.getPrice() != null && a.getName() != null)
                                .map(Analog::getPrice)
                                .mapToDouble(Double::doubleValue)
                                .summaryStatistics();
                        double min = statistics.getMin();

                        List<Analog> minAnalog = analogList.stream().filter(a -> a.getPrice() != null && a.getPrice().equals(min)).collect(Collectors.toList());
                        Analog analog = !minAnalog.isEmpty() ? minAnalog.get(0) : null;

                        if(analog != null) {
                            outputProduct.setCheapestAnalogName(analog.getName());
                            outputProduct.setCheapestAnalogPrice(analog.getPrice());

                            String analogUrl = productsFromOrderWithPriceMap.containsKey(analog.getId()) ? "https://www.eapteka.ru" + productsFromOrderWithPriceMap.get(analog.getId()).getUrl() : null;

                            outputProduct.setCheapestAnalogLink(analogUrl);
                            outputProduct.setCheapestAnalogDescription("");
                            outputProduct.setCheapestAnalogAnnotation("");
                        }
                    }
                }
                if (outputProduct.getPrice() != null && outputProduct.getActiveSubstance() != null && !outputProduct.getAnalogs().isEmpty()
                && outputProduct.getCheapestAnalogLink() != null)
                    outputData.add(outputProduct);
            }
        });

        List<ProductOutputData> result = outputData.stream().filter(p -> {
            int begin = p.getLink().indexOf(".ru");
            int end = p.getLink().indexOf("id");
            String s = p.getLink().substring(begin + 3, end);

            if(p.getCheapestAnalogLink().contains("null")) return false;

            int beginAnalog = p.getCheapestAnalogLink().indexOf(".ru");
            int endAnalog = p.getCheapestAnalogLink().indexOf("id");

            String s2 = p.getCheapestAnalogLink().substring(beginAnalog + 3, endAnalog);
            return !cityUrls.contains(s) && !cityUrls.contains(s2);
        }).collect(Collectors.toList());

        return result;
    }


}
