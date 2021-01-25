
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class HttpClient {
    private String xml;
    private String url = "https://speller.yandex.net/services/spellservice/checkText?text=";
    private String res;
    private CloseableHttpResponse httpRes = null;

    //вводим слово
    private void inputWord() {
        System.out.println("Введите слово: ");
        Scanner in = new Scanner(System.in);
        res = in.nextLine().replaceAll(" ", "+");
        run();
    }

    //запускаем get запрос и сохраняем хмл ответ ввиде String
    private void run() {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url + res);
            httpRes = httpclient.execute(httpGet);
            xml = EntityUtils.toString(httpRes.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpRes.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //парсим String в документ
    public Document getXML()
            throws SAXException, ParserConfigurationException, IOException {
        inputWord();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }


}
