import com.nimbusds.jose.util.Base64;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * Base64Test
 * </p>
 *
 * @author livk
 * @date 2022/7/25
 */
public class Base64Test {
    @Test
    public void test() {
        System.out.println(Base64.encode("livk-client:secret"));
    }
}
