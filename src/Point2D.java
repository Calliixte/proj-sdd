public class Point2D {
    /**
     * Classe representant un point pour la gestion de coordonnées des graphes TSPLIB
     */
    private Double x;
    private Double y;

    public Point2D(Double x,Double y){
        this.x = x;
        this.y = y;
    }

    public Double getX(){
        return this.x;
    }

    public Double getY(){
        return this.y;
    }

    /**
     * Renvoie la distance euclienne au point p
     * @param p point auquel on va calculer la distance
     * @return distance euclienne 
     */
    public Double distanceEuclidienne(Point2D p){
        double diffX = this.getX()-p.getX();
        double diffY = this.getY()-p.getY();
        return Math.sqrt((diffX*diffX)+(diffY*diffY));
    }

    public String __toString(){
        return "Point(" + this.x+", "+this.y+")";
    }

}
