/**
 * Created by bOGDy on 12/12/2015.
 */

import java.io.Serializable;

public abstract class Command implements Serializable
{
    private int position;
    private Character c;

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public Character getC()
    {
        return c;
    }

    public void setC(Character c)
    {
        this.c = c;
    }


    public void undo(){};
    public void redo(){};


    public Command(int position, Character c)
    {
        this.position = position;
        this.c = c;
    }

    public abstract command_type getType();

}
