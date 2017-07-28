package com.kodgames.corgi.core.util.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Administrator on 2017/3/29.
 */
public class RsaConfig 
{
    private static RsaConfig instance = new RsaConfig();
    private static RsaKey rsaKey;

    private RsaConfig()
    {
    }

    public static RsaConfig getInstance()
    {
        return instance;
    }

    /**
     * 解析配置文件，得到公钥和私钥
     * @throws IOException
     */
    public void parse() throws IOException
    {
        String name = RsaConfig.class.getClassLoader().getResource("rsa.json").getPath();
        File file = new File(name);
        
        InputStream inputStream = new FileInputStream(file);
        
        ObjectMapper mapper = new ObjectMapper();
        rsaKey = mapper.readValue(inputStream, RsaKey.class);
    }

    public static RsaKey getRsaKey() {
        return rsaKey;
    }
}
