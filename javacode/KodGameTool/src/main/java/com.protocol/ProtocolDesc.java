package com.protocol;

import java.util.List;

/**
 * Created by jz on 2018/4/29.
 */
public class ProtocolDesc {
    private String desc;
    private String startId;
    private List<Protocol> protoList;

    private class Protocol{
        private String name;
        private String className;
    }
}
