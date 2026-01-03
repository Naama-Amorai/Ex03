package assignments.Ex3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
        init(w,h, v);
    }
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {
        this(size,size, 0);
    }
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
        this._map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j] = v;
            }
        }
		///////////////////////////////////
	}
	@Override
	public void init(int[][] arr) {
        if (arr == null) {
            throw new RuntimeException("error: null arr");
        }
        int w = arr.length;
        int h = arr[0].length;
        for (int i = 1; i < w; i++) {
            if (arr[i].length != h) {
                throw new RuntimeException("error: ragged 2D array");
            }
        }
        _map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j] = arr[i][j];
            }
        }
	}
	@Override
	public int[][] getMap() {
        int[][] ans = new int[_map.length][_map[0].length];
        for (int i = 0; i < _map.length; i++) {
            for (int j = 0; j < _map[0].length; j++) {
                ans[i][j] = _map[i][j];
            }
        }
        return ans;
	}
	@Override
	public int getWidth() {return _map.length;}

	@Override

	public int getHeight() {return _map[0].length;}
	@Override

	public int getPixel(int x, int y) {
        if (x < 0 || x >= _map.length) {
            throw new RuntimeException();
        }
        if (y < 0 || y >= _map[0].length) {
            throw new RuntimeException();
        }
        return _map[x][y];
    }
	@Override

	public int getPixel(Pixel2D p) {

        if (p.getX() < 0 || p.getX() >= _map.length) {
            throw new RuntimeException();
        }
        if (p.getY() < 0 || p.getY() >= _map[0].length) {
            throw new RuntimeException();
        }
        return _map[p.getX()][p.getY()];
    }

    @Override


	public void setPixel(int x, int y, int v) {
        if (x < 0 || x >= _map.length) {
        throw new RuntimeException();
    }
        if (y < 0 || y >= _map[0].length) {
            throw new RuntimeException();
        }
        _map[x][y] = v;
    }
	@Override
	public void setPixel(Pixel2D p, int v) {

        if (p.getX() < 0 || p.getX() >= _map.length) {
            throw new RuntimeException();
        }
        if (p.getY() < 0 || p.getY() >= _map[0].length) {
            throw new RuntimeException();
        }
        _map[p.getX()][p.getY()] = v;
	}

	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
        int counter = 0;
        if (xy == null || !isInside(xy)) {
            throw new RuntimeException();
        }
        int old_color = getPixel(xy);
        if (old_color == new_v) {return 0;}
        Queue<Pixel2D> q = new LinkedList<>();
        setPixel(xy, new_v);
        counter ++;
        q.add(xy);
        while (!q.isEmpty()) {
            Pixel2D current = q.poll();
            Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
            if (_cyclicFlag) {
                neighbor1 = new Index2D((current.getX() + 1) % this.getWidth(), current.getY());
                neighbor2 = new Index2D((current.getX() - 1 + this.getWidth()) % this.getWidth(), current.getY());
                neighbor3 = new Index2D(current.getX(), (current.getY() + 1) % this.getHeight());
                neighbor4 = new Index2D(current.getX(), (current.getY() - 1 + this.getHeight()) % this.getHeight());
            } else {
                neighbor1 = new Index2D(current.getX() + 1, current.getY());
                neighbor2 = new Index2D(current.getX() - 1, current.getY());
                neighbor3 = new Index2D(current.getX(), current.getY() + 1);
                neighbor4 = new Index2D(current.getX(), current.getY() - 1);
            }
            Pixel2D[] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
            for (Pixel2D n : neighbors) {
                if (isInside(n) && getPixel(n) == old_color) {
                    setPixel(n, new_v);
                    q.add(n);
                    counter++;
                }
            }
        }

        return counter;
    }
	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        Pixel2D[] ans = null;
        if (p1 == null || p2 == null || !isInside(p1) || !isInside(p2)){
            throw new RuntimeException();
        }
        if (p1.equals(p2)){
            ans = new Pixel2D[] {p1};
            return ans;
        }
        Pixel2D [][] savepath = new Pixel2D[this.getWidth()][this.getHeight()];
        savepath [p1.getX()][p1.getY()] = p1;
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(p1);
        while (!q.isEmpty()){
            Pixel2D current = q.poll();
            if (current.equals(p2)) {break;}
            Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
            if (_cyclicFlag){
                neighbor1 = new Index2D((current.getX() + 1) % this.getWidth(), current.getY());
                neighbor2 = new Index2D((current.getX() - 1 + this.getWidth()) % this.getWidth(), current.getY());
                neighbor3 = new Index2D(current.getX(), (current.getY() + 1) % this.getHeight());
                neighbor4 = new Index2D(current.getX(), (current.getY() - 1 + this.getHeight()) % this.getHeight());
            }
            else {
                neighbor1 = new Index2D(current.getX() + 1, current.getY());
                neighbor2 = new Index2D(current.getX() - 1, current.getY());
                neighbor3 = new Index2D(current.getX(), current.getY() + 1);
                neighbor4 = new Index2D(current.getX(), current.getY() - 1);
            }
            Pixel2D [] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
            for (Pixel2D n : neighbors){
                if (isInside(n) && getPixel(n) != obsColor){
                    if (savepath [n.getX()][n.getY()] == null) {
                        savepath[n.getX()][n.getY()] = current;
                        q.add(n);
                    }
                }

            }
        }
        if (savepath [p2.getX()][p2.getY()] == null){
            return ans;
        }
        else{
            ArrayList<Pixel2D> reverse = new ArrayList<>();
            reverse.add(p2);
            Pixel2D temp = p2;
            while (!temp.equals(p1)){
                temp = savepath[temp.getX()][temp.getY()] ;
                reverse.add(temp);
            }
            ans = new Pixel2D[reverse.size()];
            for (int i = ans.length-1 ; i >= 0; i--) {
                ans [i] = reverse.get(ans.length-1-i);
            }
        }
        return ans;
    }


	@Override
	public boolean isInside(Pixel2D p) {
        if (p == null) {
            return false;
        }
        if (this.getWidth() <= p.getX() || p.getX() < 0 || this.getHeight() <= p.getY() || p.getY() < 0) {
            return false;
        }
        return true;
    }

	@Override
	public boolean isCyclic() {
        if (this._cyclicFlag){
            return true;
        }
        return false;
	}
	@Override

	public void setCyclic(boolean cy) {
        if (cy){
            this._cyclicFlag = true;
        }
        else {
            this._cyclicFlag = false;
        }
    }
	@Override

	public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D ans = new Map(this.getWidth(), this.getHeight(), -1);
        ans.setPixel(start, 0);
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(start);
        while (!q.isEmpty()){
            Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
            Pixel2D current = q.poll();
            if (_cyclicFlag){
                neighbor1 = new Index2D((current.getX() + 1) % this.getWidth(), current.getY());
                neighbor2 = new Index2D((current.getX() - 1 + this.getWidth()) % this.getWidth(), current.getY());
                neighbor3 = new Index2D(current.getX(), (current.getY() + 1) % this.getHeight());
                neighbor4 = new Index2D(current.getX(), (current.getY() - 1 + this.getHeight()) % this.getHeight());
            }
            else {
                neighbor1 = new Index2D(current.getX() + 1, current.getY());
                neighbor2 = new Index2D(current.getX() - 1, current.getY());
                neighbor3 = new Index2D(current.getX(), current.getY() + 1);
                neighbor4 = new Index2D(current.getX(), current.getY() - 1);
            }
            Pixel2D [] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
            for (Pixel2D n : neighbors){
                if (isInside(n) && getPixel(n) != obsColor && ans.getPixel(n) == -1){
                    ans.setPixel(n , (ans.getPixel(current)+1) );
                    q.add(n);
                }
            }
        }
        return ans;
    }
	}
