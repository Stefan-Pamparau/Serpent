/**
 * Created by bOGDy on 12/12/2015.
 */
public class Delete extends Command {

    public Delete(int position, Character c)
    {
        super(position, c);
    }

    @Override
    public command_type getType()
    {
        return command_type.DELETE;
    }
}
