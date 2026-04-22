import com.ysh.dlt2811bean.utils.per.data2.CmsInt64U;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import java.math.BigInteger;

public class TestCmsInt64UFix {
    public static void main(String[] args) throws PerDecodeException {
        System.out.println("=== 测试修复后的 CmsInt64U ===");
        
        System.out.println("\n1. 测试常量:");
        System.out.println("CmsInt64U.MIN = " + CmsInt64U.MIN);
        System.out.println("CmsInt64U.MAX = " + CmsInt64U.MAX);
        System.out.println("MAX 的十进制值: " + CmsInt64U.MAX.toString());
        System.out.println("MAX 的十六进制值: " + CmsInt64U.MAX.toString(16));
        
        System.out.println("\n2. 测试编码解码 Long.MAX_VALUE:");
        try {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64U.encode(pos, Long.MAX_VALUE);
            System.out.println("编码 Long.MAX_VALUE: 成功");
            
            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt64U decoded = CmsInt64U.decode(pis);
            System.out.println("解码结果: " + decoded.getValue());
            System.out.println("是否相等: " + decoded.getValue().equals(BigInteger.valueOf(Long.MAX_VALUE)));
        } catch (IllegalArgumentException e) {
            System.out.println("编码 Long.MAX_VALUE: 失败 - " + e.getMessage());
        }
        
        System.out.println("\n3. 测试编码解码无符号最大值 (2^64-1):");
        try {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64U.encode(pos, CmsInt64U.MAX);
            System.out.println("编码 2^64-1: 成功");
            
            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt64U decoded = CmsInt64U.decode(pis);
            System.out.println("解码结果: " + decoded.getValue());
            System.out.println("是否相等: " + decoded.getValue().equals(CmsInt64U.MAX));
        } catch (IllegalArgumentException e) {
            System.out.println("编码 2^64-1: 失败 - " + e.getMessage());
        }
        
        System.out.println("\n4. 测试解码全1字节 (0xFFFFFFFFFFFFFFFF):");
        byte[] allOnes = new byte[] {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
                                     (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        PerInputStream pis2 = new PerInputStream(allOnes);
        CmsInt64U decodedMax = CmsInt64U.decode(pis2);
        System.out.println("解码全1字节的结果: " + decodedMax.getValue());
        System.out.println("是否等于 MAX: " + decodedMax.getValue().equals(CmsInt64U.MAX));
        
        System.out.println("\n5. 测试负值编码 (应该失败):");
        try {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64U.encode(pos, -1L);
            System.out.println("编码 -1L: 成功 (不应该发生)");
        } catch (IllegalArgumentException e) {
            System.out.println("编码 -1L: 失败 - " + e.getMessage() + " (正确)");
        }
        
        System.out.println("\n6. 测试超过最大值编码 (应该失败):");
        try {
            PerOutputStream pos = new PerOutputStream();
            BigInteger tooLarge = CmsInt64U.MAX.add(BigInteger.ONE);
            CmsInt64U.encode(pos, tooLarge);
            System.out.println("编码 MAX+1: 成功 (不应该发生)");
        } catch (IllegalArgumentException e) {
            System.out.println("编码 MAX+1: 失败 - " + e.getMessage() + " (正确)");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}