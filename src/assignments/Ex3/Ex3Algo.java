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
    public class Ex3Algo implements PacManAlgo {
        private int _count;

        public Ex3Algo() {
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
            int x = (int) Math.round(Double.parseDouble(p[0]));
            int y = (int) Math.round(Double.parseDouble(p[1]));
            Pixel2D pacpos = new Index2D(x, y);
            Map2D mapboard = new Map(board);
            Map2D dis = mapboard.allDistance(pacpos, blue);
            _count++;
            GhostCL[] ghosts = game.getGhosts(0);
            System.out.println(pos);
            Pixel2D closestghost = closestghost(ghosts , dis);
            Pixel2D closestEatable = isEatable(mapboard , ghosts , pacpos);
            if (closestEatable != null){
                return nextmove(mapboard , closestEatable , pacpos);
            }
            if (closestghost != null){
                return runaway(mapboard , closestghost , pacpos);
            }
            Pixel2D closestGreen = closesetGreen(mapboard, dis);
            if (closestGreen != null && mapboard.shortestPath(pacpos , closestGreen, blue).length <= 4){
                return nextmove(mapboard ,closestGreen , pacpos);
            }
            Pixel2D closestPink = closesetPink(mapboard, dis);
            return nextmove(mapboard ,closestPink , pacpos);

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
            int minDis = Integer.MAX_VALUE;
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
            if (path == null || path.length < 1){
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
            int run = 20  ;
            Pixel2D ans = null;
            for (int i = 0; i < ghosts.length; i++) {
                GhostCL current = ghosts[i];
                String currentLocation = current.getPos(0).toString();
                String[] g = currentLocation.split(",");
                int x = (int) Math.round(Double.parseDouble(g[0]));
                int y = (int) Math.round(Double.parseDouble(g[1]));
                Pixel2D ghost = new Index2D(x, y);
                    if (current.getStatus() != 0 && ((current.remainTimeAsEatable(0) == 0) ||((alldismap.getPixel(ghost)+1) * GameInfo.DT) > current.remainTimeAsEatable(0))) {
                        if (alldismap.getPixel(ghost) < run && 0 <= alldismap.getPixel(ghost)) {
                            run = alldismap.getPixel(ghost);
                            ans = ghost;
                        }
                    }
                }

            return ans;
            }

        public static int runaway(Map2D map, Pixel2D closestghost, Pixel2D pos) {
            int blue = Game.getIntColor(Color.BLUE, 0);
            int pink = Game.getIntColor(Color.PINK, 0);
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
            int max = -1 ;
            Pixel2D ans = null;
            for (Pixel2D n : neighbors){
                Pixel2D [] nsp = map.shortestPath(closestghost, n , blue);
                if (nsp != null) {
                    int count = howManyneighbors(map, n, map.isCyclic());
                    if (count > 1) {
                        if (nsp.length == max && map.getPixel(n) == pink) {
                            ans = n;
                        } else if (nsp.length > max) {
                            max = nsp.length;
                            ans = n;
                        }
                    }
                }
            }
            if (ans == null) {
                for (Pixel2D n : neighbors){
                    Pixel2D [] nsp = map.shortestPath(closestghost, n , blue);
                    if (nsp != null) {
                            if (nsp.length == max && map.getPixel(n) == pink) {
                                ans = n;
                            } else if (nsp.length > max) {
                                max = nsp.length;
                                ans = n;
                        }
                    }
                }
            };
            return nextmove(map , ans , pos);

            }

        public static Pixel2D isEatable(Map2D map , GhostCL[] ghosts , Pixel2D pac) {
            Pixel2D ans = null;
            int blue = Game.getIntColor(Color.BLUE, 0);
            int dis = 20;
            for (int i = 0; i < ghosts.length; i++) {
                GhostCL current = ghosts[i];
                if (current.getStatus() != 0 && current.remainTimeAsEatable(0) > 0){
                    String currentLocation = current.getPos(0).toString();
                    String[] g = currentLocation.split(",");
                    int x = (int) Math.round(Double.parseDouble(g[0]));
                    int y = (int) Math.round(Double.parseDouble(g[1]));
                    Pixel2D currenP = new Index2D(x, y);
                    Pixel2D [] path = map.shortestPath(currenP , pac , blue);
                    if (y != 11 && y!= 12 && path != null) {
                        if (path.length < dis && (GameInfo.DT * (path.length + 1))  < current.remainTimeAsEatable(0)){
                            dis  = path.length;
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

        }
