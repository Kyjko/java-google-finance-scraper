package data.gather.advanced;

import data.gather.base.DataFetcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AutomatedDataFetcher extends DataFetcher {

    private final HashMap<String, String> options;
    private final List<BigDecimal> fetchedDataList = Collections.synchronizedList(new ArrayList<>());
    private final Runnable dataFetchTask;

    public static final long MAX_LIST_SIZE = 10000L;
    public static final long MAX_INTERVAL_LENGTH = 100000L;

    public AutomatedDataFetcher(HashMap<String, String> options) {
        super(options);
        this.options = options;

        if(this.options.get("interval").equals("max")) {
            this.options.put("interval", String.valueOf(MAX_INTERVAL_LENGTH));
        }

        this.dataFetchTask = () -> {
            var interval = Integer.parseInt(this.options.get("interval"));
            var sleepTime = Integer.parseInt(this.options.get("sleep_time"));
            for(int i = 0; i < interval; i++) {
                try {
                    var res = super.getData();
                    if(res.equals(BigDecimal.ZERO)) {
                        System.exit(0);
                    }
                    fetchedDataList.add(res);
                    if(fetchedDataList.size() > MAX_LIST_SIZE) {
                        fetchedDataList.remove(0);
                    }
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
        };
    }

    public void getAutomatedData() {
        Executor ex = Executors.newSingleThreadExecutor();
        ex.execute(this.dataFetchTask);
        System.out.println("Getting data...");
    }

    public void compare() {
        try {
            var lastElem = fetchedDataList.get(fetchedDataList.size() - 1);
            var beforeLastElem = fetchedDataList.get(fetchedDataList.size() - 2);
            String prompt = (!lastElem.equals(beforeLastElem)) ? ("change : " + (lastElem.subtract(beforeLastElem))) : "";
            System.out.println(prompt);
        } catch(IndexOutOfBoundsException ignored) {}
    }

    public List<BigDecimal> getFetchedDataList() {
        return fetchedDataList;
    }

    public Runnable getDataFetchTask() {
        return dataFetchTask;
    }
}
