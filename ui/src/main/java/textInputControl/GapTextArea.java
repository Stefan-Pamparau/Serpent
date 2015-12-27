package textInputControl;

import core.Gap;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class GapTextArea extends TextArea {
    private static final Integer DEFAULT_GAP_BUFFER_INITIAL_CAPACITY = 128;
    private Gap gapBuffer;
    private TextArea textArea;

    public GapTextArea() {
        this("");
    }

    public GapTextArea(String text) {
        super(text);
        gapBuffer = new Gap(DEFAULT_GAP_BUFFER_INITIAL_CAPACITY);
        textArea = new TextArea(text);
        addKeyTypedEventHandler();
        addKeyPressedEventHandler();
        addKeyReleasedEventHandler();
        addActionEventHandler();
    }

    private void addKeyTypedEventHandler() {
        this.setOnKeyTyped(event -> {
            KeyCode keyCode = event.getCode();
            String c = event.getCharacter();
            if (keyCode.equals(KeyCode.BACK_SPACE)) {
                gapBuffer.backspace();
            } else if (keyCode.isLetterKey()) {
                gapBuffer.insert(c.toCharArray()[0]);
            }
            textArea.setText(gapBuffer.toString());
        });
    }

    private void addKeyPressedEventHandler() {
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                gapBuffer.moveKeyLeft();
            } else if (event.getCode() == KeyCode.RIGHT) {
                gapBuffer.moveKeyRight();
            }
            if (event.getCode() == KeyCode.CONTROL) {
                gapBuffer.addCursor(0);
            }
            if (event.getCode() == KeyCode.DELETE) {
                gapBuffer.delete();
            }
        });
    }

    private void addKeyReleasedEventHandler() {
        this.setOnKeyReleased(event -> {

        });
    }

    private void addActionEventHandler() {
        this.addEventHandler(ActionEvent.ACTION, event -> {

        });
    }
}
