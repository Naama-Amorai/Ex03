package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
    private int _count;
    public Ex3Algo() {_count=0;}
    @Override
    /**
     *  Add a short description for the algorithm as a String.
     */
    public String getInfo() {
        return null;
    }
    @Override
    /**
     * This ia the main method - that you should design, implement and test.
     */
    public int move(PacmanGame game) {
        if(_count==0 || _count==300) {
            int code = 0;
            int[][] board = game.getGame(0);
            printBoard(board);
            int blue = Game.getIntColor(Color.BLUE, code);
            int pink = Game.getIntColor(Color.PINK, code);
            int black = Game.getIntColor(Color.BLACK, code);
            int green = Game.getIntColor(Color.GREEN, code);
            System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
            String pos = game.getPos(code).toString();
            System.out.println("Pacman coordinate: "+pos);
            GhostCL[] ghosts = game.getGhosts(code);
            printGhosts(ghosts);
            int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
        }
        _count++;
        int dir = randomDir();
        int blue = Game.getIntColor(Color.BLUE, 0);
        int[][] board = game.getGame(0);
        String posP = game.getPos(0).toString();
        String[] p = posP.split(",");
        int x = Integer.parseInt(p[0]);
        int y = Integer.parseInt(p[1]);
        Pixel2D pacman = new Index2D(x, y);
        GhostCL[] ghosts = game.getGhosts(0);
        Map2D map_board = new Map(board);
        Map2D dis = map_board.allDistance(pacman, blue);
        Map2D ghost_map = markghost(map_board , ghosts);
        Map2D safe_dis = ghost_map.allDistance(pacman, blue);
        Pixel2D closestGreen = closesetGreen(map_board, safe_dis);
        Pixel2D eatable = isEatable(dis , ghosts);
        if (eatable != null && dis.getPixel(eatable) < 10){
            return nextmove(map_board, eatable, pacman);
        }
        if (isGhostTooClose(dis ,  ghosts , whenrun())){
            Map closestghost = closestghost(map_board , dis,ghosts);
            return runaway(map_board , dis, closestghost, pacman);
        }
        if (closestGreen != null){
            return nextmove(map_board, closestGreen, pacman);
        }
        Pixel2D closestPink = closesetPink(map_board, safe_dis);
        return nextmove(map_board, closestPink, pacman);

    }
    private static void printBoard(int[][] b) {
        for(int y =0;y<b[0].length;y++){
            for(int x =0;x<b.length;x++){
                int v = b[x][y];
                System.out.print(v+"\t");
            }
            System.out.println();
        }
    }
    private static void printGhosts(GhostCL[] gs) {
        for(int i=0;i<gs.length;i++){
            GhostCL g = gs[i];
            System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
        }
    }
    private static int randomDir() {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        int ind = (int)(Math.random()*dirs.length);
        return dirs[ind];
    }

        public static int nextmove(Map2D map, Pixel2D closets, Pixel2D pos) {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        int blue = Game.getIntColor(Color.BLUE, 0);
        if (closets == null) {
            return randomDir();
        }
        Pixel2D[] path = map.shortestPath(pos, closets, blue);
        if (path == null || path.length < 2) {
            return randomDir();
        }
        Pixel2D next = path[1];
        if ((pos.getY() + 1) % map.getHeight() == next.getY()) {
            return dirs[0];
        }
        if ((pos.getX() - 1 + map.getWidth()) % map.getWidth() == next.getX()) {
            return dirs[1];
        }
        if ((pos.getY() - 1 + map.getHeight()) % map.getHeight() == next.getY()) {
            return dirs[2];
        }
        if ((pos.getX() + 1) % map.getWidth() == next.getX()) {
            return dirs[3];
        }
        return randomDir();
    }

        public static Pixel2D closesetPink(Map2D map, Map2D alldismap) {
        Pixel2D ans = null;
        int pink = Game.getIntColor(Color.PINK, 0);
        int minDis = Integer.MAX_VALUE;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == pink) {
                    int distvalue = alldismap.getPixel(x, y);
                    if (distvalue != -1 && minDis > distvalue) {
                        minDis = distvalue;
                        ans = new Index2D(x, y);
                    }
                }
            }
        }
        return ans;
    }

        public static Pixel2D closesetGreen(Map2D map, Map2D alldismap) {
        Pixel2D ans = null;
        int green = Game.getIntColor(Color.GREEN, 0);
        int minDis = 8;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == green) {
                    int distvalue = alldismap.getPixel(x, y);
                    if (distvalue != -1 && minDis > distvalue) {
                        minDis = distvalue;
                        ans = new Index2D(x, y);
                    }
                }
            }
        }
        return ans;
    }


    public static Map closestghost (Map2D map ,Map2D alldismap , GhostCL[] ghosts) {
        int blue = Game.getIntColor(Color.BLUE, 0);
        Map ans = new Map(map.getWidth(), map.getHeight(), 1000);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == blue) {
                    ans.setPixel(x, y, -1);
                }
            }
        }
        for (GhostCL current : ghosts) {
            String currentLocation = current.getPos(0).toString();
            Pixel2D ghost = getghostPixel(currentLocation);
            int ghostDist = alldismap.getPixel(ghost);
            double timeToReach = ghostDist  * GameInfo.DT / 1000.0;
            if (current.getStatus() != 0 && (timeToReach  > current.remainTimeAsEatable(0))) {
                    Map2D ghost_dist = map.allDistance(ghost, blue);
                    for (int x = 0; x < map.getWidth(); x++) {
                        for (int y = 0; y < map.getHeight(); y++) {
                            int d = ghost_dist.getPixel(x, y);
                            int currentVal = ans.getPixel(x, y);
                            if (currentVal != -1 && d != -1 && d < currentVal) {
                                ans.setPixel(x, y, d);
                            }
                        }
                    }
                }
            }
        return ans;
        }

        public static int runaway (Map2D map, Map2D dis_map, Map closestghost , Pixel2D pos){
            Pixel2D ans = null;
            int blue = Game.getIntColor(Color.BLUE, 0);
            int pink = Game.getIntColor(Color.PINK, 0);
            int green = Game.getIntColor(Color.GREEN, 0);
            int x = pos.getX();
            int y = pos.getY();
            Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
            if (map.isCyclic()){
                neighbor1 = new Index2D((x + 1) % map.getWidth(), y);
                neighbor2 = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
                neighbor3 = new Index2D(x, (y + 1) % map.getHeight());
                neighbor4 = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
            }
            else {
                neighbor1 = new Index2D(x + 1, y);
                neighbor2 = new Index2D(x - 1, y);
                neighbor3 = new Index2D( x , y + 1);
                neighbor4 = new Index2D( x, y - 1);
            }
            Pixel2D [] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
            int maxdist = Integer.MIN_VALUE;
            int good_neighbors = 0;
            for (Pixel2D n : neighbors) {
                if (!(((n.getX() == 21) || (n.getX() == 1)) && n.getY() == 13) && !isInCage(n)) {
                    int count = howManyneighbors(closestghost, n, closestghost.isCyclic());
                    if (closestghost.getPixel(n) >= maxdist && count > good_neighbors) {
                        good_neighbors = count;
                        maxdist = closestghost.getPixel(n);
                        ans = n;
                    }else if (closestghost.getPixel(n) == maxdist && count == good_neighbors && (map.getPixel(n) == pink ||map.getPixel(n) == green) ) {
                        ans = n;
                    } else if (closestghost.getPixel(n) > 7 && count > good_neighbors) {
                        good_neighbors = count;
                        maxdist = closestghost.getPixel(n);
                        ans = n;
                    } else if (closestghost.getPixel(n) > maxdist && count > 2) {
                        good_neighbors = count;
                        maxdist = closestghost.getPixel(n);
                        ans = n;
                    } else if (closestghost.getPixel(n) > maxdist) {
                        good_neighbors = count;
                        maxdist = closestghost.getPixel(n);
                        ans = n;
                    }
                }

            }
            if (ans == null) {return randomDir();}
            return nextmove(map , ans , pos);
        }


    public static Pixel2D isEatable(Map2D Distmap , GhostCL[] ghosts) {
        Pixel2D ans = null;
        int blue = Game.getIntColor(Color.BLUE, 0);
        int dis = Integer.MAX_VALUE ;
        for (GhostCL current : ghosts) {
            if (current.getStatus() != 0 && current.remainTimeAsEatable(0) > 0){
                String currentLocation = current.getPos(0).toString();
                String[] g = currentLocation.split(",");
                int x = Integer.parseInt(g[0]);
                int y = Integer.parseInt(g[1]);
                Pixel2D currenP = new Index2D(x, y);
                int distToPac = Distmap.getPixel(x , y);
                if (!isInCage(currenP) && distToPac != -1) {
                    double timeToReach = (distToPac + 2) * GameInfo.DT / 1000.0 ;
                    if (distToPac < dis && timeToReach < current.remainTimeAsEatable(0)){
                        dis  = distToPac;
                        ans = currenP;
                    }
                }
            }
        }
        return ans;
    }


        private static Pixel2D getghostPixel (String ghost_pos){
        String[] g = ghost_pos.split(",");
        int x = Integer.parseInt(g[0]);
        int y = Integer.parseInt(g[1]);
        Pixel2D ghost = new Index2D(x, y);
        return ghost;
    }

        public static Map2D markghost(Map2D mymap ,GhostCL[] ghosts ) {
        Map ans = new Map(mymap.getMap());
        int blue = Game.getIntColor(Color.BLUE, 0);
        for (int i = 0; i < ghosts.length; i++) {
            GhostCL current = ghosts[i];
            Pixel2D g = getghostPixel(current.getPos(0).toString());
            if (current.getStatus() != 0) {
                ans.setPixel(g , blue);
            }
        }
        return ans;
    }

        private boolean isGhostTooClose (Map2D dis , GhostCL[] ghosts, int limit){
        for (GhostCL g : ghosts) {
            if (g.getStatus() != 0 && g.remainTimeAsEatable(0) < 0.2 ) {
                Pixel2D gPos = getghostPixel(g.getPos(0).toString());
                int d = dis.getPixel(gPos);
                if ( !isInCage(gPos) && d != -1 && d <= limit ) {
                    return true;
                }
            }
        }
        return false;
    }

        private static boolean isInCage(Pixel2D p) {
        int x = p.getX();
        int y = p.getY();
        return (x >= 9 && x <= 13 && y >= 11 && y <= 12);
    }

        public static int whenrun(){
        if (GameInfo.DT  <= 50){return 8;}
        if (GameInfo.DT  <= 100){return 10;}
        if (GameInfo.DT  <= 150){return 13;}
        if (GameInfo.DT  <= 200){return 15;}
        return 10;
    }

        public static int howManyneighbors(Map2D map , Pixel2D n , boolean isCyclic) {
        Pixel2D neighbor1 , neighbor2 , neighbor3 , neighbor4 ;
        int counter = 0;
        int blue = Game.getIntColor(Color.BLUE, 0);
        int x = n.getX();
        int y = n.getY();
        if (isCyclic){
            neighbor1 = new Index2D((x + 1) % map.getWidth(), y);
            neighbor2 = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
            neighbor3 = new Index2D(x, (y + 1) % map.getHeight());
            neighbor4 = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
        }
        else {
            neighbor1 = new Index2D(x + 1, y);
            neighbor2 = new Index2D(x - 1, y);
            neighbor3 = new Index2D( x , y + 1);
            neighbor4 = new Index2D( x, y - 1);
        }
        Pixel2D [] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
        for (Pixel2D p : neighbors){
            if (map.isInside(p) && map.getPixel(p) != blue){
                counter++;
            }
        }
        return counter;
    }

}

