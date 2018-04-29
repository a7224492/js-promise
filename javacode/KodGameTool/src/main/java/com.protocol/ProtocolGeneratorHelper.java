package com.protocol;

import com.kodgames.message.generaor.Main;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jz on 2018/4/29.
 */
public class ProtocolGeneratorHelper {
    private static final String PROTOCOL_DESC_FILE_PATH = "C:\\Users\\jz\\Desktop\\kodgame\\message\\src\\main\\resources\\ProtocolDesc.xml";
    private static final String PROTO_GENERATOR_BAT_PATH = "C:\\Users\\jz\\Desktop\\kodgame\\message\\proto\\compile_proto_files.bat";
    private static final String PROTO_DIR_PATH = "C:\\Users\\jz\\Desktop\\kodgame\\message\\proto\\protobuf";

    public static void main(String[] args) throws IOException, DocumentException, InterruptedException {
        File protoDir = new File(PROTO_DIR_PATH);
        if (!protoDir.exists()) {
            exit(-1, "proto dir path doesn't exist!");
        }

        if (!protoDir.isDirectory()) {
            exit(-2, "proto dir path is not directory!");
        }

        // read all proto file
        File[] protoFileArr = protoDir.listFiles((dir, name) -> name.endsWith(".proto"));
        if (protoFileArr == null) {
            exit(-3, "can't find proto file");
            return;
        }

        Map<Integer, List<ProtoFile>> startId2ProtoFile = new HashMap<>();
        for (File file : protoFileArr) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            ProtoFile protoFile = null;

            while (br.ready()) {
                // parse proto file
                List<String> protoFileStringList = ProtoFile.needParse(br.readLine(), br);
                if (protoFileStringList != null) {
                    protoFile = ProtoFile.parse(file, protoFileStringList);
                    break;
                }
            }

            if (protoFile == null) {
                System.err.println("can't find package and proto class in proto file");
                continue;
            }

            while (br.ready()) {
                // parse proto message
                List<String> protoMessageStringList = ProtoMessage.needParse(br.readLine(), br);
                if (protoMessageStringList != null) {
                    ProtoMessage protoMessage = ProtoMessage.parse(protoFile, protoMessageStringList);
                    protoFile.addAutoGenerateProtoMessage(protoMessage);
                }
            }

            br.close();
            if (protoFile.getAutoGenerateProtoMessage().size() > 0) {
                List<ProtoFile> protoFiles = startId2ProtoFile.computeIfAbsent(protoFile.getStartId(), k -> new ArrayList<>());
                protoFiles.add(protoFile);
            }
        }

        // read protocol desc xml file
		SAXReader reader = new SAXReader();
        Document doc = reader.read(new FileInputStream(PROTOCOL_DESC_FILE_PATH));
        Element root = doc.getRootElement();
        List<Element> paragraphList = root.element("ParagraphCollection").elements("Paragraph");

        for (Element paragraph : paragraphList) {
            int startId = 0;
            try {
                startId = Integer.parseInt(paragraph.element("ProtocolStartID").getText());
            } catch (NumberFormatException e) {
                System.out.println(e.toString());
                continue;
            }

            List<ProtoFile> protoFiles = startId2ProtoFile.get(startId);
            if (protoFiles == null) {
                continue;
            }

            for (ProtoFile protoFile : protoFiles) {
                for (ProtoMessage protoMessage : protoFile.getAutoGenerateProtoMessage()) {
                    String protocolName = protoName2ProtocolName(protoMessage);
                    String protoClass = protoFile.getPackageName()+"."+protoFile.getClassName()+"."+protoMessage.getName()+protoMessage.getType();

                    // if protocol exist, don't add again
                    List<Element> protocolList = paragraph.element("ProtocolCollection").elements("Protocol");
                    if (protocolList.stream().filter(e -> e.attribute(protocolName) != null).count() > 0) {
                        continue;
                    }

                    // add protocl element
                    Element protocolElem = paragraph.element("ProtocolCollection").addElement("Protocol");
                    protocolElem.addAttribute(protocolName, "");
                    protocolElem.addAttribute("PROTOCOL_CLASS", protoClass);

                    // add protocol code desc if need
                    if (protoMessage.getSuccessCode() != null) {
                        // add success protocol code
                        Element successElem = protocolElem.addElement("String");
                        successElem.addAttribute("name", upperAnd_split(protoMessage.getName())+"_"+protoMessage.getSuccessCode().getName());
                        successElem.addAttribute("isSuccess", "true");
                        successElem.addText(protoMessage.getSuccessCode().getTip());
                    }

                    if (protoMessage.getFailCodeList().size() > 0) {
                        // add fail protocol code
                        for (ProtoMessage.ProtocolCode failCode : protoMessage.getFailCodeList()) {
                            Element failElem = protocolElem.addElement("String");
                            failElem.addAttribute("name", upperAnd_split(protoMessage.getName())+"_"+failCode.getName());
                            failElem.addText(failCode.getTip());
                        }
                    }
                }
            }
        }

        XMLWriter writer = new XMLWriter(new FileOutputStream(PROTOCOL_DESC_FILE_PATH), OutputFormat.createPrettyPrint());
        writer.write(doc);

        // exec proto generator bat
        Process exec = Runtime.getRuntime().exec("cmd /c start "+PROTO_GENERATOR_BAT_PATH, null, new File(PROTO_GENERATOR_BAT_PATH+"\\.."));
        exec.waitFor();
        killProcess();

        // exec protocol generator
        Main.main(null);
    }

    private static void exit(int code, String msg) {
        System.err.println(msg);
        System.exit(code);
    }

    private static String protoName2ProtocolName(ProtoMessage protoMessage) {
        return "P_" + upperAnd_split(protoMessage.getName()) + "_" + protoMessage.getType();
    }

    private static String upperAnd_split(String protoName) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < protoName.length(); ++i) {
            if (protoName.charAt(i) >= 'A' && protoName.charAt(i) <= 'Z' && i != protoName.length()-1 && i != 0) {
                result.append("_");
            }
            result.append(Character.toUpperCase(protoName.charAt(i)));
        }

        return result.toString();
    }

    private static void killProcess() {
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            rt.exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}