package assignments.Ex3;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

//כל מה שעשיתי עד עכשיו 05/01 יש פה כמה וארסיות לכל קוד , מתחילה בex3algo מחדש
public class Myalgo_new implements PacManAlgo {
    private int _count;

    public Myalgo_new() {
        _count = 0;
    }

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
            if (_count == 0 || _count == 300) {
                int code = 0;
                int[][] board = game.getGame(0);
                printBoard(board);
                int blue = Game.getIntColor(Color.BLUE, code);
                int pink = Game.getIntColor(Color.PINK, code);
                int black = Game.getIntColor(Color.BLACK, code);
                int green = Game.getIntColor(Color.GREEN, code);
                System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
                String pos = game.getPos(code).toString();
                System.out.println("Pacman coordinate: " + pos);
                GhostCL[] ghosts = game.getGhosts(code);
                printGhosts(ghosts);
                int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
            }
            int blue = Game.getIntColor(Color.BLUE, 0);
            int[][] board = game.getGame(0);
            String pos = game.getPos(0).toString();
            String[] p = pos.split(",");
            int x = Integer.parseInt(p[0]);
            int y = Integer.parseInt(p[1]);
            Pixel2D pacpos = new Index2D(x, y);
            Map2D map_board = new Map(board);
            Map2D dis = map_board.allDistance(pacpos, blue);
            _count++;
            GhostCL[] ghosts = game.getGhosts(0);
            Pixel2D closestEatable = isEatable(dis , ghosts);
            if (closestEatable != null) {
                if (dis.getPixel(closestEatable) != -1 && dis.getPixel(closestEatable) <= (GameInfo.DT/15)) {
                    return nextmove(map_board, closestEatable, pacpos);
                }
            }
            Pixel2D dangerous_closestghost = closestghost(ghosts , dis);
            Pixel2D closestGreen = closesetGreen(map_board, dis);
            if (dangerous_closestghost != null){
                if (closestGreen != null && dis.getPixel(closestGreen) != -1 && dis.getPixel(closestGreen) <= 4 && aliveghost(ghosts)){
                    if( dis.getPixel(dangerous_closestghost) > 3 ) {
                        return nextmove(map_board, closestGreen, pacpos);
                    }
                }
                if (dis.getPixel(dangerous_closestghost) != -1) {
                    return runaway(map_board, dangerous_closestghost, pacpos);
                }
            }
            Pixel2D closestPink = closesetPink(map_board, dis);
            return nextmove(map_board ,closestPink , pacpos);
        }

    private static void printBoard(int[][] b) {
        for (int y = 0; y < b[0].length; y++) {
            for (int x = 0; x < b.length; x++) {
                int v = b[x][y];
                System.out.print(v + "\t");
            }
            System.out.println();
        }
    }

    private static void printGhosts(GhostCL[] gs) {
        for (int i = 0; i < gs.length; i++) {
            GhostCL g = gs[i];
            System.out.println(i + ") status: " + g.getStatus() + ",  type: " + g.getType() + ",  pos: " + g.getPos(0) + ",  time: " + g.remainTimeAsEatable(0));
        }
    }

    private static int randomDir() {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        int ind = (int) (Math.random() * dirs.length);
        return dirs[ind];
    }

    public static Pixel2D closesetPink(Map2D map, Map2D alldismap) {
        Pixel2D ans = null;
        int pink = Game.getIntColor(Color.PINK, 0);
        int minDis = Integer.MAX_VALUE;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == pink)
                {
                    int distvalue = alldismap.getPixel(x, y);
                    if ( distvalue != -1 && minDis > distvalue) {
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
        int minDis = 10;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == green)
                {
                    int distvalue = alldismap.getPixel(x, y);
                    if ( distvalue != -1 && minDis > distvalue) {
                        minDis = distvalue;
                        ans = new Index2D(x, y);
                    }
                }
            }
        }
        return ans;
    }

    public static int nextmove(Map2D map, Pixel2D closets, Pixel2D pos) {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        int blue = Game.getIntColor(Color.BLUE, 0);
        Pixel2D [] path = map.shortestPath(pos, closets, blue);
        if (path == null || path.length < 2){
            return randomDir();
        }
        Pixel2D next = path[1];
        if ((pos.getY() + 1 ) % map.getHeight() == next.getY()){
            return dirs[0];
        }
        if ((pos.getX() - 1 + map.getWidth())%map.getWidth() == next.getX()){
            return dirs[1];
        }
        if ((pos.getY() - 1 + map.getHeight()) % map.getHeight() == next.getY()){
            return dirs[2];
        }
        else {
            return dirs[3];
        }
    }
    public static Pixel2D closestghost(GhostCL[] ghosts , Map2D alldismap) {
        int minDistance = Integer.MAX_VALUE;
        Pixel2D ans = null;
        for (int i = 0; i < ghosts.length; i++) {
            GhostCL current = ghosts[i];
            String currentLocation = current.getPos(0).toString();
            String[] g = currentLocation.split(",");
            int x = Integer.parseInt(g[0]);
            int y = Integer.parseInt(g[1]);
            Pixel2D ghost = new Index2D(x, y);
            int ghostDist = alldismap.getPixel(ghost);
            boolean isInCage = (x >= 8 && x <= 14 && y >= 11 && y <= 12);
            if (ghostDist != -1 && !isInCage ) {
                if (current.getStatus() != 0 && ((current.remainTimeAsEatable(0) == 0) )) {
                    if (ghostDist < minDistance) {
                        minDistance = ghostDist;
                        ans = ghost;
                    }
                }
            }
        }

        return ans;
    }

