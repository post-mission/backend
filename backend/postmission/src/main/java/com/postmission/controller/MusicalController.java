package com.postmission.controller;

import com.postmission.model.MusicalDTO;
import com.postmission.model.MusicalInfo;
import com.postmission.service.MusicalService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/musical")
public class MusicalController {
    private final String url = "http://www.kopis.or.kr/openApi/restful/pblprfr";
    private final String SERVICE_KEY = "KOPIS에서 발급받은 서비스 키";
    private final String stdate = "19900101";
    private String eddate = "";
    private String genre = "AAAB";

    @Autowired
    private MusicalService serv;


    private static String getTagValue(String tag, Element eElement) {
        NodeList nodeList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nodeList.item(0);
        if (nValue == null) {
            return null;
        }
        return nValue.getNodeValue();
    }

    @GetMapping("/save")
    public List<MusicalInfo> checkData() throws IOException, ParseException {
        //db 티켓 한줄요약 추가

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        eddate = sdf.format(c1.getTime());

//        System.out.println(SERVICE_KEY);
//        System.out.println(stdate);
//        System.out.println(eddate);
        int page = 1;
        int numOfValues = 200;
        List<MusicalInfo> list = new ArrayList<>();
        try {
            while (true) {
                StringBuilder urlBuilder = new StringBuilder(url);
                urlBuilder.append("?").append(URLEncoder.encode("shcate", StandardCharsets.UTF_8)).append("=").append(genre);
                urlBuilder.append("&").append(URLEncoder.encode("service", StandardCharsets.UTF_8)).append("=").append(SERVICE_KEY); /*Service Key*/
                urlBuilder.append("&").append(URLEncoder.encode("stdate", StandardCharsets.UTF_8)).append("=").append(stdate);
                urlBuilder.append("&").append(URLEncoder.encode("eddate", StandardCharsets.UTF_8)).append("=").append(eddate);
                urlBuilder.append("&").append(URLEncoder.encode("rows", StandardCharsets.UTF_8)).append("=").append(numOfValues);
                urlBuilder.append("&").append(URLEncoder.encode("cpage", StandardCharsets.UTF_8)).append("=").append(page);
//                urlBuilder.append("&").append(URLEncoder.encode("shcate", StandardCharsets.UTF_8)).append("=").append(genre);

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(urlBuilder.toString());

                //root check
//            doc.getDocumentElement().normalize();
//            System.out.println("Root Element : " + doc.getDocumentElement().getNodeName());

                NodeList nodeList = doc.getElementsByTagName("db");
                System.out.println("파싱할 리스트 수 : " + nodeList.getLength());

                //파싱 종료 조건
//                if (nodeList.getLength() < numOfValues) {
//                    break;
//                }

                for (int temp = 0; temp < nodeList.getLength(); temp++) {
                    Node nNode = nodeList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nNode;
//                      System.out.println(getTagValue("mt20id", element)+"  "+getTagValue("prfnm", element));
                        MusicalInfo tempInfo = new MusicalInfo(getTagValue("mt20id", element), getTagValue("prfnm", element));
                        list.add(tempInfo);

                    }

                }
                page++;
                System.out.println(page);
                //테스트용 끊기
                if (page > 42) {
                    break;
                }
            } //while
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("파싱끝");
        return list;
    }

//    @GetMapping("/{search}")
//    public void getId(@PathVariable String search) {
//        try {
//            List<MusicalInfo> list = serv.findByNameContaining(search);
//            for (MusicalInfo m : list) {
//                System.out.println(m.getId() + " " + m.getName());
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @GetMapping("/title")
    public ResponseEntity<List<MusicalInfo>> getMusicalList(@RequestParam String title) {
        System.out.println("파라메터 = " +title);

        try {
            List<MusicalInfo> list = serv.findByNameContaining(title);
            for (MusicalInfo m : list) {
                System.out.println(m.getId() + " " + m.getName());
            }

            if(list != null && !list.isEmpty()) {
                return new ResponseEntity<List<MusicalInfo>>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }



    @GetMapping("/info")
    public ResponseEntity<MusicalDTO> getInfo(@RequestParam String id) {
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("/").append(id);
        urlBuilder.append("?").append(URLEncoder.encode("service", StandardCharsets.UTF_8)).append("=").append(SERVICE_KEY); /*Service Key*/

        System.out.println(urlBuilder.toString());

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(urlBuilder.toString());

            NodeList nodeList = doc.getElementsByTagName("db");
            Node nNode = nodeList.item(0);
            MusicalDTO musicalDTO = new MusicalDTO();
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                musicalDTO.setId(getTagValue("mt20id", element));
                musicalDTO.setName(getTagValue("prfnm", element));
                musicalDTO.setPlace(getTagValue("fcltynm", element));
                musicalDTO.setPoster(getTagValue("poster", element));
                musicalDTO.setRuntime(getTagValue("prfruntime", element));

                musicalDTO.setStartDate(getTagValue("prfpdfrom", element));
                musicalDTO.setEndDate(getTagValue("prfpdto", element));
                System.out.println(musicalDTO);

                if(musicalDTO != null) {
                    return new ResponseEntity<MusicalDTO>(musicalDTO, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                }
            }

        }catch (Exception e ) {
            e.printStackTrace();
        }


        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
