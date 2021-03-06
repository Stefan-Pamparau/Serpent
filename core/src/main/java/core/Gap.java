package core;

import java.util.*;

public class Gap {
    private Character[] buffer;
    private int gapStart;
    private int gapEnd;
    private int capacity;
    private int textLength;
    private List<Integer> cursors;
    private int numberOfLines;


    /**
     * core.Gap buffer with support for multiple cursors.
     * It automatically reallocates the space after a threshold of 80% has been reached.
     * @param capacity - initial capacity before reallocation
     */
    public Gap(int capacity) {
        buffer = new Character[capacity];
        this.capacity = capacity;
        this.gapStart = 0;
        this.gapEnd = capacity - 1;
        this.textLength = 0;
        this.cursors = new ArrayList<>();
        this.cursors.add(0);
        this.numberOfLines = 1;
    }


    private void insertHelper(Character c) {
        System.out.println("Buffer stress: " + (textLength * 100 / capacity) + "%");
        if ((textLength * 100 / capacity) > 80) {
            buffer = reallocateGapBuffer();
        }
        if (gapStart < gapEnd) {
            buffer[gapStart] = c;
            gapStart++;
            textLength++;
            if (c.equals('\n'))
                numberOfLines++;
        }
    }


    /**
     * Multi-cursor insert action
     * @param c - inserted character
     */
    public void insert(Character c) {
        for (int cursorPos = cursors.size() - 1; cursorPos >= 0; cursorPos--) {
            jumpTo(cursors.get(cursorPos));
            insertHelper(c);
            cursors.set(cursorPos, cursors.get(cursorPos) + cursorPos + 1);
        }
        System.out.println("Cursors: " + Collections.singleton(cursors));
    }


    /**
     * Multi-cursor backspace action
     */
    public void backspace() {
        int delta = 1;
        for (int cursorPos = cursors.size() - 1; cursorPos >= 0; cursorPos--) {
            if (cursors.get(cursorPos) == 0)
                continue;
            if (cursors.get(0) == 0)
                delta = 0;

            jumpTo(cursors.get(cursorPos));
            if (gapStart > 0) {
                gapStart--;
                if (buffer[gapStart].equals('\n'))
                    numberOfLines--;
                cursors.set(cursorPos, cursors.get(cursorPos) - cursorPos - delta);
                this.textLength--;
            }
        }
        removeDuplicateCursors();
        System.out.println("Cursors: " + Collections.singletonList(cursors));
    }


    /**
     * Multi-cursor delete action
     */
    public void delete() {
        for (int cursorPos = cursors.size() - 1; cursorPos >= 0; cursorPos--) {
            jumpTo(cursors.get(cursorPos));
            if (gapEnd < this.capacity - 1) {
                if (buffer[gapEnd].equals('\n'))
                    numberOfLines--;
                gapEnd++;
                this.textLength--;
            }

            cursors.set(cursorPos, cursors.get(cursorPos) - cursorPos);
        }

        removeDuplicateCursors();
        System.out.println(Collections.singletonList(cursors));
    }


    /**
     * Double the size of the gap buffer on each reallocation
     * @return reallocated buffer
     */
    private Character[] reallocateGapBuffer() {
        this.capacity *= 2;
        Character newBuffer[] = new Character[this.capacity];
        System.arraycopy(this.buffer, 0, newBuffer, 0, gapStart);
        System.arraycopy(this.buffer, gapEnd, newBuffer, this.capacity - (this.buffer.length - gapEnd),
                this.buffer.length - gapEnd);

        this.gapEnd = this.capacity - (this.buffer.length - gapEnd);

        return newBuffer;
    }


    /**
     * Used inside jumpTo()
     */
    private void goLeft() {
        if (gapStart > 0) {
            gapEnd--;
            gapStart--;
            buffer[gapEnd] = buffer[gapStart];
        }
    }


    /**
     * Used inside jumpTo()
     */
    private void goRight() {
        if (gapEnd < capacity - 1) {
            buffer[gapStart] = buffer[gapEnd];
            gapStart++;
            gapEnd++;
        }
    }


    /**
     * Move cursor to the given position.
     * @param position - new cursor position
     */
    public void jumpTo(int position) {
        int delta = Math.abs(position - gapStart);
        for (int i = 0; i < delta; i++)
            if (gapStart < position)
                goRight();
            else
                goLeft();
    }


    /**
     * User controlled action. Move all cursors to the left by one position.
     */
    public void moveKeyLeft() {
        if (gapStart > 0) {
            gapEnd--;
            gapStart--;
            buffer[gapEnd] = buffer[gapStart];
        }

        for (int cursorPos = cursors.size() - 1; cursorPos >= 0; cursorPos--) {
            if (cursors.get(cursorPos) > 0)
                cursors.set(cursorPos, cursors.get(cursorPos) - 1);
                if (cursors.get(cursorPos) == 0)
                    removeDuplicateCursors();
        }
        System.out.println(Collections.singletonList(cursors));
    }


    /**
     * User controlled action. Move all cursors to the right by one position.
     */
    public void moveKeyRight() {
        if (gapEnd < capacity - 1) {
            buffer[gapStart] = buffer[gapEnd];
            gapStart++;
            gapEnd++;
        }

        for (int cursorPos = cursors.size() - 1; cursorPos >= 0; cursorPos--) {
            if (cursors.get(cursorPos) < textLength)
                cursors.set(cursorPos, cursors.get(cursorPos) + 1);
                if (cursors.get(cursorPos) == textLength)
                    removeDuplicateCursors();
        }
        System.out.println(Collections.singletonList(cursors));
    }

    /**
     * Move cursor to given position
     * @param position - new cursor position
     */
    public void jumpCursorTo(int position) {
        int delta = Math.abs(position - gapStart);
        for (int i = 0; i < delta; i++)
            if (gapStart < position)
                moveKeyRight();
            else
                moveKeyLeft();
    }


    /**
     * Remove cursors that reside at the same position.
     */
    private void removeDuplicateCursors() {
        this.cursors = new ArrayList<>(new LinkedHashSet<>(this.cursors));
    }


    /**
     * Add a new cursor inside the text.
     * @param position of new cursor
     */
    public void addCursor(int position) {
        if (cursors.contains(position))
            return;
        this.cursors.add(position);
        Collections.sort(this.cursors);
    }


    public List<Integer> getCursors() {
        return this.cursors;
    }


    public int getNumberOfLines() {
        return numberOfLines;
    }


    public Character getCursorCharacter() {
        return buffer[gapEnd];
    }


    public Character getCharacter(int i) {
        if (i < gapStart)
            return buffer[i];
        else
            return buffer[gapEnd - gapStart + i];
    }


    public int getGapEnd() {
        return gapEnd;
    }


    public int getCapacity() {
        return capacity;
    }


    public int getTextLength() {
        return this.textLength;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gapStart; i++)
            sb.append(buffer[i]);
        for (int i = gapEnd; i < capacity - 1; i++)
            sb.append(buffer[i]);

        return sb.toString();
    }

}