//        public static Pixel2D closestghost(GhostCL[] ghosts , Map2D alldismap) {
//            int minDistance = Integer.MAX_VALUE;
//            Pixel2D ans = null;
//            for (int i = 0; i < ghosts.length; i++) {
//                GhostCL current = ghosts[i];
//                String currentLocation = current.getPos(0).toString();
//                String[] g = currentLocation.split(",");
//                int x = Integer.parseInt(g[0]);
//                int y = Integer.parseInt(g[1]);
//                Pixel2D ghost = new Index2D(x, y);
//                int ghostDist = alldismap.getPixel(ghost);
//                if (ghostDist != -1) {
//                    double timeToReach = (ghostDist + 3) * GameInfo.DT / 1000.0;
//                    if (current.getStatus() != 0 && ((current.remainTimeAsEatable(0) == 0) || (timeToReach  > current.remainTimeAsEatable(0)))) {
//                        if (ghostDist < minDistance) {
//                            minDistance = ghostDist;
//                            ans = ghost;
//                        }
//                    }
//                }
//            }
//
//            return ans;
    //           }

//        public static int runaway(Map2D map, Pixel2D closestghost, Pixel2D pos) {
//            int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
//            int blue = Game.getIntColor(Color.BLUE, 0);
//            Pixel2D [] path = map.shortestPath(pos, closestghost, blue);
//            if (path == null || path.length < 2){
//                return randomDir();
//            }
//            Pixel2D next = path[1];
//            if ((pos.getY() + 1 ) % map.getHeight() == next.getY()){
//                return dirs[2];
//            }
//            if ((pos.getX() - 1 + map.getWidth())%map.getWidth() == next.getX()){
//                return dirs[3];
//            }
//            if ((pos.getY() - 1 + map.getHeight()) % map.getHeight() == next.getY()){
//                return dirs[0];
//            }
//            else {
//                return dirs[1];
//            }
//        }



    //        public static int runaway(Map2D map, Pixel2D closestghost, Pixel2D pos) {
