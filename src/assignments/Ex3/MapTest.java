package assignments.Ex3;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     *
     */
    private int[][] _map_3_3 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
    int[][] n = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
    private Map2D _m0 = new Map();
    private Map2D _m1 = new Map();
    private Map2D _m3_3 = new Map();

    @BeforeEach
    public void setuo() {
        _m3_3 = new Map(_map_3_3);
        _m0.init(n);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int[500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3, 2);
        _m1.fill(p1, 1);
    }

    @Test
    void init2() {
        int[][] arr = null;
        int[][] arr2 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10}};
        assertThrows(Exception.class, () -> {
            Map m = new Map(arr);
        });
        assertThrows(Exception.class, () -> {
            Map m = new Map(arr2);
        });
        Map2D m = new Map(n);
        assertEquals(_m0, m);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }

    @Test
    void testEquals() {
        _m0 = new Map();
        assertEquals(_m0, _m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }

    @Test
    public void testgetmap() {
        int[][] ans = _m3_3.getMap();
        assertArrayEquals(_map_3_3, ans);
    }

    @Test
    public void testgetWgetH() {
        assertEquals(3, _m0.getWidth());
        assertEquals(4, _m0.getHeight());
    }

    @Test
    public void testgetpixel() {
        int p1 = _m0.getPixel(1, 2);
        assertEquals(7, p1);
        assertThrows(Exception.class, () -> {
            int p2 = _m0.getPixel(5, 2);
        });
    }

    @Test
    public void testgetpixel2() {
        Pixel2D p1 = new Index2D(1, 2);
        int t = _m0.getPixel(p1);
        assertEquals(7, t);
        Pixel2D p2 = new Index2D(8, 2);
        assertThrows(Exception.class, () -> {
            int t2 = _m0.getPixel(p2);
        });
    }

    @Test
    public void testIsinside(){
        Pixel2D p1 = new Index2D(4,2);
        Pixel2D p2 = new Index2D(1,2);
        assertFalse(_m0.isInside(p1));
        assertTrue(_m0.isInside(p2));
    }
    @Test
    public void testsameDimensions(){
        Map m1 = new Map(3, 4, 0);
        Map m2 = new Map(3, 4, 0);
        Map m3 = new Map(2, 2, 0);

        assertFalse(m1.sameDimensions(m3));
        assertTrue(m1.sameDimensions(m2));    }
    @Test
    public void testadd(){
        int[][] data = {{1, 2}, {3, 4}};
        Map mSource = new Map(data);
        Map mTarget = new Map(2, 2, 0);
        mTarget.addMap2D(mSource);
        if (!mTarget.equals(mSource)) {
            fail("add failed");
        }
    }
    @Test
    public void testadd2(){
        Map m1 = new Map(3, 3, 0);
        Map m2 = new Map(10, 10, 0);
        assertThrows(Exception.class, () ->{
            m1.addMap2D(m2);
        });
    }


    @Test
    public void testequals() {
        Map _m1 = new Map(20,20,0);
        Map _m2 = new Map(20,20,0);
        Map _m3 = new Map(20,20,2);
        assertTrue(_m1.equals(_m2));
        assertFalse(_m1.equals(_m3));
    }
    @Test
    public void testfill() {
        Map _m1 = new Map(10, 10, 0);
        _m1.setCyclic(false);
        Pixel2D p1 = new Index2D(4, 0);
        Pixel2D p2 = new Index2D(8, 9);
        Pixel2D start = new Index2D(2, 2);
        _m1.drawRect(p1, p2, 1);
        _m1.fill(start, 2);
        _m1.setCyclic(true);
        assertEquals(2,_m1.getPixel(3,3));
        assertEquals(0,_m1.getPixel(9,5));
        assertEquals(1,_m1.getPixel(6,0));
        _m1.init(10, 10, 0);
        _m1.drawRect(p1, p2, 1);
        _m1.fill(start, 2);
        assertEquals(2,_m1.getPixel(3,3));
        assertEquals(2,_m1.getPixel(9,5));
        assertEquals(1,_m1.getPixel(6,0));
    }
    @Test
    public void testfill2() {
        Map _m1 = new Map(10, 10, 0);
        Pixel2D p1 = new Index2D(4, 0);
        Pixel2D p2 = new Index2D(8, 9);
        Pixel2D start = new Index2D(5, 5);
        _m1.drawRect(p1, p2, 1);
        _m1.fill(start, 2);
        assertEquals(0,_m1.getPixel(3,3));
        assertEquals(2,_m1.getPixel(6,0));
    }

    @Test
    public void testshortestPath() {
        Map _m1 = new Map(10,10,0);
        _m1.setCyclic(false);
        Pixel2D p1 = new Index2D(4,0);
        Pixel2D p2 = new Index2D(8,9);
        Pixel2D start = new Index2D(2,2);
        Pixel2D end = new Index2D(9,5);
        _m1.drawRect(p1,p2,1);
        Pixel2D [] ans = _m1.shortestPath(start,end,1);
        assertArrayEquals(null, ans);
    }
    @Test
    public void testshortestPath2() {
        Map _m1 = new Map(10,10,0);
        Pixel2D p1 = new Index2D(4,0);
        Pixel2D p2 = new Index2D(8,9);
        Pixel2D start = new Index2D(2,2);
        Pixel2D end = new Index2D(9,5);
        _m1.drawRect(p1,p2,1);
        Pixel2D [] ans = _m1.shortestPath(start,end,1);
        Pixel2D [] ans_expected = {new Index2D(2, 2), new Index2D(1, 2), new Index2D(0, 2), new Index2D(9, 2), new Index2D(9, 3),new Index2D(9, 4), new Index2D(9, 5)};
        assertArrayEquals(ans_expected, ans);
        Pixel2D [] worng_ans= {new Index2D(2, 2), new Index2D(1, 3), new Index2D(0, 2), new Index2D(7, 2)};
        if (Arrays.equals(ans,worng_ans)){
            fail();
        }
    }
    @Test
    public void allDistance() {
        Map mymap = new Map(5,5,0);
        Pixel2D p1 = new Index2D(1,0);
        Pixel2D p2 = new Index2D(2,4);
        Pixel2D start = new Index2D(1,1);
        mymap.drawRect(p1 , p2 , -1);
        Map2D ans = new Map();
        ans = mymap.allDistance(start , -1 );
        assertEquals(3,ans.getPixel(3,1));
        assertEquals(-1,ans.getPixel(2,1));
        assertEquals(0,ans.getPixel(start));
    }
    @Test
    public void allDistance2() {
        Map mymap = new Map(5,5,0);
        mymap.setCyclic(false);
        Pixel2D p1 = new Index2D(1,0);
        Pixel2D p2 = new Index2D(2,4);
        Pixel2D start = new Index2D(1,1);
        mymap.drawRect(p1 , p2 , -1);
        Map2D ans = new Map();
        ans = mymap.allDistance(start , -1);
        assertEquals(-1,ans.getPixel(3,1));
        assertEquals(1,ans.getPixel(0,1));
    }
}

