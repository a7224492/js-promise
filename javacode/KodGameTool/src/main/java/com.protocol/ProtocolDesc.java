package com.protocol;

import org.dom4j.Element;

/**
 * Created by jz on 2018/4/29.
 */
public class ProtocolDesc {
    static final String PROTO_CLASS_ATTR = "PROTOCOL_CLASS";
    static final String IS_AUTO_GENERATE_ATTR = "IS_AUTO_GENERATE";
    static final String PROTO_NAME_ATTR = "PROTO_NAME";

    private int startId;

    /**
     * 指向的proto协议类，PROTO_CLASS的值
     */
    private String protocolClass;

    /**
     * 是否是自动生成的
     */
    private boolean isAutoGenerate = false;

    /**
     * 协议名
     */
    private String name;

    public static ProtocolDesc create(int startId, Element protocolElem) {
        ProtocolDesc protocolDesc = new ProtocolDesc();
        protocolDesc.startId = startId;
        protocolDesc.protocolClass = protocolElem.attributeValue(PROTO_CLASS_ATTR);
        String isAutoGenerateValue = protocolElem.attributeValue(IS_AUTO_GENERATE_ATTR);
        if (isAutoGenerateValue != null && !isAutoGenerateValue.isEmpty()) {
            protocolDesc.isAutoGenerate = true;
        }
        protocolDesc.name = protocolElem.attributeValue(PROTO_NAME_ATTR);
        return protocolDesc;
    }

    public String getName()
    {
        return name;
    }

    public int getStartId()
    {
        return startId;
    }

    public String getProtocolClass()
    {
        return protocolClass;
    }

    public boolean isAutoGenerate()
    {
        return isAutoGenerate;
    }
}
