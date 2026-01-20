//package assignments.Ex3;
//
//import exe.ex3.game.Game;
//import exe.ex3.game.GhostCL;
//import exe.ex3.game.PacManAlgo;
//import exe.ex3.game.PacmanGame;
//
//import java.awt.*;
//import java.util.ArrayList;
//
///**
// * This is the major algorithmic class for Ex3 - the PacMan game:
// *
// * This code is a very simple example (random-walk algorithm).
// * Your task is to implement (here) your PacMan algorithm.
// */
//public class Ex3Algo implements PacManAlgo {
//    private int _count;
//
//    public Ex3Algo() {
//        _count = 0;
//    }
//
//    @Override
//    /**
//     *  Add a short description for the algorithm as a String.
//     */
//    public String getInfo() {
//        return null;
//    }
//
//    @Override
//    /**
//     * This ia the main method - that you should design, implement and test.
//     */
//    public int move(PacmanGame game) {
//        if (_count == 0 || _count == 300) {
//            int code = 0;
//            int[][] board = game.getGame(0);
//            printBoard(board);
//            int blue = Game.getIntColor(Color.BLUE, code);
//            int pink = Game.getIntColor(Color.PINK, code);
//            int black = Game.getIntColor(Color.BLACK, code);
//            int green = Game.getIntColor(Color.GREEN, code);
//            System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
//            String pos = game.getPos(code).toString();
//            System.out.println("Pacman coordinate: " + pos);
//            GhostCL[] ghosts = game.getGhosts(code);
//            printGhosts(ghosts);
//            int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
//        }
//        _count++;
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        int[][] board = game.getGame(0);
//        String posP = game.getPos(0).toString();
//        String[] p = posP.split(",");
//        int x = Integer.parseInt(p[0]);
//        int y = Integer.parseInt(p[1]);
//        Pixel2D pacman = new Index2D(x, y);
//        GhostCL[] ghosts = game.getGhosts(0);
//        Map2D map_board = new Map(board);
//        Map2D dis = map_board.allDistance(pacman, blue);
//        Map dangerous_ghost = closestghosts(map_board, ghosts, dis);
//        Map closest_ghost = closestghost(map_board, ghosts, dis);
//        Pixel2D closestEatable = isEatable(dis, ghosts);
//        if (closestEatable != null) {
//            System.out.println("eat ghost");
//            System.out.println("pacman:" + pacman.toString() + "closest ghost:" + closestEatable.toString());
//            return nextmove(map_board, closestEatable, pacman);
//        }
//        Map2D tempMap = new Map(map_board.getMap());
//        Map2D mapForEating = markghost(tempMap, ghosts);
//        Map2D safeDist = mapForEating.allDistance(pacman, blue);
//        Pixel2D closestGreen = closesetGreen(map_board, safeDist);
//        //int distToGhost = dangerous_ghost.getPixel(pacman);
//        if (isGhostTooClose(dis, ghosts, 10)) {
////            if (closestGreen != null) {
////                int distToGreen = safeDist.getPixel(closestGreen);
////                //Pixel2D[] path = map_board.shortestPath(pacman, closestGreen, blue);
////                if (distToGreen != -1 && distToGreen <= 4) {
////                    System.out.println("go to green");
////                    System.out.println("pacman:" + pacman.toString() + "closest green:" + closestGreen.toString());
////                    return nextmove(mapForEating, closestGreen, pacman);
////                }
////            }
//            return runaway(map_board, dangerous_ghost, closest_ghost , ghosts, pacman);
//        }
//        if (closestGreen != null) {
//            int distToGreen = safeDist.getPixel(closestGreen);
//            //Pixel2D[] path = map_board.shortestPath(pacman, closestGreen, blue);
//            if (distToGreen != -1 && distToGreen <= 6 && alive_noteatable(ghosts)) {
//                System.out.println("go to green");
//                System.out.println("pacman:" + pacman.toString() + "closest green:" + closestGreen.toString());
//                return nextmove(mapForEating, closestGreen, pacman);
//            }
//        }
//
//        Pixel2D closestPink = closesetPink(map_board, safeDist);
//        return nextmove(map_board, closestPink, pacman);
//    }
//
//
//    public static Pixel2D closesetPink(Map2D map, Map2D alldismap) {
//        Pixel2D ans = null;
//        int pink = Game.getIntColor(Color.PINK, 0);
//        int minDis = Integer.MAX_VALUE;
//        for (int x = 0; x < map.getWidth(); x++) {
//            for (int y = 0; y < map.getHeight(); y++) {
//                if (map.getPixel(x, y) == pink) {
//                    int distvalue = alldismap.getPixel(x, y);
//                    if (distvalue != -1 && minDis > distvalue) {
//                        minDis = distvalue;
//                        ans = new Index2D(x, y);
//                    }
//                }
//            }
//        }
//        return ans;
//    }
//
//    public static Pixel2D closesetGreen(Map2D map, Map2D alldismap) {
//        Pixel2D ans = null;
//        int green = Game.getIntColor(Color.GREEN, 0);
//        int minDis = 10;
//        for (int x = 0; x < map.getWidth(); x++) {
//            for (int y = 0; y < map.getHeight(); y++) {
//                if (map.getPixel(x, y) == green) {
//                    int distvalue = alldismap.getPixel(x, y);
//                    if (distvalue != -1 && minDis > distvalue) {
//                        minDis = distvalue;
//                        ans = new Index2D(x, y);
//                    }
//                }
//            }
//        }
//        return ans;
//    }
//
//    public static Map closestghosts(Map2D map , GhostCL[] ghosts, Map2D alldismap) {
//        int maxDistance = 10;
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        Map ans = new Map(alldismap.getWidth() , alldismap.getHeight() , 0);
//        for (int i = 0; i < ghosts.length; i++) {
//            GhostCL current = ghosts[i];
//            String currentLocation = current.getPos(0).toString();
//            Pixel2D ghost = getghostPixel(currentLocation);
//            int ghostDist = alldismap.getPixel(ghost);
//            if (ghostDist != -1 && !isInCage(ghost)) {
//                double timeToReach = (ghostDist) * GameInfo.DT / 1000.0 ;
//                if (current.getStatus() != 0 && ((current.remainTimeAsEatable(0) < 0.6) || (timeToReach < (current.remainTimeAsEatable(0))))){
//                    if (ghostDist < maxDistance) {
//                        Map2D ghost_dist = map.allDistance(ghost, blue);
//                        ans.addMap2D(ghost_dist);
//                    }
//                }
//            }
//        }
//        return ans;
//    }
//
//    public static Map closestghost(Map2D map, GhostCL[] ghosts, Map2D alldismap) {
//        //  int maxDistance = 1000;
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        Map ans = new Map(alldismap.getWidth(), alldismap.getHeight(), 1000);
//        for (int x = 0; x < map.getWidth(); x++) {
//            for (int y = 0; y < map.getHeight(); y++) {
//                if (map.getPixel(x, y) == blue) {
//                    ans.setPixel(x, y, -1);
//                }
//            }
//        }
//        for (GhostCL current : ghosts) {
//            String currentLocation = current.getPos(0).toString();
//            Pixel2D ghost = getghostPixel(currentLocation);
//            int ghostDist = alldismap.getPixel(ghost);
//            if (ghostDist != -1 && !isInCage(ghost)) {
//                double timeToReach = (ghostDist + 2) * GameInfo.DT / 1000.0;
//                if (current.getStatus() != 0 && ((current.remainTimeAsEatable(0) < timeToReach))) {
//                    Map2D ghost_dist = map.allDistance(ghost, blue);
//                    for (int x = 0; x < map.getWidth(); x++) {
//                        for (int y = 0; y < map.getHeight(); y++) {
//                            int d = ghost_dist.getPixel(x, y);
//                            int currentVal = ans.getPixel(x, y);
//                            if (currentVal != -1 && d != -1 && d < currentVal) {
//                                ans.setPixel(x, y, d);
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
//        return ans;
//    }
//
////    public static Map closestghosts(Map2D map , GhostCL[] ghosts, Map2D alldismap) {
////        int maxDistance = 10;
////        int blue = Game.getIntColor(Color.BLUE, 0);
////        Map ans = new Map(alldismap.getWidth() , alldismap.getHeight() , 1000);
////        for (int x = 0; x < map.getWidth(); x++) {
////            for (int y = 0; y < map.getHeight(); y++) {
////                if (map.getPixel(x, y) == blue) {
////                    ans.setPixel(x, y, -1);
////                }
////            }
////        }
////        for (GhostCL current : ghosts) {
////            String currentLocation = current.getPos(0).toString();
////            Pixel2D ghost = getghostPixel(currentLocation);
////            int ghostDist = alldismap.getPixel(ghost);
////            if (ghostDist != -1 && !isInCage(ghost)) {
////                double timeToReach = (ghostDist + 2) * GameInfo.DT / 1000.0;
////                if (current.getStatus() != 0 && ((current.remainTimeAsEatable(0) < timeToReach) )) {
////                    if (ghostDist < maxDistance) {
////                        Map2D ghost_dist = map.allDistance(ghost, blue);
////                        for (int x = 0; x < map.getWidth(); x++) {
////                            for (int y = 0; y < map.getHeight(); y++) {
////                                int d = ghost_dist.getPixel(x, y);
////                                int currentVal = ans.getPixel(x, y);
////                                if (currentVal != -1 && d != -1 && d < currentVal) {
////                                    ans.setPixel(x, y, d);
////                                }
////                            }
////                        }
////
////                    }
////                }
////            }
////        }
////        return ans;
////    }
//
//    public static int nextmove(Map2D map, Pixel2D closets, Pixel2D pos) {
//        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        if (closets == null) {
//            return randomDir();
//        }
//        Pixel2D[] path = map.shortestPath(pos, closets, blue);
//        if (path == null || path.length < 2) {
//            return randomDir();
//        }
//        Pixel2D next = path[1];
//        if ((pos.getY() + 1) % map.getHeight() == next.getY()) {
//            return dirs[0];
//        }
//        if ((pos.getX() - 1 + map.getWidth()) % map.getWidth() == next.getX()) {
//            return dirs[1];
//        }
//        if ((pos.getY() - 1 + map.getHeight()) % map.getHeight() == next.getY()) {
//            return dirs[2];
//        }
//        if ((pos.getX() + 1) % map.getWidth() == next.getX()) {
//            return dirs[3];
//        }
//        return randomDir();
//    }
//
////            public static int runaway(Map2D map, Map closestghosts , GhostCL[] ghosts , Pixel2D pos) {
////            int blue = Game.getIntColor(Color.BLUE, 0);
////            int pink = Game.getIntColor(Color.PINK, 0);
////            int green = Game.getIntColor(Color.GREEN, 0);
////            int x = pos.getX();
////            int y = pos.getY();
////            Map2D newmap = new Map(map.getMap());
////            newmap = markghost(newmap , ghosts);
////            Map2D safeDistMap = newmap.allDistance(pos, blue);
////                Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
////                if (map.isCyclic()) {
////                    neighbor1 = new Index2D((x + 1) % map.getWidth(), y);
////                    neighbor2 = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
////                    neighbor3 = new Index2D(x, (y + 1) % map.getHeight());
////                    neighbor4 = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
////                } else {
////                    neighbor1 = new Index2D(x + 1, y);
////                    neighbor2 = new Index2D(x - 1, y);
////                    neighbor3 = new Index2D(x, y + 1);
////                    neighbor4 = new Index2D(x, y - 1);
////                }
////                Pixel2D[] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
////                Pixel2D ans = null;
////                Pixel2D closesTogreenToP = closesetGreen(map ,safeDistMap);
////                if (closesTogreenToP != null ) {
////                    Pixel2D[] path = newmap.shortestPath(pos, closesTogreenToP, blue);
////                    if (path != null && path.length > 1) {
////                        Pixel2D pathToGreen = path[1];
////                        if (closestghosts.getPixel(pathToGreen) > 3) {
////                            System.out.println("run from ghost to green");
////                            System.out.println("pacman:" + pos.toString() + "closest green:" + closestghosts.getPixel(closesTogreenToP));
////                            return nextmove(newmap, closesTogreenToP, pos);
////                        }
////                    }
////                }
////
////                int maxDist = -1;
////                int numneighbors = 1;
////                for (Pixel2D n : neighbors) {
////                        if (map.isInside(n) && newmap.getPixel(n) != blue && closestghosts.getPixel(n) != 0 && !isInCage(n)) {
////                            int count = howManyneighbors(map, n, map.isCyclic());
////                                int nToghost = closestghosts.getPixel(n);
////                                if (nToghost >= 2) {
////                                    if (maxDist <= 4) {
////                                        if (nToghost > maxDist) {
////                                            maxDist = nToghost;
////                                            numneighbors = count;
////                                            ans = n;
////                                        }
////                                        else if (nToghost == maxDist && count > numneighbors) {
////                                            numneighbors = count;
////                                            ans = n;
////                                        }
////                                    }
////                                    else {
////                                        if (nToghost > 4) {
////                                            if (count > numneighbors) {
////                                                numneighbors = count;
////                                                maxDist = nToghost;
////                                                ans = n;
////                                            } else if (count == numneighbors) {
////                                                if (nToghost > maxDist) {
////                                                    maxDist = nToghost;
////                                                    ans = n;
////                                                } else if (nToghost == maxDist  &&  (map.getPixel(n) == pink || map.getPixel(n) == green)) {
////                                                    ans = n;
////                                                }
////                                            }
////                                        }
////                                    }
////                                }
////                            }
////                        }
////
////                    if (ans == null) {
////                        return randomDir();
////                    }
////                System.out.println("run from ghost");
////                System.out.println("pacman:" + pos.toString() + "closest ghost:" + closestghosts.getPixel(pos) );
////                    return nextmove(newmap, ans, pos);
////                }
//
////    public static int runaway(Map2D map, Map closestghosts , GhostCL[] ghosts , Pixel2D pos) {
////        int blue = Game.getIntColor(Color.BLUE, 0);
////        int pink = Game.getIntColor(Color.PINK, 0);
////        int green = Game.getIntColor(Color.GREEN, 0);
////        int x = pos.getX();
////        int y = pos.getY();
////        Map2D newmap = new Map(map.getMap());
////        newmap = markghost(newmap , ghosts);
////        Map2D safeDistMap = newmap.allDistance(pos, blue);
////        Pixel2D[] neighbors = getNeighbors(pos , map);
////      //  Pixel2D ans = null;
////        Pixel2D closesTogreenToP = closesetGreen(map ,safeDistMap);
////        if (closesTogreenToP != null ) {
////            Pixel2D[] path = newmap.shortestPath(pos, closesTogreenToP, blue);
////            if (path != null && path.length > 1) {
////                Pixel2D pathToGreen = path[1];
////                if (closestghosts.getPixel(pathToGreen) > 3) {
////                    System.out.println("run from ghost to green");
////                    System.out.println("pacman:" + pos.toString() + "closest green:" + closestghosts.getPixel(closesTogreenToP));
////                    return nextmove(newmap, closesTogreenToP, pos);
////                }
////            }
////        }
////        Pixel2D bestMove = null;
////        double maxScore = -1000;
////        for (Pixel2D n : neighbors) {
////            if (map.isInside(n) && newmap.getPixel(n) != blue && !isInCage(n)) {
////                int nToghost = closestghosts.getPixel(n);
////                int count = howManyneighbors(map, n, map.isCyclic());
////                double currentScore = nToghost;
////                if (count <= 1) {
////                    currentScore -= 50;
////                }
////                if (nToghost < 2) {
////                    currentScore -= 100;
////                }
////                if (count == 2 && nToghost < 5) {
////                    currentScore -= 20;
////                }
////                else if (count >= 3 ) {
////                    currentScore += 0.5;
////                }
////                if (count >= 2 &&  (map.getPixel(n) == pink || map.getPixel(n) == green)) {
////                    currentScore += 0.1;
////                }
////                if (currentScore > maxScore) {
////                    maxScore = currentScore;
////                    bestMove = n;
////
////                            }
////                        }
////                    }
////                if (bestMove == null) {
////                    System.out.println("run from ghost by random");
////
////                    return randomDir();
////        }
////        System.out.println("Running! Best Score: " + maxScore + " Target: " + bestMove);
////        return nextmove(newmap, bestMove, pos);
////    }
//
//
//    public static int runaway(Map2D map, Map totalGhostDistMap,Map minGhostDistMap, GhostCL[] ghosts, Pixel2D pos) {
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        int pink = Game.getIntColor(Color.PINK, 0);
//        int green = Game.getIntColor(Color.GREEN, 0);
//        int x = pos.getX();
//        int y = pos.getY();
//        Map2D newmap = new Map(map.getMap()); //מפה חדשה באותו הגודל והנתונים
//        newmap = markghost(newmap, ghosts); //מסמנים על גבי המפה את הרוחות ככחולות
//        Map2D safeDistMap = newmap.allDistance(pos, blue); // חישוב כל המרחקים לפקמן כשאני יודעת שהרוחות הן מכשול כחול
//        Pixel2D[] neighbors = getNeighbors(pos, map);
//        Pixel2D closesTogreenToP = closesetGreen(map, safeDistMap);
//        if (closesTogreenToP != null) {
//            Pixel2D[] path = newmap.shortestPath(pos, closesTogreenToP, blue);
//            if (path != null && path.length > 1 && minGhostDistMap.getPixel(closesTogreenToP) > 4 ) {
//                Pixel2D pathToGreen = path[1];
//                System.out.println("run from ghost to green");
//                System.out.println("pacman:" + pos.toString() + "closest green:");
//                return nextmove(map, pathToGreen, pos);
//            }
//        }
//        int maxMinDist = -100;
//        int maxTotalDist = -100;
//        int maxNeighbors = 0 ;
//        Pixel2D bestMove = null;
//        for (Pixel2D n : neighbors) {
//            if (map.isInside(n) && newmap.getPixel(n) != blue && !isInCage(n)) {
//                int currentNeighbors = howManyneighbors(newmap , n , newmap.isCyclic());
//                int currentMinDist = minGhostDistMap.getPixel(n);
//                int currentTotalDist = totalGhostDistMap.getPixel(n);
//                if (currentMinDist > 2 && currentNeighbors > 2) {
//                    maxMinDist = currentMinDist;
//                    maxTotalDist = currentTotalDist;
//                    bestMove = n;
//                }
//                else if (currentMinDist > maxMinDist ) {
//                    maxMinDist = currentMinDist;
//                    maxTotalDist = currentTotalDist;
//                    bestMove = n;
//                }
//                else if (currentMinDist == maxMinDist) {
//                    if (currentTotalDist > maxTotalDist) {
//                        maxTotalDist = currentTotalDist;
//                        bestMove = n;
//                    }
//                }
////                if (currentMinDist > maxMinDist) {
////                    maxMinDist = currentMinDist;
////                    maxNeighbors = currentNeighbors; // מעדכנים גם את זה
////                    maxTotalDist = currentTotalDist;
////                    bestMove = n;
////                }
////                else if (currentMinDist == maxMinDist) {
////                    if (currentNeighbors > maxNeighbors) {
////                        maxNeighbors = currentNeighbors;
////                        maxTotalDist = currentTotalDist;
////                        bestMove = n;
////                    }
////                     else if (currentNeighbors == maxNeighbors) {
////                        if (currentTotalDist > maxTotalDist) {
////                            maxTotalDist = currentTotalDist;
////                            bestMove = n;
////                        }
////                    }
//            }
//        }
//
//
//        return nextmove(newmap, bestMove, pos);
//    }
//
//    public static Pixel2D isEatable(Map2D Distmap , GhostCL[] ghosts) {
//        Pixel2D ans = null;
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        int dis = 10 ;
//        for (int i = 0; i < ghosts.length; i++) {
//            GhostCL current = ghosts[i];
//            if (current.getStatus() != 0 ){
//                String currentLocation = current.getPos(0).toString();
//                Pixel2D currenP = getghostPixel(currentLocation);
//                int distToPac = Distmap.getPixel(currenP);
//                if (!isInCage(currenP) && !nearCage(currenP) && distToPac != -1) {
//                    double timeToReach = ((distToPac + 1) * GameInfo.DT) / 1000.0 ;
//                    boolean safeTime =(timeToReach * 1.2 < current.remainTimeAsEatable(0));
//                    if (distToPac < dis && safeTime){
//                        dis  = distToPac;
//                        ans = currenP;
//                    }
//                }
//            }
//        }
//        return ans;
//    }
//
//    private static void printBoard(int[][] b) {
//        for(int y =0;y<b[0].length;y++){
//            for(int x =0;x<b.length;x++){
//                int v = b[x][y];
//                System.out.print(v+"\t");
//            }
//            System.out.println();
//        }
//    }
//    private static void printGhosts(GhostCL[] gs) {
//        for(int i=0;i<gs.length;i++){
//            GhostCL g = gs[i];
//            System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
//        }
//    }
//    private static int randomDir() {
//        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
//        int ind = (int)(Math.random()*dirs.length);
//        return dirs[ind];
//    }
//    private static Pixel2D getghostPixel (String ghost_pos){
//        String[] g = ghost_pos.split(",");
//        int x = Integer.parseInt(g[0]);
//        int y = Integer.parseInt(g[1]);
//        Pixel2D ghost = new Index2D(x, y);
//        return ghost;
//    }
//    public static int howManyneighbors(Map2D map , Pixel2D n , boolean isCyclic) {
//        Pixel2D neighbor1 , neighbor2 , neighbor3 , neighbor4 ;
//        int counter = 0;
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        int x = n.getX();
//        int y = n.getY();
//        if (isCyclic){
//            neighbor1 = new Index2D((x + 1) % map.getWidth(), y);
//            neighbor2 = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
//            neighbor3 = new Index2D(x, (y + 1) % map.getHeight());
//            neighbor4 = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
//        }
//        else {
//            neighbor1 = new Index2D(x + 1, y);
//            neighbor2 = new Index2D(x - 1, y);
//            neighbor3 = new Index2D( x , y + 1);
//            neighbor4 = new Index2D( x, y - 1);
//        }
//        Pixel2D [] neighbors = {neighbor1, neighbor2, neighbor3, neighbor4};
//        for (Pixel2D p : neighbors){
//            if (map.isInside(p) && map.getPixel(p) != blue){
//                counter++;
//            }
//        }
//        return counter;
//    }
//    public static boolean aliveghost( GhostCL[] ghosts ) {
//        for (int i = 0; i < ghosts.length; i++) {
//            GhostCL current = ghosts[i];
//            if (current.getStatus() != 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//    public static boolean alive_noteatable( GhostCL[] ghosts ) {
//        for (int i = 0; i < ghosts.length; i++) {
//            GhostCL current = ghosts[i];
//            if (current.getStatus() != 0 && current.remainTimeAsEatable(0) < 0.5) {
//                return true;
//            }
//        }
//        return false;
//    }
//    private static boolean isInCage(Pixel2D p) {
//        int x = p.getX();
//        int y = p.getY();
//        return (x >= 9 && x <= 13 && y >= 11 && y <= 12);
//    }
//    private static boolean nearCage(Pixel2D p) {
//        int x = p.getX();
//        int y = p.getY();
//        return (x >= 9 && x <= 13 && y >= 13 && y <= 14);
//    }
//    public static Map2D markghost(Map2D mymap ,GhostCL[] ghosts ) {
//        int blue = Game.getIntColor(Color.BLUE, 0);
//        for (int i = 0; i < ghosts.length; i++) {
//            GhostCL current = ghosts[i];
//            Pixel2D g = getghostPixel(current.getPos(0).toString());
//            if (current.getStatus() != 0) {
//                mymap.setPixel(g , blue);
//            }
//        }
//        return mymap;
//    }
//
//    public static int whenrun(){
//        if (GameInfo.DT  <= 50){return 8;}
//        if (GameInfo.DT  <= 100){return 10;}
//        if (GameInfo.DT  <= 150){return 13;}
//        if (GameInfo.DT  <= 200){return 15;}
//        return 10;
//    }
//
//    private static Pixel2D[] getNeighbors(Pixel2D pos, Map2D map) {
//        int x = pos.getX();
//        int y = pos.getY();
//        Pixel2D neighbor1, neighbor2, neighbor3, neighbor4;
//        if (map.isCyclic()) {
//            neighbor1 = new Index2D((x + 1) % map.getWidth(), y);
//            neighbor2 = new Index2D((x - 1 + map.getWidth()) % map.getWidth(), y);
//            neighbor3 = new Index2D(x, (y + 1) % map.getHeight());
//            neighbor4 = new Index2D(x, (y - 1 + map.getHeight()) % map.getHeight());
//        } else {
//            neighbor1 = new Index2D(x + 1, y);
//            neighbor2 = new Index2D(x - 1, y);
//            neighbor3 = new Index2D(x, y + 1);
//            neighbor4 = new Index2D(x, y - 1);
//        }
//        return new Pixel2D[]{neighbor1, neighbor2, neighbor3, neighbor4};
//    }
//
//    private boolean isGhostTooClose (Map2D dis , GhostCL[] ghosts, int limit){
//        for (GhostCL g : ghosts) {
//            if (g.getStatus() != 0 && g.remainTimeAsEatable(0) < 0.2 ) {
//                Pixel2D gPos = getghostPixel(g.getPos(0).toString());
//                int d = dis.getPixel(gPos);
//                if ( !isInCage(gPos) && d != -1 && d <= limit ) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}