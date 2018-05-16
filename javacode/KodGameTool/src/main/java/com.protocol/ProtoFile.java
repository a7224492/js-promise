package com.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by jz on 2018/4/29.
 */
class ProtoFile {
    private File file;
    private String packageName;
    private String className;
    private List<ProtoMessage> autoGenerateProtoMessage = new ArrayList<>();
    private List<ProtoMessage> unmodifiedAutoGenerateProtoMessage = Collections.unmodifiableList(autoGenerateProtoMessage);
    private int startId;

    private static Map<String, Integer> protoClass2StartId = new HashMap<>();

    static  {
        protoClass2StartId.put("AuthProtoBuf", 10001);
        protoClass2StartId.put("GameProtoBuf", 30001);
        protoClass2StartId.put("BattleProtoBuf", 40001);
        protoClass2StartId.put("ClubProtoBuf", 80001);
        protoClass2StartId.put("PushProtoBuf", 900001);
        protoClass2StartId.put("ReplayProtoBuf", 1000001);
        protoClass2StartId.put("CampaignProtoBuf", 1100001);
        protoClass2StartId.put("GoldProtoBuf", 90001);
        protoClass2StartId.put("ActivityProtoBuf", 30001);
        protoClass2StartId.put("ChatProtoBuf", 40001);
        protoClass2StartId.put("ContactProtoBuf", 30001);
        protoClass2StartId.put("MailProtoBuf", 30001);
        protoClass2StartId.put("MarqueeProtoBuf", 30001);
        protoClass2StartId.put("NoticeProtoBuf", 30001);
        protoClass2StartId.put("RoomProtoBuf", 30001);
    }

    static ProtoFile parseIfNeed(File file, BufferedReader br) throws IOException {
        List<String> parseLineList = needParse(br);
        if (parseLineList != null) {
            return parse(file, parseLineList);
        }

        return null;
    }

    static List<String> needParse(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (!line.trim().startsWith("package")) {
            return null;
        }

        List<String> result = new ArrayList<>();
        result.add(line);

        String nextLine = br.readLine();
        result.add(nextLine);
        return result;
    }

    static ProtoFile parse(File file, List<String> content) {
        ProtoFile protoFile = new ProtoFile();
        protoFile.file = file;

        String packageLine = content.get(0);
        protoFile.packageName = packageLine.substring("package ".length(), packageLine.length()-1);

        String classNameLine = content.get(1);
        protoFile.className = classNameLine.substring("option java_outer_classname = \"".length(), classNameLine.length()-2);

        protoFile.startId = protoClass2StartId.get(protoFile.className);
        return protoFile;
    }

    public boolean containProtocol(String protoName) {
        return unmodifiedAutoGenerateProtoMessage.stream().map(ProtoMessage::getName).filter(name -> name.equals(protoName)).findFirst().orElse(null) != null;
    }

    public void addAutoGenerateProtoMessage(ProtoMessage protoMessage) {
        this.autoGenerateProtoMessage.add(protoMessage);
    }

    public String getClassName() {
        return className;
    }

    public List<ProtoMessage> getAutoGenerateProtoMessage() {
        return unmodifiedAutoGenerateProtoMessage;
    }

    public int getStartId() {
        return startId;
    }

    String getPackageName() {
        return packageName;
    }
}
