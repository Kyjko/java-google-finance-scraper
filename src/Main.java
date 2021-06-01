import data.gather.advanced.AutomatedDataFetcher;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        HashMap<String, String> options = new HashMap<>();
        options.put("quote", "TSLA");
        options.put("print_data", "N");
        options.put("interval", "max");
        options.put("sleep_time", "10");

        //DataFetcher df = new DataFetcher(options);
        AutomatedDataFetcher adf = new AutomatedDataFetcher(options);
        adf.getAutomatedData();
        adf.compare();
        List<BigDecimal> res = adf.getFetchedDataList();
        /*for(var r : res) {
            System.out.println(r);
        }*/

    }

}
