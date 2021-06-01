package data.gather.advanced;

import data.gather.base.DataFetcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutomatedDataFetcher extends DataFetcher {

    private final HashMap<String, String> options;
    private final List<BigDecimal> fetchedDataList = new ArrayList<>();

    public AutomatedDataFetcher(HashMap<String, String> options) {
        super(options);
        this.options = options;
    }

    public void getAutomatedData() {
        var interval = Integer.parseInt(this.options.get("interval"));
        var sleepTime = Integer.parseInt(this.options.get("sleep_time"));
        for(int i = 0; i < interval; i++) {
            try {
                var res = super.getData();
                fetchedDataList.add(res);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<BigDecimal> getFetchedDataList() {
        return fetchedDataList;
    }
}
