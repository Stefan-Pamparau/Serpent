package livecoding;

/**
 * Created by li4ick on 13-Jan-16.
 */
public class Backspace extends Command {

    public Backspace(int position) {
        super(position, null);
    }

    @Override
    public command_type getType() {
        return command_type.BACKSPACE;
    }
}
