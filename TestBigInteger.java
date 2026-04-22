import java.math.BigInteger;

public class TestBigInteger {
    public static void main(String[] args) {
        BigInteger max = new BigInteger("18446744073709551615"); // 2^64-1
        System.out.println("MAX = " + max);
        System.out.println("MAX.toByteArray().length = " + max.toByteArray().length);
        
        byte[] bytes = max.toByteArray();
        System.out.print("MAX.toByteArray() = [");
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(bytes[i]);
        }
        System.out.println("]");
        
        // 取最后8个字节
        byte[] last8 = new byte[8];
        int srcPos = Math.max(0, bytes.length - 8);
        int destPos = Math.max(0, 8 - bytes.length);
        int length = Math.min(8, bytes.length);
        System.arraycopy(bytes, srcPos, last8, destPos, length);
        
        System.out.print("last8 bytes = [");
        for (int i = 0; i < last8.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(last8[i]);
        }
        System.out.println("]");
        
        // 重建 BigInteger
        BigInteger reconstructed = new BigInteger(1, last8); // 1 means positive (unsigned)
        System.out.println("reconstructed = " + reconstructed);
        System.out.println("reconstructed.equals(max) = " + reconstructed.equals(max));
        System.out.println("reconstructed.compareTo(max) = " + reconstructed.compareTo(max));
        
        // 检查二进制表示
        System.out.println("\n二进制检查:");
        System.out.println("MAX 的二进制长度: " + max.bitLength());
        System.out.println("reconstructed 的二进制长度: " + reconstructed.bitLength());
        
        // 手动计算
        BigInteger manual = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
        System.out.println("manual (2^64-1) = " + manual);
        System.out.println("manual.equals(max) = " + manual.equals(max));
    }
}