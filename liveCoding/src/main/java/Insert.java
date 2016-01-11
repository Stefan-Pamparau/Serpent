/**
 * Created by bOGDy on 12/12/2015.
 */
public class Insert extends Command {

    public Insert(int position, Character c)
    {
        super(position, c);
    }

    @Override
    public command_type getType()
    {
        return command_type.INSERT;
    }
}
