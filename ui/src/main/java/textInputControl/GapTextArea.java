package textInputControl;

import java.util.ArrayList;
import java.util.List;

import core.Gap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class GapTextArea {
    private static final Integer DEFAULT_GAP_BUFFER_INITIAL_CAPACITY = 128;
    private Gap gapBuffer;
    private TextArea textArea;
    private Tab tab;
    private Thread cursorThread;
    private Boolean run;


    public GapTextArea(TextArea textArea, Tab tab) {
        this("", textArea, tab);
    }

    public GapTextArea(String text, TextArea textArea, Tab tab) {
        gapBuffer = new Gap(DEFAULT_GAP_BUFFER_INITIAL_CAPACITY);
        this.textArea = textArea;
        this.tab = tab;
        run = true;
        addKeyTypedEventHandler();
        addKeyPressedEventHandler();
        addKeyReleasedEventHandler();
        addActionEventHandler();
        addTabSelectedListener();
        addTabClosedListener();
    }

    public GapTextArea(String text, TextArea textArea, Tab tab, Gap gapBuffer) {
        this(text, textArea, tab);
        this.gapBuffer = gapBuffer;
        this.textArea.setText(gapBuffer.toString());
    }

    public void closeCursorThread() {
        run = false;
    }

    private void addTabClosedListener() {
        tab.setOnClosed(event -> cursorThread.interrupt());
    }

    private void addTabSelectedListener() {
        tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initializeAndStartCursorThread();
            }
        });
    }

    private void initializeAndStartCursorThread() {
        cursorThread = new Thread() {
            @Override
            public void run() {
                while (tab.isSelected() && run) {
                    List<Integer> cursors = new ArrayList<>(gapBuffer.getCursors());
                    for (Integer cursor : cursors) {
                        Platform.runLater(() -> textArea.positionCaret(cursor));
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        cursorThread.start();
    }

    private void addKeyTypedEventHandler() {
        textArea.setOnKeyTyped(event -> {
            String c = event.getCharacter();
            if (!Character.isISOControl(c.toCharArray()[0])) {
                gapBuffer.insert(c.toCharArray()[0]);
            }
        });
    }

    private void addKeyPressedEventHandler() {
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                gapBuffer.insert('\n');
            }
            if (event.getCode() == KeyCode.BACK_SPACE) {
                System.out.println("Backspace");
                gapBuffer.backspace();
            }
            if (event.getCode() == KeyCode.LEFT) {
                gapBuffer.moveKeyLeft();
            }
            if (event.getCode() == KeyCode.RIGHT) {
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
        textArea.setOnKeyReleased(event -> {
            textArea.clear();
            textArea.setText(gapBuffer.toString());
        });
    }

    private void addActionEventHandler() {
        textArea.addEventHandler(ActionEvent.ACTION, event -> {
        });
    }
}