//            int blue = Game.getIntColor(Color.BLUE, 0);
//            int pink = Game.getIntColor(Color.PINK, 0);
//            int green = Game.getIntColor(Color.GREEN, 0);
//
//            int x = pos.getX();
//            int y = pos.getY();
//            Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
//            if (map.isCyclic()){
//                neighbor1 = new Index2D((x + 1) % map.getWidth(), y);
//                neighbor2 = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
//                neighbor3 = new Index2D(x, (y + 1) % map.getHeight());
//                neighbor4 = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
//            }
//            else {
//                neighbor1 = new Index2D(x + 1, y);
//                neighbor2 = new Index2D(x - 1, y);
//                neighbor3 = new Index2D( x , y + 1);
//                neighbor4 = new Index2D( x, y - 1);
//            }
//            Pixel2D [] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
//            int maxDist = -1 ;
//            int maxNeighbors = -1;
//            Pixel2D ans = null;
//            for (Pixel2D n : neighbors){
//                if (map.isInside(n) && map.getPixel(n) != blue) {
//                    Pixel2D[] nsp = map.shortestPath(closestghost, n, blue);
//                    if (nsp != null) {
//                        int count = howManyneighbors(map, n, map.isCyclic());
//                        if (nsp.length >= maxDist || count >= maxNeighbors) {
//                            if  (count > maxNeighbors && nsp.length > 3 ){
//                                maxNeighbors = count;
//                                maxDist = nsp.length;
//                                ans = n;
//                            }
//                            else if (nsp.length > maxDist && count > 1 ) {
//                                maxNeighbors = count;
//                                maxDist = nsp.length;
//                                ans = n;
//                            }
//                            else if  (nsp.length == maxDist && count > maxNeighbors){
//                                maxNeighbors = count;
//                                maxDist = nsp.length;
//                                ans = n;
//                            }
//                             else if (nsp.length == maxDist && count == maxNeighbors && (map.getPixel(n) == pink || map.getPixel(n) == green)) {
//                                ans = n;
//                            }
//                            else if (maxNeighbors <= 1 && nsp.length > maxDist) {
//                                maxNeighbors = count;
//                                maxDist = nsp.length;
//                                ans = n;
//                            }
//                        }
//                    }
//                }
//            }
//            if (ans == null) {return randomDir();}
//            return nextmove(map , ans , pos);
//
//            }
    //תיקון ג'ימי
    public static int runaway(Map2D map, Pixel2D closestghost, Pixel2D pos) {
        int blue = Game.getIntColor(Color.BLUE, 0);

        // יצירת 4 השכנים (כולל טיפול במפה מעגלית)
        int x = pos.getX();
        int y = pos.getY();
        Pixel2D[] neighbors = new Pixel2D[4];

        if (map.isCyclic()) {
            neighbors[0] = new Index2D((x + 1) % map.getWidth(), y);
            neighbors[1] = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
            neighbors[2] = new Index2D(x, (y + 1) % map.getHeight());
            neighbors[3] = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
        } else {
            neighbors[0] = new Index2D(x + 1, y);
            neighbors[1] = new Index2D(x - 1, y);
            neighbors[2] = new Index2D(x, y + 1);
            neighbors[3] = new Index2D(x, y - 1);
        }

        int maxDist = -1;
        int bestNeighborsCount = -1;
        Pixel2D bestMove = null;

        for (Pixel2D n : neighbors) {
            // סינון ראשוני: לא להיכנס לקירות
            if (map.isInside(n) && map.getPixel(n) != blue) {

                // חישוב המרחק מהמפלצת אם נלך למשבצת n
                Pixel2D[] path = map.shortestPath(closestghost, n, blue);

                if (path != null) {
                    int dist = path.length;
                    int count = howManyneighbors(map, n, map.isCyclic());

                    // לוגיקה פשוטה וברזל:

                    // 1. אם מצאנו מרחק גדול יותר ממה שהיה עד עכשיו - לוקחים אותו מיד!
                    // לא מעניין אותנו כמה שכנים יש, העיקר להיות רחוקים.
                    if (dist > maxDist) {
                        maxDist = dist;
                        bestNeighborsCount = count;
                        bestMove = n;
                    }
                    // 2. שובר שוויון: אם המרחק זהה, נעדיף מקום פתוח יותר
                    else if (dist == maxDist) {
                        if (count > bestNeighborsCount) {
                            bestNeighborsCount = count;
                            bestMove = n;
                        }
                    }
                }
            }
        }

        if (bestMove == null) return randomDir();
        return nextmove(map, bestMove, pos);
    }

//        public static Pixel2D isEatable(Map2D Distmap , GhostCL[] ghosts) {
//            Pixel2D ans = null;
//            int blue = Game.getIntColor(Color.BLUE, 0);
//            int dis = Integer.MAX_VALUE ;
//            for (int i = 0; i < ghosts.length; i++) {
//                GhostCL current = ghosts[i];
//                if (current.getStatus() != 0 && current.remainTimeAsEatable(0) > 0){
//                    String currentLocation = current.getPos(0).toString();
//                    String[] g = currentLocation.split(",");
//                    int x = Integer.parseInt(g[0]);
//                    int y = Integer.parseInt(g[1]);
//                    Pixel2D currenP = new Index2D(x, y);
//                    int distToPac = Distmap.getPixel(x , y);
//                    if ((y < 11 || y >13 || x < 9 || x > 13) && distToPac != -1) {
//                        if (distToPac < dis ){
//                            dis  = distToPac;
//                            ans = currenP;
//                        }
//                    }
//                }
//            }
//            return ans;
//        }

    public static Pixel2D isEatable(Map2D Distmap , GhostCL[] ghosts) {
        Pixel2D ans = null;
        int blue = Game.getIntColor(Color.BLUE, 0);
        int dis = Integer.MAX_VALUE ;
        for (int i = 0; i < ghosts.length; i++) {
            GhostCL current = ghosts[i];
            if (current.getStatus() != 0 && current.remainTimeAsEatable(0) > 0){
                String currentLocation = current.getPos(0).toString();
                String[] g = currentLocation.split(",");
                int x = Integer.parseInt(g[0]);
                int y = Integer.parseInt(g[1]);
                Pixel2D currenP = new Index2D(x, y);
                int distToPac = Distmap.getPixel(x , y);
                if ((y < 11 || y >13 || x < 9 || x > 13) && distToPac != -1) {
                    double timeToReach = (distToPac + 3) * GameInfo.DT / 1000.0 ;
                    if (distToPac < dis && timeToReach < current.remainTimeAsEatable(0)){
                        dis  = distToPac;
                        ans = currenP;
                    }
                }
            }
        }
        return ans;
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

    public static boolean aliveghost( GhostCL[] ghosts ) {
        for (int i = 0; i < ghosts.length; i++) {
            GhostCL current = ghosts[i];
            if (current.getStatus() != 0) {
                return true;
            }
        }
        return false;

    }
}



