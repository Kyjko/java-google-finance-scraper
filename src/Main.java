import data.gather.advanced.AutomatedDataFetcher;
import data.gather.base.DataFetcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        HashMap<String, String> options = new HashMap<>();
        options.put("quote", "TSLA");
        options.put("print_data", "Y");
        options.put("interval", "10");
        options.put("sleep_time", "30");

        //DataFetcher df = new DataFetcher(options);
        AutomatedDataFetcher adf = new AutomatedDataFetcher(options);
        adf.getAutomatedData();
        List<BigDecimal> res = adf.getFetchedDataList();

        /*for(var r : res) {
            System.out.println(r);
        }*/

    }

}
