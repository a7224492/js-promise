package com.protocol;

import com.kodgames.message.generaor.Main;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
    public static final String PROTOCOL_DESC_FILE_PATH = "E:\\Users\\Administrator\\Desktop\\server_http\\message\\src\\main\\resources\\ProtocolDesc.xml";
    private static final String PROTOCOL_FILE_PATH = "E:\\Users\\Administrator\\Desktop\\server_http\\message\\protocode\\ProtocolCode_Platform.lua";
    private static final String PROTO_GENERATOR_BAT_DIR = "E:\\Users\\Administrator\\Desktop\\server_http\\message\\proto";
    private static final String PROTO_GENERATOR_BAT_NAME = "compile_proto_files.bat";

    private static final String CLIENT_COMMON_DIR_PATH = "E:\\Users\\Administrator\\Desktop\\Common";
    private static final String COMMIT_BAT_PATH = "C:\\Users\\kod\\Desktop\\MyJavaCode\\javacode\\KodGameTool\\svn_commit.bat";

    public static void main(String[] args) throws IOException, DocumentException, InterruptedException {
        File protoDir = Paths.get(PROTO_GENERATOR_BAT_DIR, "protobuf").toFile();
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
                protoFile = ProtoFile.parseIfNeed(file, br);
                if (protoFile != null) {
                    break;
                }
            }

            if (protoFile == null) {
                System.err.println("can't find package and proto class in proto file");
                continue;
            }

            while (br.ready()) {
                // parse proto message
                ProtoMessage protoMessage = ProtoMessage.parseIfNeed(protoFile, br);
                if (protoMessage != null) {
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
        Document doc = new SAXReader().read(new FileInputStream(PROTOCOL_DESC_FILE_PATH));
        Element root = doc.getRootElement();

        // parse paragrah
        List<Element> paragraphList = root.element("ParagraphCollection").elements("Paragraph");
        for (Element paragraph : paragraphList) {
            // get paragraph start id
            int startId = 0;
            try {
                startId = Integer.parseInt(paragraph.element("ProtocolStartID").getText());
            } catch (NumberFormatException e) {
                System.out.println(e.toString());
                continue;
            }

            List<ProtoFile> startIdProtoFiles = startId2ProtoFile.get(startId);
            if (startIdProtoFiles == null) {
                continue;
            }

            // add protocol desc from proto file
            for (ProtoFile protoFile : startIdProtoFiles) {
                for (ProtoMessage protoMessage : protoFile.getAutoGenerateProtoMessage()) {
                    String protocolName = protoName2ProtocolName(protoMessage);
                    String protoClass = protoFile.getPackageName()+"."+protoFile.getClassName()+"."+protoMessage.getName()+protoMessage.getType();

                    // if protocol exist, don't add again
                    List<Element> protocolList = paragraph.element("ProtocolCollection").elements("Protocol");
                    if (protocolList.stream().filter(e -> e.attribute(protocolName) != null).count() > 0) {
                        continue;
                    }

                    // add protocol element
                    Element protocolElem = paragraph.element("ProtocolCollection").addElement("Protocol");
                    protocolElem.addAttribute(protocolName, "");
                    protocolElem.addAttribute(ProtocolDesc.PROTO_CLASS_ATTR, protoClass);
                    protocolElem.addAttribute(ProtocolDesc.IS_AUTO_GENERATE_ATTR, "true");
                    protocolElem.addAttribute(ProtocolDesc.PROTO_NAME_ATTR, protoMessage.getName());

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

            // delete protocol desc not in proto file
            Element paragraghElem = paragraph.element("ProtocolCollection");
            List<Element> protocolElemList = paragraghElem.elements("Protocol");
            for (Element protocolElem : protocolElemList) {
                ProtocolDesc protocolDesc = ProtocolDesc.create(startId, protocolElem);
                if (!protocolDesc.isAutoGenerate()) {
                    continue;
                }

                // if in proto files
                boolean exist = false;
                for (ProtoFile startIdProtoFile : startIdProtoFiles) {
                   if (startIdProtoFile.containProtocol(protocolDesc.getName())) {
                       exist = true;
                       break;
                   }
                }

                if (exist) {
                    continue;
                }

                // proto desc doesn't exist in proto files, remove
                paragraghElem.remove(protocolElem);
            }
        }

        XMLWriter writer = new XMLWriter(new FileOutputStream(PROTOCOL_DESC_FILE_PATH), OutputFormat.createPrettyPrint());
        writer.write(doc);

        // exec proto generator bat
        Process exec = Runtime.getRuntime().exec("cmd /c start "+Paths.get(PROTO_GENERATOR_BAT_DIR, PROTO_GENERATOR_BAT_NAME), null, new File(PROTO_GENERATOR_BAT_DIR));
        exec.waitFor();

        // exec protocol generator
        Main.main(null);

        // copy and commit protocol files
        copyAndCommitProtocolFiles();
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

    private static void copyAndCommitProtocolFiles()
        throws IOException, InterruptedException
    {
        // copy proto files
        Files.walkFileTree(Paths.get(PROTO_GENERATOR_BAT_DIR, "protobuf"), new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException
            {
                Files.copy(file, Paths.get(CLIENT_COMMON_DIR_PATH, "platform_jar","proto", file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

        // copy protocol files
        Files.walkFileTree(Paths.get(PROTO_GENERATOR_BAT_DIR, "pb"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, Paths.get(CLIENT_COMMON_DIR_PATH, "Protocols", "pb", file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

        // copy protocol desc file
        Files.copy(Paths.get(PROTOCOL_FILE_PATH), Paths.get(CLIENT_COMMON_DIR_PATH, "platform_jar", "protocode", "ProtocolCode_Platform.lua"), StandardCopyOption.REPLACE_EXISTING);

        // commit all to svn
        Files.copy(Paths.get(COMMIT_BAT_PATH), Paths.get(CLIENT_COMMON_DIR_PATH, "commit.bat"), StandardCopyOption.REPLACE_EXISTING);
        Process exec = Runtime.getRuntime().exec("cmd /c start " + Paths.get(CLIENT_COMMON_DIR_PATH, "commit.bat"), null, new File(CLIENT_COMMON_DIR_PATH));
        exec.waitFor();
    }
}