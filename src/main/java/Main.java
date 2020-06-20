import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Surkov Aleksey (stibium128@gmail.com)
 * @date 19.06.2020 21:30
 */
public class Main {
    private static Logger logger;
    private  static StringBuilder stringArgs = new StringBuilder("Passed arguments: ");

    public static void main(String[] args) {
        logger = LogManager.getRootLogger();
        Arrays.stream(args).forEach(arg -> stringArgs.append(arg + " "));
        logger.info(stringArgs.toString());
        logger.info("Get all elements \"item\" without the attribute to \"exclude\"");
        NodeList nodes = new Parser(args).parsing();

        for (int i = 0; i < nodes.getLength(); i++) {
            String node = String.format(
                    "<item num='%s'>%s</item>",
                    nodes.item(i).getParentNode().getAttributes().item(0).getNodeValue(),
                    nodes.item(i).getNodeValue()
            );
            logger.info(node);
        }

        logger.info("Sum the values of those elements that are indicated in the arguments");
        double sum = new Summator(nodes, args, logger).sum();
        logger.info("Sum = " + sum);

        logger.info("Get the current Euro rate = " + getCurrentRate());
        logger.info("Result = " + (getCurrentRate() * sum));
    }

    private static double getCurrentRate() {
        String rates = "";
        double rate = 0.0;
        try {
            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = input.readLine()) != null){
                response.append(line);
            }
            input.close();
            rates = response.toString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(rates);
            rate = jsonNode.get("Valute").get("EUR").get("Value").asDouble();
        } catch (IOException | ArithmeticException e) {
            e.printStackTrace();
        }
        return rate;
    }
}
