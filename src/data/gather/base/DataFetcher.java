package data.gather.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class DataFetcher {
    private final HashMap<String, String> options;
    private final StringBuilder dataContent = new StringBuilder();
    private String baseURL = "https://www.google.com/finance/quote/";
    private String baseURLExtension = ":NASDAQ";

    public DataFetcher(final HashMap<String, String> options) {
        this.options = options;

        String bu, bux;
        if((bu = this.options.get("base_url")) != null) {
            this.baseURL = bu;
        }
        if((bux = this.options.get("base_url_extension")) != null) {
            this.baseURLExtension = bux;
        }
    }

    public BigDecimal getData() throws IOException {
        String sb = baseURL +
                this.options.get("quote") +
                baseURLExtension;

        URL url = new URL(sb);
        URLConnection urlConnection = url.openConnection();
        InputStreamReader ir = new InputStreamReader(urlConnection.getInputStream());
        BufferedReader br = new BufferedReader(ir);

        String line;
        while((line = br.readLine()) != null) {
            dataContent.append(line);
        }
        var stringDataContent = dataContent.toString();

        if (this.options.get("verbose") != null && this.options.get("verbose").equals("Y")) {
            System.out.println(stringDataContent);
        }

        var idx = stringDataContent.indexOf("<div class=\"YMlKec fxKbKc\">");
        if(idx != -1) {
            int i, j;
            i = idx;
            while (stringDataContent.charAt(i) != '$') {
                i++;
            }
            j = i;
            while (stringDataContent.charAt(j) != '<') {
                j++;
            }
            var resString = stringDataContent.substring(i, j);
            if (this.options.get("print_data") != null && this.options.get("print_data").equals("Y")) {
                System.out.println("===== Price of " + this.options.get("quote") + " : " + resString + " =====");
            }
            return new BigDecimal(resString.substring(1));
        } else {
            System.err.println("BAD URL");
            return BigDecimal.ZERO;
        }
    }

    public String getDataContentInString() {
        return this.dataContent.toString();
    }

    public HashMap<String, String> getOptions() {
        return this.options;
    }
}
