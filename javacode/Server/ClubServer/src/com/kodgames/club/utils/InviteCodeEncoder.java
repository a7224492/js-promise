package com.kodgames.club.utils;

public final class InviteCodeEncoder
{
    // 将RFC Base64编码的字符集最后两个字符修改为 #=, 补位用+
    // 这两个字符相对来说更易识别和输入 (标准中定义的是+/, 补位用=)
    private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#="
            .toCharArray();
    private static int[] toInt = new int[128];

    private static final int CodeLength = 8;                // 邀请码字符串的长度
    private static final int ByteArrayLength = 6;            // 邀请码字节数组长度

    private static final int MaxClubId = 1048576;            // Club 20bit, 最大值
    private static final int MaxRoleId = 268435456;        // Role 28bit, 最大值

    // 邀请码用clubId和roleId生成
    public static class ClubCodeInfo
    {
        int clubId;
        int roleId;

        public int getClubId() { return clubId; }
        public  int getRoleId() { return roleId; }
    }

    // 编码方法:
    // club取20bit, role取28bit, 共48bit
    // 采用base64的编码方法, 6bit编码为一个byte, 共8byte
    // 构造byte[]时将club和role合并后拆分为6个byte, 方法如下:
    //          club                                           role
    //   8      8      4                            4       8     8     8
    // ar[0] ar[1] ar[2]高4位                  ar[2]低4位 ar[3] ar[4] ar[5]

    public static String generateCode(int club, int role) throws Exception
    {
        if (club < 0 || club > MaxClubId)
            throw new Exception("Illegal clubId:" + club);
        if (role < 0 || role > MaxRoleId)
            throw new Exception("Illegal roleId:" + role);

        byte[] ar = new byte[ByteArrayLength];
        int c = club & 0x000FFFFF;        // club取20bit
        int r = role & 0x0FFFFFFF;        // role取28bit

        ar[0] = (byte) (c >>> 12);
        ar[1] = (byte) ((c & 0xFFF) >>> 4);
        ar[2] = (byte) ((c & 0xF) << 4);

        ar[2] |= (byte) (r >> 24);
        ar[3] = (byte) ((r & 0xFFFFFF) >>> 16);
        ar[4] = (byte) ((r & 0xFFFF) >>> 8);
        ar[5] = (byte) (r & 0xFF);

        return encode(ar);
    }

    public static ClubCodeInfo getClubInfo(String code) throws Exception
    {
        if (code.length() != CodeLength)
            throw new Exception("Illegal invitation code:" + code);

        byte[] ar = decode(code);
        if (ar.length != ByteArrayLength)
            throw new Exception("Illegal byte array for invitation code:" + code);

        //System.out.printf("byte data: %d, %d, %d, %d, %d, %d\n", ((int)ar[0] & 0xFF), ((int)ar[1] & 0xFF), ((int)ar[2] & 0xFF), ((int)ar[3] & 0xFF), ((int)ar[4] & 0xFF), ((int)ar[5] & 0xFF));

        int club = 0;
        int role = 0;

        // 注意:
        // Java中没有无符号数值类型
        // 将无符号byte转为int的正确方法为
        // int a = (int)ubyte_value & 0xFF

        club |= (((int) ar[0] & 0xFF) << 12);
        club |= (((int) ar[1] & 0xFF) << 4);
        club |= (((int) ar[2] & 0xFF) >>> 4);

        role |= ((((int) ar[2] & 0xFF) & 0xF) << 24);
        role |= (((int) ar[3] & 0xFF) << 16);
        role |= (((int) ar[4] & 0xFF) << 8);
        role |= ((int) ar[5] & 0xFF);

        ClubCodeInfo info = new ClubCodeInfo();
        info.clubId = club;
        info.roleId = role;
        return info;
    }


    static
    {
        for (int i = 0; i < ALPHABET.length; i++)
        {
            toInt[ALPHABET[i]] = i;
        }
    }

    /**
     * Translates the specified byte array into Base64 string.
     *
     * @param buf the byte array (not null)
     * @return the translated Base64 string (not null)
     */
    public static String encode(byte[] buf)
    {
        int size = buf.length;
        char[] ar = new char[((size + 2) / 3) * 4];
        int a = 0;
        int i = 0;
        while (i < size)
        {
            byte b0 = buf[i++];
            byte b1 = (i < size) ? buf[i++] : 0;
            byte b2 = (i < size) ? buf[i++] : 0;

            int mask = 0x3F;
            ar[a++] = ALPHABET[(b0 >> 2) & mask];
            ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
            ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
            ar[a++] = ALPHABET[b2 & mask];
        }
        switch (size % 3)
        {
            case 1:
                ar[--a] = '+';
            case 2:
                ar[--a] = '+';
        }
        return new String(ar);
    }

    /**
     * Translates the specified Base64 string into a byte array.
     *
     * @param s the Base64 string (not null)
     * @return the byte array (not null)
     */
    public static byte[] decode(String s)
    {
        int delta = s.endsWith("++") ? 2 : s.endsWith("+") ? 1 : 0;
        byte[] buffer = new byte[s.length() * 3 / 4 - delta];
        int mask = 0xFF;
        int index = 0;
        for (int i = 0; i < s.length(); i += 4)
        {
            int c0 = toInt[s.charAt(i)];
            int c1 = toInt[s.charAt(i + 1)];
            buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
            if (index >= buffer.length)
            {
                return buffer;
            }
            int c2 = toInt[s.charAt(i + 2)];
            buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
            if (index >= buffer.length)
            {
                return buffer;
            }
            int c3 = toInt[s.charAt(i + 3)];
            buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
        }
        return buffer;
    }


    // 单元测试
    public static void main(String[] args)
    {
        {
            String in = "string";
            String out = encode(in.getBytes());
            System.out.println("encode string: " + out);
            String back = new String(decode(out));
            if (back.equals(in))
                System.out.println("string encode and decode success");
            else
                System.out.println("string encode and decode failed");
        }

        {
            int club = 99;
            int role = 234423;
            String code;
            try
            {
                code = generateCode(club, role);
                System.out.println("code is " + code);

                ClubCodeInfo info = getClubInfo(code);
                System.out.println("club = " + info.clubId + ", role = " + info.roleId);
                if (info.roleId == role && info.clubId == club)
                    System.out.println("code encode and decode success");
                else
                    System.out.println("code encode and decode failed");
            } catch (Exception e)
            {
                System.out.println("exception: " + e.toString());
            }
        }
    }
}
