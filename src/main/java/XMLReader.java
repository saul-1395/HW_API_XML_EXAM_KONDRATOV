

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class XMLReader {

    private static boolean resultEmpty;
    private HttpClient httpClient;
    private Document doc;

    public  void printChecResult() {

        httpClient = new HttpClient();
        try {
            //получаем документ с xml
            doc= httpClient.getXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        // включаем поддержку пространства имен XML
        builderFactory.setNamespaceAware(true);

        // Создаем объект XPathFactory
        XPathFactory xpathFactory = XPathFactory.newInstance();

        // Получаем экзмепляр XPath для создания
        // XPathExpression выражений
        XPath xpath = xpathFactory.newXPath();
        //вызываем метод куда отдаём документ и xpath парсер
        String inputWord = getInputWord(doc, xpath);
        if (resultEmpty) {
            System.out.println("всё верно");
        } else {

            System.out.println("Ввели слово: " + inputWord);

            List<String> variant = getVariant(doc, xpath);
            System.out.println("Варианты: "
                    + variant.toString());

        }

    }


    private static List<String> getVariant(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            // получаем список всех узлов, которые отвечают условию
            XPathExpression xPathExpression = xpath.compile(
                    "/SpellResult/error/s/text()"
            );
            //получаем список узлов node
            NodeList nodeList = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++)
                list.add(nodeList.item(i).getNodeValue()); //получаем значение в каждом узле
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static String getInputWord(Document doc, XPath xpath) {
        String inputWord = null;
        try {
            XPathExpression xPathExpression = xpath.compile(
                    "/SpellResult/error/word/text()"
            );
            inputWord = (String) xPathExpression.evaluate(doc, XPathConstants.STRING);
            if (inputWord.isEmpty()) {
                resultEmpty = true;
            } else {
                resultEmpty = false;
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return inputWord;
    }

}