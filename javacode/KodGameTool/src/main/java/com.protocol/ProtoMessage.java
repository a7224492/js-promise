package com.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jz on 2018/4/29.
 */
public class ProtoMessage {
    private String name;
    private String type;
    private ProtoFile protoFile;
    private ProtocolCode successCode;
    private List<ProtocolCode> failCodeList = new ArrayList<>();
    private List<ProtocolCode> unmodifiedFailCodeList = Collections.unmodifiableList(failCodeList);

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    static List<String> needParse(String line, BufferedReader br) throws IOException {
        if (!line.trim().startsWith("//@auto_generator")) {
            return null;
        }

        List<String> result = new ArrayList<>();
        do {
            line = br.readLine();
            result.add(line);
        } while (br.ready() && !line.contains("}"));

        return result;
    }

    static ProtoMessage parse(ProtoFile protoFile, List<String> lineList) {
        ProtoMessage protoMessage = new ProtoMessage();
        protoMessage.protoFile = protoFile;

        for (String line : lineList) {
            if (line.trim().startsWith("message")) {
                protoMessage.name = line.substring("message ".length(), line.length()-3);
                protoMessage.type = line.substring(line.length()-3);
            }

            if (line.trim().startsWith("//@result")) {
                String[] codeLine = line.trim().split(" ");
                ProtocolCode protocolCode = new ProtocolCode(codeLine[1], codeLine[2]);
                if (protocolCode.name.equals("SUCCESS")) {
                    protoMessage.successCode = protocolCode;
                } else {
                    protoMessage.failCodeList.add(protocolCode);
                }
            }
        }
        return protoMessage;
    }

    public ProtocolCode getSuccessCode() {
        return successCode;
    }

    public List<ProtocolCode> getFailCodeList() {
        return unmodifiedFailCodeList;
    }

    public static class ProtocolCode
    {
        String name;
        String tip;

        ProtocolCode(String name, String tip) {
            this.name = name;
            this.tip = tip;
        }

        public String getName() {
            return name;
        }

        public String getTip() {
            return tip;
        }
    }
}
