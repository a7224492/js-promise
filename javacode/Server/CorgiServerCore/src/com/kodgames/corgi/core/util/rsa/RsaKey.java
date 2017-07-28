package com.kodgames.corgi.core.util.rsa;

/**
 * Created by Administrator on 2017/3/29.
 */
public class RsaKey {

    private String publicKey = "";		//公钥
	private String privateKey = "";		//私钥

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "RsaKey{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }

}
