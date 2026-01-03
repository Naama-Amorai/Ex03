package assignments.Ex3;

public class Index2D implements Pixel2D {
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }
    public double distance2D(Pixel2D t) {
        if (t == null){
            throw new RuntimeException("illegal pixel");
        }
        if (t.getX() == this._x && t.getY() == this._y){
            return 0.0;
        }
        double ans = Math.sqrt(Math.pow(t.getX() - this._x,2) + Math.pow(t.getY() - this._y,2));
        return ans;
    }
    @Override
    public String toString() {
        return getX()+","+getY();
    }
    @Override
    public boolean equals(Object t) {
        boolean ans = false;
       /////// you do NOT need to add your code below ///////
        if(t instanceof Pixel2D) {
            Pixel2D p = (Pixel2D) t;
            ans = (this.distance2D(p)==0);
        }
       ///////////////////////////////////
        return ans;
    }
}
