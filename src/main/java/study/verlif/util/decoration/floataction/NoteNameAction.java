package study.verlif.util.decoration.floataction;

import study.verlif.ui.stage.common.FloatAction;

public class NoteNameAction implements FloatAction {
    private String tagName;

    public NoteNameAction(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String tag() {
        return tagName;
    }

    @Override
    public String style() {
        return "-fx-border-color: black;-fx-border-width: 1px";
    }
}
