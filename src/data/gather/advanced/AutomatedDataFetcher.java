package data.gather.advanced;

import data.gather.base.DataFetcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class AutomatedDataFetcher extends DataFetcher {

    private final HashMap<String, String> options;
    private final List<BigDecimal> fetchedDataList = Collections.synchronizedList(new ArrayList<>());
    private final Callable<Integer> dataFetchTask;

    public static final long MAX_LIST_SIZE = 5L;
    public static final long MAX_INTERVAL_LENGTH = 100000L;

    private BigDecimal superGetData() {
        try {
            return super.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    public AutomatedDataFetcher(HashMap<String, String> options) {
        super(options);
        this.options = options;

        if(this.options.get("interval").equals("max")) {
            this.options.put("interval", String.valueOf(MAX_INTERVAL_LENGTH));
        }

        this.dataFetchTask = () -> {
            var interval = Integer.parseInt(options.get("interval"));
            var sleepTime = Integer.parseInt(options.get("sleep_time"));
            for (int i = 0; i < interval; i++) {
                var res = superGetData();
                if (res.equals(BigDecimal.ZERO)) {
                    return -1;
                }
                fetchedDataList.add(res);
                if (fetchedDataList.size() > MAX_LIST_SIZE) {
                    fetchedDataList.remove(0);
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return 0;
        };
    }

    public void getAutomatedData() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<Integer> f = ex.submit(this.dataFetchTask);
        System.out.println("Getting data...");
        Integer x = null;
        try {
            x = f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(x != null && x < 0) {
            System.err.println("ERROR in getAutomatedData!");
            ex.shutdown();
        }
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

    public Callable<Integer> getDataFetchTask() {
        return dataFetchTask;
    }
}
