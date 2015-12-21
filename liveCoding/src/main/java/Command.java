/**
 * Created by bOGDy on 12/12/2015.
 */

import java.io.Serializable;
public class Command implements Serializable
{
    protected String test; //this is for testing purposes
    public void undo(){};
    public void redo(){};

    public Command()
    {
        test = "test";
    }

    public Command(String inString)
    {
        test = inString;
    }

    public String getTest()
    {
        return test;
    } //for testing purposes

}
