import net.sf.json.JSONObject;
import utils.padUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TestScan {
    private String MsgLen;
    private String TxnId;
    private String AgentId;
    private String OprId;
    private String TxnLogId;
    private String BusinessId;
    private String TermNo;
    private String TradeName;
    private String PayChannel;
    private String LimitPay;
    private String TxnAmt;
    private String UserData;
    private String ExtendParams;
    private String TelNo = "11111111111";
    private String reqStr;
    private static final String keyStr = "5D9199121707450B";
    public static String keyflg="";

    private void b2cPay() throws Exception{
        MsgLen = "414";
        TxnId = "N001";
        AgentId = "3801020001";
        OprId = "01";
        TxnLogId = "00a0wm";
        BusinessId = "856020000024681";
        TermNo = "00000001";
        TradeName = "服务化测试";
        PayChannel = "2";
        LimitPay = "0";
        TxnAmt = "1";
        UserData = "134769929342983354";

        MsgLen = padUtils.pad(MsgLen, " ", 4);
        TxnId = padUtils.pad(TxnId, " ", 4);
        AgentId = padUtils.pad(AgentId, " ", 10);
        OprId = padUtils.pad(OprId, " ", 2);
        TxnLogId = padUtils.pad(TxnLogId, " ", 6);
        BusinessId = padUtils.pad(BusinessId, " ", 15);
        TermNo = padUtils.pad(TermNo, " ", 32);
        TradeName = padUtils.pad(TradeName, " ", 64);
        PayChannel = padUtils.pad(PayChannel, " ", 2);
        LimitPay = padUtils.pad(LimitPay, " ", 1);
        TxnAmt = padUtils.pad(TxnAmt, " ", 10);
        UserData = padUtils.pad(UserData, " ", 256);
        TelNo = padUtils.pad(TelNo, " ", 20);
        reqStr = MsgLen + TxnId + AgentId + OprId + TxnLogId + BusinessId + TermNo + TradeName + PayChannel + LimitPay + TxnAmt + UserData + TelNo;
        System.out.println(reqStr);

        sendCorg(reqStr);
    }

    private static void sendCorg(String reqStr) throws Exception{
        int msgLen = Integer.parseInt(reqStr.substring(0, 4).trim());
        String txnCod = reqStr.substring(4, 8);
        String macDataStr = reqStr.substring(0, reqStr.length() - 20);
        System.out.println(macDataStr.length());
        byte[] macByteArr = macDataStr.getBytes();
        int sendMsgLen = macByteArr.length + 8;
        int utfMsgLen = macDataStr.getBytes("utf-8").length + 8;
        System.out.println(msgLen==sendMsgLen);
        if (msgLen != sendMsgLen) {
            System.out.println("msgLen [" + msgLen + "]");
            System.out.println("sendMsgLen [" + sendMsgLen + "]");
            return;
        }

        if ("N001".equals(txnCod) || "N002".equals(txnCod)
                || "N007".equals(txnCod)) {
            byte[] merNameByte = new byte[64];
            System.arraycopy(macByteArr, 73, merNameByte, 0, 64);
            System.out.println("转码处理前:" + byte2hex(merNameByte));
            System.out.println("转码处理后:"
                    + byte2hex(new String(merNameByte).getBytes("UTF-8")));
            System.arraycopy(new String(merNameByte).getBytes("UTF-8"), 0,
                    macByteArr, 73, 64);
        }
        if ("N001".equals(txnCod) && macByteArr.length > 406) {
            byte[] ExtendParams = new byte[macByteArr.length - 406];
            System.arraycopy(macByteArr, 406, ExtendParams, 0,
                    macByteArr.length - 406);
            System.arraycopy(new String(ExtendParams).getBytes("UTF-8"), 0,
                    macByteArr, 406, macByteArr.length - 406);
        } else {
            System.out.println("macByteArr length less 406:" + macByteArr.length);
        }
        String telNo = reqStr.substring(reqStr.length() - 20,
                reqStr.length());

        String macHexStr = byte2hex(macByteArr);
        String telNoHexStr = byte2hex(telNo.getBytes());
        String Mac = "";
        try {
            String macData = macCal(macHexStr);
            Mac = DES_1(macData, keyStr, 0);
        } catch (Exception e1) {
        }
        String msg = macHexStr + Mac + telNoHexStr;

        String sign = "";

        sign = Bit32(msg);

        sendContent(msg,sign);
    }

    private static void sendContent(String msg,String sign) throws  Exception{
        URL url = new URL("http://dsfcs.postar.cn/mobilePre/pre/txnForHJ");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        connection.setRequestProperty("Content-Type","application/json");
        connection.connect();

        //POST请求
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        JSONObject obj = new JSONObject();
        obj.put("msg", msg);
        obj.put("sign", sign);
        String content = obj.toString();

        System.out.println("content:"+content);
        HashMap<Object, Object> aa= new HashMap<Object, Object>();


        out.write(msg.getBytes("GBK"));//这样可以处理中文乱码问题

        //     out.writeBytes(content);//这个中文会乱码  /
        out.flush();
        out.close();
        //读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
            System.out.println(lines);
        }

        reader.close();
        // 断开连接
        connection.disconnect();

    }
    public byte[] desCrypto(byte[] datasource, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final int[] iSelePM1 = new int[] { 57, 49, 41, 33, 25, 17,
            9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30,
            22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };
    private static final int[] iSelePM2 = new int[] { 14, 17, 11, 24, 1, 5, 3,
            28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41,
            52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32 };
    private static final int[] iROLtime = new int[] { 1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1 };
    private static final int[] iInitPM = new int[] { 58, 50, 42, 34, 26, 18,
            10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14,
            6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59,
            51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55,
            47, 39, 31, 23, 15, 7 };
    private static final int[] iInvInitPM = new int[] { 40, 8, 48, 16, 56, 24,
            64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62,
            30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33,
            1, 41, 9, 49, 17, 57, 25 };
    private static final int[] iEPM = new int[] { 32, 1, 2, 3, 4, 5, 4, 5, 6,
            7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18,
            19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29,
            30, 31, 32, 1 };
    private static final int[] iPPM = new int[] { 16, 7, 20, 21, 29, 12, 28,
            17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19,
            13, 30, 6, 22, 11, 4, 25 };
    private static final int[][] iSPM = new int[][] {
            { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7,
                    4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8,
                    13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4,
                    9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 },
            { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4,
                    7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11,
                    10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3,
                    15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 },
            { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0,
                    9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8,
                    15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9,
                    8, 7, 4, 15, 14, 3, 11, 5, 2, 12 },
            { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11,
                    5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12,
                    11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1,
                    13, 8, 9, 4, 5, 11, 12, 7, 2, 14 },
            { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2,
                    12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10,
                    13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14,
                    2, 13, 6, 15, 0, 9, 10, 4, 5, 3 },
            { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4,
                    2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2,
                    8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15,
                    10, 11, 14, 1, 7, 6, 0, 8, 13 },
            { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11,
                    7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13,
                    12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4,
                    10, 7, 9, 5, 0, 15, 14, 2, 3, 12 },
            { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13,
                    8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9,
                    12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10,
                    8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };
    private static int[] iCipherKey = new int[64];
    private static int[] iCKTemp = new int[56];
    private static int[] iPlaintext = new int[64];
    private static int[] iCiphertext = new int[64];
    private static int[] iPKTemp = new int[64];
    private static int[] iL = new int[32];
    private static int[] iR = new int[32];
    private static final int[][] s1 = new int[][] {
            { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };
    private static final int[][] s2 = new int[][] {
            { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };
    private static final int[][] s3 = new int[][] {
            { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };
    private static final int[][] s4 = new int[][] {
            { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };
    private static final int[][] s5 = new int[][] {
            { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };
    private static final int[][] s6 = new int[][] {
            { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };
    private static final int[][] s7 = new int[][] {
            { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };
    private static final int[][] s8 = new int[][] {
            { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };
    private static final int[] ip = new int[] { 58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64,
            56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51,
            43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47,
            39, 31, 23, 15, 7 };
    private static final int[] _ip = new int[] { 40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37,
            5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3,
            43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41,
            9, 49, 17, 57, 25 };
    private static final int[] LS = new int[] { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2,
            2, 2, 2, 2, 2, 1 };
    private static int[][] subKey = new int[16][48];
    private static int HEX = 0;
    private static int ASC = 1;

    private static void permu(int[] iSource, int[] iDest, int[] iPM) {
        for (int i = 0; i < iPM.length; ++i) {
            iDest[i] = iSource[iPM[i] - 1];
        }

    }

    private static void arrayBitToI(byte[] bArray, int[] iArray) {
        for (int i = 0; i < iArray.length; ++i) {
            iArray[i] = bArray[i / 8] >> 7 - i % 8 & 1;
        }

    }

    private static void arrayIToBit(byte[] bArray, int[] iArray) {
        for (int i = 0; i < bArray.length; ++i) {
            bArray[i] = (byte) iArray[8 * i];

            for (int j = 1; j < 8; ++j) {
                bArray[i] = (byte) (bArray[i] << 1);
                bArray[i] += (byte) iArray[8 * i + j];
            }
        }

    }

    private static void arrayM2Add(int[] array1, int[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array1[i] ^= array2[i];
        }

    }

    private static void arrayCut(int[] iSource, int[] iDest1, int[] iDest2) {
        int k = iSource.length;

        for (int i = 0; i < k / 2; ++i) {
            iDest1[i] = iSource[i];
            iDest2[i] = iSource[i + k / 2];
        }

    }

    private static void arrayComb(int[] iDest, int[] iSource1, int[] iSource2) {
        int k = iSource1.length;

        for (int i = 0; i < k; ++i) {
            iDest[i] = iSource1[i];
            iDest[i + k] = iSource2[i];
        }

    }

    private static void ROL(int[] array) {
        int temp = array[0];

        int i;
        for (i = 0; i < 27; ++i) {
            array[i] = array[i + 1];
        }

        array[27] = temp;
        temp = array[28];

        for (i = 0; i < 27; ++i) {
            array[28 + i] = array[28 + i + 1];
        }

        array[55] = temp;
    }

    private static int[][] invSubKeys(int[][] iSubKeys) {
        int[][] iInvSubKeys = new int[16][48];

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 48; ++j) {
                iInvSubKeys[i][j] = iSubKeys[15 - i][j];
            }
        }

        return iInvSubKeys;
    }

    private static void Sbox(int[] iInput, int iOffI, int[] iOutput, int iOffO,
                             int[] iSPM) {
        int iRow = iInput[iOffI] * 2 + iInput[iOffI + 5];
        int iCol = iInput[iOffI + 1] * 8 + iInput[iOffI + 2] * 4
                + iInput[iOffI + 3] * 2 + iInput[iOffI + 4];
        int x = iSPM[16 * iRow + iCol];
        iOutput[iOffO] = x >> 3 & 1;
        iOutput[iOffO + 1] = x >> 2 & 1;
        iOutput[iOffO + 2] = x >> 1 & 1;
        iOutput[iOffO + 3] = x & 1;
    }

    private static int[] encFunc(int[] iInput, int[] iSubKey) {
        int[] iTemp1 = new int[48];
        int[] iTemp2 = new int[32];
        int[] iOutput = new int[32];
        permu(iInput, iTemp1, iEPM);
        arrayM2Add(iTemp1, iSubKey);

        for (int i = 0; i < 8; ++i) {
            Sbox(iTemp1, i * 6, iTemp2, i * 4, iSPM[i]);
        }

        permu(iTemp2, iOutput, iPPM);
        return iOutput;
    }

    private static int[][] makeSubKeys(byte[] bCipherKey) {
        int[][] iSubKeys = new int[16][48];
        arrayBitToI(bCipherKey, iCipherKey);
        permu(iCipherKey, iCKTemp, iSelePM1);

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < iROLtime[i]; ++j) {
                ROL(iCKTemp);
            }

            permu(iCKTemp, iSubKeys[i], iSelePM2);
        }

        return iSubKeys;
    }

    private static byte[] encrypt(byte[] bPlaintext, int[][] iSubKeys) {
        byte[] bCiphertext = new byte[8];
        arrayBitToI(bPlaintext, iPlaintext);
        permu(iPlaintext, iPKTemp, iInitPM);
        arrayCut(iPKTemp, iL, iR);

        for (int i = 0; i < 16; ++i) {
            if (i % 2 == 0) {
                arrayM2Add(iL, encFunc(iR, iSubKeys[i]));
            } else {
                arrayM2Add(iR, encFunc(iL, iSubKeys[i]));
            }
        }

        arrayComb(iPKTemp, iR, iL);
        permu(iPKTemp, iCiphertext, iInvInitPM);
        arrayIToBit(bCiphertext, iCiphertext);
        return bCiphertext;
    }

    private static byte[] decrypt(byte[] bCiphertext, int[][] iSubKeys) {
        int[][] iInvSubKeys = invSubKeys(iSubKeys);
        return encrypt(bCiphertext, iInvSubKeys);
    }

    private static byte[] BitXor(byte[] Data1, byte[] Data2, int Len) {
        byte[] Dest = new byte[Len];

        for (int i = 0; i < Len; ++i) {
            Dest[i] = (byte) (Data1[i] ^ Data2[i]);
        }

        return Dest;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] b2 = new byte[b.length / 2];

            for (int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }

            return b2;
        }
    }

    public static String encryptDES(String strKey, String strEncData) {
        if (strKey.length() != 32) {
            throw new IllegalArgumentException("密钥长度不正确,必须为32");
        } else if (strEncData.length() != 32) {
            throw new IllegalArgumentException("数据明文长度不正确,必须为32");
        } else {
            String strKey1 = strKey.substring(0, 16);
            String strKey2 = strKey.substring(16, 32);
            String strTemp1 = strEncData.substring(0, 16);
            String strTemp2 = strEncData.substring(16, 32);
            byte[] cipherKey1 = hex2byte(strKey1.getBytes());
            byte[] cipherKey2 = hex2byte(strKey2.getBytes());
            byte[] bCiphertext1 = hex2byte(strTemp1.getBytes());
            byte[] bCiphertext2 = hex2byte(strTemp2.getBytes());
            int[][] subKeys1 = new int[16][48];
            int[][] subKeys2 = new int[16][48];
            subKeys1 = makeSubKeys(cipherKey1);
            subKeys2 = makeSubKeys(cipherKey2);
            byte[] bTemp11 = encrypt(bCiphertext1, subKeys1);
            byte[] bTemp21 = decrypt(bTemp11, subKeys2);
            byte[] bPlaintext11 = encrypt(bTemp21, subKeys1);
            byte[] bTemp12 = encrypt(bCiphertext2, subKeys1);
            byte[] bTemp22 = decrypt(bTemp12, subKeys2);
            byte[] bPlaintext12 = encrypt(bTemp22, subKeys1);
            return byte2hex(bPlaintext11) + byte2hex(bPlaintext12);
        }
    }

    public static String decryptDES(String strKey, String strEncData) {
        if (strKey.length() != 32) {
            throw new IllegalArgumentException("密钥长度不正确,必须为32");
        } else if (strEncData.length() != 32) {
            throw new IllegalArgumentException("数据密文长度不正确,必须为32");
        } else {
            String strKey1 = strKey.substring(0, 16);
            String strKey2 = strKey.substring(16, 32);
            String strTemp1 = strEncData.substring(0, 16);
            String strTemp2 = strEncData.substring(16, 32);
            byte[] cipherKey1 = hex2byte(strKey1.getBytes());
            byte[] cipherKey2 = hex2byte(strKey2.getBytes());
            byte[] bCiphertext1 = hex2byte(strTemp1.getBytes());
            byte[] bCiphertext2 = hex2byte(strTemp2.getBytes());
            int[][] subKeys1 = new int[16][48];
            int[][] subKeys2 = new int[16][48];
            subKeys1 = makeSubKeys(cipherKey1);
            subKeys2 = makeSubKeys(cipherKey2);
            byte[] bTemp11 = decrypt(bCiphertext1, subKeys1);
            byte[] bTemp21 = encrypt(bTemp11, subKeys2);
            byte[] bPlaintext11 = decrypt(bTemp21, subKeys1);
            byte[] bTemp12 = decrypt(bCiphertext2, subKeys1);
            byte[] bTemp22 = encrypt(bTemp12, subKeys2);
            byte[] bPlaintext12 = decrypt(bTemp22, subKeys1);
            return byte2hex(bPlaintext11) + byte2hex(bPlaintext12);
        }
    }

    public static int getIntByChar(char ch) throws Exception {
        char t = Character.toUpperCase(ch);
        boolean i = false;
        int i1;
        switch (t) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                i1 = Integer.parseInt(Character.toString(t));
                break;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            default:
                throw new Exception("getIntByChar was wrong");
            case 'A':
                i1 = 10;
                break;
            case 'B':
                i1 = 11;
                break;
            case 'C':
                i1 = 12;
                break;
            case 'D':
                i1 = 13;
                break;
            case 'E':
                i1 = 14;
                break;
            case 'F':
                i1 = 15;
        }

        return i1;
    }

    public static int[] string2Binary(String source) {
        int len = source.length();
        int[] dest = new int[len * 4];
        char[] arr = source.toCharArray();

        for (int i = 0; i < len; ++i) {
            int t = 0;

            try {
                t = getIntByChar(arr[i]);
            } catch (Exception arg8) {
                arg8.printStackTrace();
            }

            String[] str = Integer.toBinaryString(t).split("");
            int k = i * 4 + 3;

            for (int j = str.length - 1; j > 0; --j) {
                dest[k] = Integer.parseInt(str[j]);
                --k;
            }
        }

        return dest;
    }

    public static int getXY(int x, int y) {
        int temp = x;
        if (y == 0) {
            x = 1;
        }

        for (int i = 2; i <= y; ++i) {
            x *= temp;
        }

        return x;
    }

    public static String binary2Hex(String s) {
        int len = s.length();
        int result = 0;
        int k = 0;
        if (len > 4) {
            return null;
        } else {
            for (int i = len; i > 0; --i) {
                result += Integer.parseInt(s.substring(i - 1, i)) * getXY(2, k);
                ++k;
            }

            switch (result) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    return "" + result;
                case 10:
                    return "A";
                case 11:
                    return "B";
                case 12:
                    return "C";
                case 13:
                    return "D";
                case 14:
                    return "E";
                case 15:
                    return "F";
                default:
                    return null;
            }
        }
    }

    public static String int2Hex(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return "" + i;
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return null;
        }
    }

    public static String binary2ASC(String s) {
        String str = "";
        byte ii = 0;
        int len = s.length();
        if (len % 4 != 0) {
            while (ii < 4 - len % 4) {
                s = "0" + s;
            }
        }

        for (int i = 0; i < len / 4; ++i) {
            str = str + binary2Hex(s.substring(i * 4, i * 4 + 4));
        }

        return str;
    }

    public static int[] changeIP(int[] source) {
        int[] dest = new int[64];

        for (int i = 0; i < 64; ++i) {
            dest[i] = source[ip[i] - 1];
        }

        return dest;
    }

    public static int[] changeInverseIP(int[] source) {
        int[] dest = new int[64];

        for (int i = 0; i < 64; ++i) {
            dest[i] = source[_ip[i] - 1];
        }

        return dest;
    }

    public static int[] expend(int[] source) {
        int[] ret = new int[48];
        int[] temp = new int[] { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10,
                11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20,
                21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,
                1 };

        for (int i = 0; i < 48; ++i) {
            ret[i] = source[temp[i] - 1];
        }

        return ret;
    }

    public static int[] press(int[] source) {
        int[] ret = new int[32];
        int[][] temp = new int[8][6];
        int[][][] s = new int[][][] { s1, s2, s3, s4, s5, s6, s7, s8 };
        StringBuffer str = new StringBuffer();

        int i;
        int x;
        for (i = 0; i < 8; ++i) {
            for (x = 0; x < 6; ++x) {
                temp[i][x] = source[i * 6 + x];
            }
        }

        for (i = 0; i < 8; ++i) {
            x = temp[i][0] * 2 + temp[i][5];
            int y = temp[i][1] * 8 + temp[i][2] * 4 + temp[i][3] * 2
                    + temp[i][4];
            int val = s[i][x][y];
            String ch = int2Hex(val);
            str.append(ch);
        }

        ret = string2Binary(str.toString());
        ret = dataP(ret);
        return ret;
    }

    public static int[] dataP(int[] source) {
        int[] dest = new int[32];
        int[] temp = new int[] { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26,
                5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22,
                11, 4, 25 };
        int len = source.length;

        for (int i = 0; i < len; ++i) {
            dest[i] = source[temp[i] - 1];
        }

        return dest;
    }

    public static int[] f(int[] R, int[] K) {
        int[] dest = new int[32];
        int[] temp = new int[48];
        int[] expendR = expend(R);
        temp = diffOr(expendR, K);
        dest = press(temp);
        return dest;
    }

    public static int[] diffOr(int[] source1, int[] source2) {
        int len = source1.length;
        int[] dest = new int[len];

        for (int i = 0; i < len; ++i) {
            dest[i] = source1[i] ^ source2[i];
        }

        return dest;
    }

    public static String printIntArr(int[] source1){
        String arr="";
        for(int i=0;i<source1.length;i++){
            arr=arr+source1[i];
        }
        return arr;
    }

    public static String encryption(String D, String K) {

        String str = "";
        int[] temp = new int[64];

        int[] data = string2Binary(D);

        //System.out.println(D+"[data1:"+printIntArr(data)+"]");
        //logger.info(D+"[data1:"+printIntArr(data)+"]");
        data = changeIP(data);
        //System.out.println(D+"[data2:"+printIntArr(data)+"]");
        //logger.info(D+"[data2:"+printIntArr(data)+"]");
        int[][] left = new int[17][32];
        int[][] right = new int[17][32];

        int i;
        for (i = 0; i < 32; ++i) {
            left[0][i] = data[i];
            right[0][i] = data[i + 32];
        }

        setKey(K);

        for (i = 1; i < 17; ++i) {
            int[] key = subKey[i - 1];

            //	System.out.println(D+"[key"+i+"["+printIntArr(key)+"]");
            //logger.info(D+"[key"+i+"["+printIntArr(key)+"]");

            left[i] = right[i - 1];
            int[] fTemp = f(right[i - 1], key);
            right[i] = diffOr(left[i - 1], fTemp);
        }

        for (i = 0; i < 32; ++i) {
            temp[i] = right[16][i];
            temp[32 + i] = left[16][i];
        }

        //	System.out.println(D+"[temp1:"+printIntArr(temp)+"]");

        temp = changeInverseIP(temp);

        //	System.out.println(D+"[temp2:"+printIntArr(temp)+"]");
        //	logger.info(D+"[temp2:"+printIntArr(temp)+"]");
        str = binary2ASC(intArr2Str(temp));
        return str;
    }

    public static String discryption(String source, String key) {
        String str = "";
        int[] data = string2Binary(source);
        data = changeIP(data);
        int[] left = new int[32];
        int[] right = new int[32];
        int[] tmp = new int[32];

        int i;
        for (i = 0; i < 32; ++i) {
            left[i] = data[i];
            right[i] = data[i + 32];
        }

        setKey(key);

        for (i = 16; i > 0; --i) {
            int[] sKey = subKey[i - 1];
            tmp = left;
            left = right;
            int[] fTemp = f(right, sKey);
            right = diffOr(tmp, fTemp);
        }

        for (i = 0; i < 32; ++i) {
            data[i] = right[i];
            data[32 + i] = left[i];
        }

        data = changeInverseIP(data);

        for (i = 0; i < data.length; ++i) {
            str = str + data[i];
        }

        str = binary2ASC(str);
        return str;
    }

    public static String DES_1(String source, String key, int type) {
        return source.length() == 16 && key.length() == 16 ? (type == 0 ? encryption(
                source, key) : (type == 1 ? discryption(source, key) : null))
                : null;
    }

    public static String macCal(String msg) throws Exception {
        int dataLen = msg.length() / 2;
        byte[] tempByt = new byte[8];
        int rcn = dataLen / 8 + 1;
        int arg9 = rcn * 16 - msg.length();
        String arg10 = fillData(msg, arg9);
        byte[] bytArr = hex2byte(arg10.getBytes());

        for (int i = 0; i < rcn; ++i) {
            for (int j = 0; j < 8; ++j) {
                tempByt[j] ^= bytArr[i * 8 + j];
            }
        }

        return byte2hex(tempByt);
    }

    private static String fillData(String msg, int fillCnt) {
        StringBuffer strbf = new StringBuffer();

        for (int i = 0; i < fillCnt; ++i) {
            strbf.append("0");
        }

        return msg + strbf.toString();
    }

    public static int[] keyPC_1(int[] source) {
        int[] dest = new int[56];
        int[] temp = new int[] { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34,
                26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
                63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6,
                61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

        for (int i = 0; i < 56; ++i) {
            dest[i] = source[temp[i] - 1];
        }

        return dest;
    }

    public static int[] keyLeftMove(int[] source, int i) {
        boolean temp = false;
        int len = source.length;
        int ls = LS[i];

        for (int k = 0; k < ls; ++k) {
            int arg6 = source[0];

            for (int j = 0; j < len - 1; ++j) {
                source[j] = source[j + 1];
            }

            source[len - 1] = arg6;
        }

        return source;
    }

    public static int[] keyPC_2(int[] source) {
        int[] dest = new int[48];
        int[] temp = new int[] { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
                23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47,
                55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50,
                36, 29, 32 };

        for (int i = 0; i < 48; ++i) {
            dest[i] = source[temp[i] - 1];
        }

        return dest;
    }

    public static void setKey(String source) {

        if(keyflg.equals(source)){
            //System.out.println(" setkey return");
            return;
        }else{
            keyflg=source;
        }
        if (subKey.length > 0) {
            subKey = new int[16][48];
        }

        int[] temp = string2Binary(source);
        int[] left = new int[28];
        int[] right = new int[28];
        int[] temp1 = new int[56];
        temp1 = keyPC_1(temp);

        int i;
        for (i = 0; i < 28; ++i) {
            left[i] = temp1[i];
            right[i] = temp1[i + 28];
        }

        for (i = 0; i < 16; ++i) {
            left = keyLeftMove(left, LS[i]);
            right = keyLeftMove(right, LS[i]);

            for (int j = 0; j < 28; ++j) {
                temp1[j] = left[j];
                temp1[j + 28] = right[j];
            }

            subKey[i] = keyPC_2(temp1);
        }

    }

    public static void printArr(int[] source) {
        int len = source.length;

        for (int i = 0; i < len; ++i) {
            System.out.print(source[i]);
        }

        System.out.println();
    }

    public static String ASC_2_HEX(String asc) {
        StringBuffer hex = new StringBuffer();

        try {
            byte[] e = asc.toUpperCase().getBytes("UTF-8");
            byte[] arg5 = e;
            int arg4 = e.length;

            for (int arg3 = 0; arg3 < arg4; ++arg3) {
                byte b = arg5[arg3];
                hex.append(Integer.toHexString((new Byte(b)).intValue()));
            }
        } catch (Exception arg6) {
            arg6.printStackTrace();
        }

        return hex.toString();
    }

    public static String HEX_2_ASC(String hex) {
        String asc = null;
        int len = hex.length();
        byte[] bs = new byte[len / 2];

        for (int e = 0; e < len / 2; ++e) {
            bs[e] = Byte.parseByte(hex.substring(e * 2, e * 2 + 2), 16);
        }

        try {
            asc = new String(bs, "UTF-8");
        } catch (Exception arg4) {
            arg4.printStackTrace();
        }

        return asc;
    }

    public static String PBOC_DES_MAC(String key, String vector, String data,
                                      int type) {
        if (key.length() != 16) {
            return null;
        } else {
            if (type == ASC) {
                data = ASC_2_HEX(data);
            }

            int len = data.length();
            int arrLen = len / 16 + 1;
            String[] D = new String[arrLen];
            if (vector == null) {
                vector = "0000000000000000";
            }

            int I;
            if (len % 16 == 0) {
                data = data + "8000000000000000";
            } else {
                data = data + "80";

                for (I = 0; I < 15 - len % 16; ++I) {
                    data = data + "00";
                }
            }

            for (I = 0; I < arrLen; ++I) {
                D[I] = data.substring(I * 16, I * 16 + 16);
                System.out.println("D[" + I + "]=" + D[I]);
            }

            String arg9 = xOr(D[0], vector);
            String O = null;

            for (int i = 1; i < arrLen; ++i) {
                System.out.println(i + "**************");
                System.out.println("I=" + arg9);
                O = DES_1(arg9, key, 0);
                System.out.println("O=" + O);
                arg9 = xOr(D[i], O);
                System.out.println("I=" + arg9);
            }

            arg9 = DES_1(arg9, key, 0);
            return arg9;
        }
    }

    public static String xOr(String s1, String s2) {
        int[] iArr = diffOr(string2Binary(s1), string2Binary(s2));
        return binary2ASC(intArr2Str(iArr));
    }

    public static String intArr2Str(int[] arr) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; ++i) {
            sb.append(arr[i]);
        }

        return sb.toString();
    }

    public static String divData(String data, String key, int type) {
        String left = null;
        String right = null;
        if (type == HEX) {
            left = key.substring(0, 16);
            right = key.substring(16, 32);
        }

        if (type == ASC) {
            left = ASC_2_HEX(key.substring(0, 8));
            right = ASC_2_HEX(key.substring(8, 16));
        }

        data = DES_1(data, left, 0);
        data = DES_1(data, right, 1);
        data = DES_1(data, left, 0);
        return data;
    }

    public static String reverse(String source) {
        int[] data = string2Binary(source);
        int j = 0;
        int[] arg5 = data;
        int arg4 = data.length;

        for (int arg3 = 0; arg3 < arg4; ++arg3) {
            int i = arg5[arg3];
            data[j++] = 1 - i;
        }

        return binary2ASC(intArr2Str(data));
    }

    public static String getDPK(String issuerFlag, String appNo, String mpk) {
        StringBuffer issuerMPK = new StringBuffer();
        issuerMPK.append(divData(issuerFlag, mpk, 0));
        issuerMPK.append(divData(reverse(issuerFlag), mpk, 0));
        StringBuffer dpk = new StringBuffer();
        dpk.append(divData(appNo, issuerMPK.toString(), 0));
        dpk.append(divData(reverse(appNo), issuerMPK.toString(), 0));
        return dpk.toString();
    }

    public static String getDPK4Once(String data, String mpk) {
        StringBuffer dpk = new StringBuffer();
        dpk.append(divData(data, mpk, 0));
        dpk.append(divData(reverse(data), mpk.toString(), 0));
        return dpk.toString();
    }
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String Bit32(String SourceString) throws Exception {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(SourceString.getBytes());
        byte messageDigest[] = digest.digest();
        return toHexString(messageDigest);
    }

    public static String Bit16(String SourceString) throws Exception {
        return Bit32(SourceString).substring(8, 24);
    }
    public static void main(String[] args) throws Exception{
        TestScan scan = new TestScan();
        scan.b2cPay();
    }
}
