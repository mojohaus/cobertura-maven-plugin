import junit.framework.TestCase;

public class CircleTest
    extends TestCase
{
    private Circle circle;

    public void setup()
    {
        circle = new Circle( 2, 3, 2.5 );
    }

    public void testX()
    {
        assertEquals( "Test X", 2, circle.getX() );
    }

    public void testY()
    {
        assertEquals( "Test Y", 3, circle.getY() );
    }

    public void testRadius()
    {
        assertEquals( "Test radius", 2.5, circle.getRadius(), 0.1 );
    }

    public void tearDown()
    {
        circle = null;
    }
}