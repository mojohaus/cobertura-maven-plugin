public class Circle
{
    private double radius;

    private int x, y;

    public Circle( int x, int y, double radius )
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public double getRadius()
    {
        return radius;
    }
    
    public double getDiameter()
    {
        return radius * 2;
    }
    
    public double getArea()
    {
        return ( Math.PI * ( radius * radius ) );
    }
    
    public double getCircumference()
    {
        return ( Math.PI * getDiameter() );
    }
}