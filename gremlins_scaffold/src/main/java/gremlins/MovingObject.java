package gremlins;

import java.util.ArrayList;


public abstract class MovingObject extends GameObject {

    protected ArrayList<StationaryObject> stonewalls;
    protected ArrayList<Brickwall> brickwalls;
    protected int speed;
    protected int collidedObstacleIndex;

    public MovingObject(int x, int y, ArrayList<StationaryObject> stonewalls, 
                        ArrayList<Brickwall> brickwalls, int speed) {
        super(x, y);
        this.stonewalls = stonewalls;
        this.brickwalls = brickwalls;
        this.speed = speed;
    }

    public abstract void tick();

    /**
     * Makes the object move in a particular direction with their given speed.
     * @param   direction   The direction that the object should move in.
    */
    protected void move(String direction) {
        if (direction == "left") {
            this.x -= this.speed;
        } else if (direction == "right") {
            this.x += this.speed;
        } else if (direction == "up") {
            this.y -= this.speed;
        } else if (direction == "down") {
            this.y += this.speed;
        }
    }

    /**
     * For any coordinate in the object's X/Y range, if it is between the lower and upper bounds of the obstacle, return true, else false.
    */
    protected boolean inObstacleRange(int[] objectRange, int ObstacleLower, int ObstacleUpper) {
        for (int point : objectRange) {
            if (point > ObstacleLower && point < ObstacleUpper) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a size 21 integer array of all X coordinates that the object occupies at the current point in time.
    */
    protected int[] findObjectRangeX() {
        int incrementX = 0;
        int[] objectRangeX = new int[App.SPRITESIZE+1];
        for (int i = 0; i < objectRangeX.length; i++) {
            objectRangeX[i] = this.x + incrementX;
            incrementX++;
        }
        return objectRangeX;
    }

    /**
     * Returns a size 21 integer array of all Y coordinates that the object occupies at the current point in time.
    */
    protected int[] findObjectRangeY() {
        int incrementY = 0;
        int[] objectRangeY = new int[App.SPRITESIZE+1];
        for (int i = 0; i < objectRangeY.length; i++) {
            objectRangeY[i] = this.y + incrementY;
            incrementY++;
        }
        return objectRangeY;
    }

    /**
     * For an individual obstacle, checks whether the object collides with it.
     * @param   obstacleSides   The coordinates of all 4 sides of the obstacle.
     * @param   direction   The current direction of the object.
    */
    protected boolean obstacleCollisionIndividual(int[] obstacleSides, String direction) {
        int objectLeft = this.x;
        int objectTop = this.y;
        int objectRight = this.x + App.SPRITESIZE;
        int objectBottom = this.y + App.SPRITESIZE;

        int[] objectRangeX = findObjectRangeX();
        int[] objectRangeY = findObjectRangeY();

        int obstacleLeft = obstacleSides[0];
        int obstacleTop = obstacleSides[1];
        int obstacleRight = obstacleSides[2];
        int obstacleBottom = obstacleSides[3];

        if (direction == "left") {
            if (objectLeft <= obstacleRight && objectLeft >= obstacleLeft && 
                inObstacleRange(objectRangeY, obstacleTop, obstacleBottom)) {
                return true;
            }
        } else if (direction == "up") {
            if (objectTop <= obstacleBottom && objectTop >= obstacleTop && 
                inObstacleRange(objectRangeX, obstacleLeft, obstacleRight)) {
                return true;
            }
        } else if (direction == "right") {
            if (objectRight >= obstacleLeft && objectRight <= obstacleRight && 
                inObstacleRange(objectRangeY, obstacleTop, obstacleBottom)) {
                return true;
            }
        } else if (direction == "down") {
            if (objectBottom >= obstacleTop && objectBottom <= obstacleBottom && 
                inObstacleRange(objectRangeX, obstacleLeft, obstacleRight)) {
                return true;
            }
        } else if (direction == "touching") {
            if (inObstacleRange(objectRangeY, obstacleTop, obstacleBottom) && 
                inObstacleRange(objectRangeX, obstacleLeft, obstacleRight)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a collision between the object and all obstacles of one object type in an arraylist.
     * @param   direction   The current direction of the object.
     * @param   obstacleList    An ArrayList of all the objects that we want to check a collsion for between this object.
    */
    protected <T extends GameObject> boolean obstacleCollision(String direction, 
                                                                ArrayList<T> obstacleList) {
        boolean collided = false;
        for (int i = 0; i < obstacleList.size(); i++) {
            int[] obstacleSides = {obstacleList.get(i).getX(), obstacleList.get(i).getY(), 
                                    obstacleList.get(i).getX()+App.SPRITESIZE, 
                                    obstacleList.get(i).getY()+App.SPRITESIZE};
            if (obstacleCollisionIndividual(obstacleSides, direction)) {
                this.collidedObstacleIndex = i;
                collided = true;
                break;
            }
        }
        return collided;
    }

    /**
     * Checks for a collision with a wall - a brickwall or stonewall.
     * @param   direction   The current direction of the object.
    */
    protected boolean wallCollision(String direction) {
        return obstacleCollision(direction, this.stonewalls) || 
                obstacleCollision(direction, this.brickwalls);
    }
}