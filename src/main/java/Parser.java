import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

/**
 * @author Surkov Aleksey (stibium128@gmail.com)
 * @date 19.06.2020 22:05
 */
public class Parser {
    private String[] args;

    public Parser(String[] args) {
        this.args = args;
    }

    public NodeList parsing() {
        try {

            DocumentBuilder documentBuilder = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document document = documentBuilder.parse(getXMLfileName(args));
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList result = (NodeList) xPath
                    .compile("//item[not(@exclude='true')]/text()")
                    .evaluate(document, XPathConstants.NODESET);
            return result;

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getXMLfileName(String[] args) {
        for (String arg : args) {
            if (arg.matches(".+(\\.xml)$")) {
                return arg;
            }
        }
        return null;
    }
}
