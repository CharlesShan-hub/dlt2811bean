import com.ysh.dlt2811bean.utils.per.data2.CmsInt64U;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;

public class TestInt64U {
    public static void main(String[] args) throws PerDecodeException {
        System.out.println("CmsInt64U.MIN = " + CmsInt64U.MIN);
        System.out.println("CmsInt64U.MAX = " + CmsInt64U.MAX);
        System.out.println("Long.MAX_VALUE = " + Long.MAX_VALUE);
        
        // 测试 -1L 是否真的不能编码
        try {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64U.encode(pos, -1L);
            System.out.println("编码 -1L: 成功");
        } catch (IllegalArgumentException e) {
            System.out.println("编码 -1L: 失败 - " + e.getMessage());
        }
        
        // 测试 Long.MAX_VALUE 是否可以编码
        try {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64U.encode(pos, Long.MAX_VALUE);
            System.out.println("编码 Long.MAX_VALUE: 成功");
            
            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt64U decoded = CmsInt64U.decode(pis);
            System.out.println("解码结果: " + decoded.getValue());
        } catch (IllegalArgumentException e) {
            System.out.println("编码 Long.MAX_VALUE: 失败 - " + e.getMessage());
        }
        
        // 测试 decode 是否能处理全1的字节（即无符号最大值）
        byte[] allOnes = new byte[] {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
                                     (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        PerInputStream pis2 = new PerInputStream(allOnes);
        CmsInt64U decodedMax = CmsInt64U.decode(pis2);
        System.out.println("解码全1字节的结果: " + decodedMax.getValue());
        System.out.println("解码全1字节的十六进制: " + Long.toHexString(decodedMax.getValue()));
    }
}