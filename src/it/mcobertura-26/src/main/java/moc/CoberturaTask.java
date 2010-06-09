package moc;

public class CoberturaTask
{

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        CoberturaTask task = new CoberturaTask();
        task.doGreet();
    }
    
    protected String doGreet() 
    {
        if ( Math.random() > 0.5 )
        {
            return "Hello";
        }
        else 
        {
            return "Goodbye";
        }
    }

}
