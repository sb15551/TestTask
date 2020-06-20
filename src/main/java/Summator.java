import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;

/**
 * @author Surkov Aleksey (stibium128@gmail.com)
 * @date 19.06.2020 22:51
 */
public class Summator {
    private HashSet<String> nums;
    private NodeList nodeList;
    private String[] args;
    private double sum;
    private Logger logger;

    public Summator(NodeList nodeList, String[] args, Logger logger) {
        this.nodeList = nodeList;
        this.nums = new HashSet<>();
        this.logger = logger;
        this.args = args;
        sum = 0.0;
    }

    public double sum() {
        init();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (nums.contains(node.getParentNode().getAttributes().item(0).getNodeValue())) {
                double digit = Double.parseDouble(node.getNodeValue().replace(',','.'));
                logger.info(digit);
                sum += digit;
            }
        }
        return sum;
    }

    private void init() {
        for (String arg : args) {
            if (arg.matches("\\d+")) {
                nums.add(arg);
            }
        }
    }
}
