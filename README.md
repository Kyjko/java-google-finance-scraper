# java-google-finance-scraper

## Get financial instrument data from the Google Finance API real-time!

Example:
 ```java
 public class Main {
    public static void main(String[] args) {

      // specify scraping options as a HashMap
      //  - print_data, graph : Y - yes, N - no
      //  - quote : a valid NASDAQ quote
      //  - interval : integer or "max" for maximum
      //  - sleep_time: integer (milliseconds) 
      
        HashMap<String, String> options = new HashMap<>();
        options.put("quote", "TSLA");
        options.put("print_data", "N");
        options.put("interval", "max");
        options.put("sleep_time", "10");
        options.put("graph", "Y");

        AutomatedDataFetcher adf = new AutomatedDataFetcher(options);
        adf.getAutomatedData();

    }

}
 ```
