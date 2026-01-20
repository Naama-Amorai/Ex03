package assignments.Ex3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;


public class Index2DTest {
    static Object a = 0;
    static Index2D p1 = new Index2D(0, 0);
    static Index2D p2 = new Index2D(8, 1);
    static Index2D p3 = new Index2D(3, 3);
    static Index2D p4 = new Index2D(3, 3);
    static Index2D p = null;



    @Test
    public void Testdistance2D() {
        double expected = Math.sqrt(65);
        assertEquals(expected, p1.distance2D(p2));
    }
    @Test
    public void Testdistance2D1() {
        assertThrows(RuntimeException.class, () -> p1.distance2D(p));
    }

    @Test
    public void Testdistance2D2() {
        double expected = 0.0;
        assertEquals(expected, p3.distance2D(p3));
    }

    @Test
    public void TesttoString(){
        String expected = "Index X:  "+ p3.getX() + " ,Index Y: " + p3.getY();
        expected.equals(p3.toString());
    }
    @Test
    public void Testequals(){
        assertTrue(p3.equals(p4));
    }
    @Test
    public void Testequals2(){
        assertFalse(p1.equals(p4));
    }
    @Test
    public void Testequals3(){
        assertFalse(p1.equals(a));
    }

}
