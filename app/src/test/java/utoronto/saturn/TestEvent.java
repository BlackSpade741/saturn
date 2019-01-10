package utoronto.saturn;

import org.junit.Test;


import static org.assertj.core.api.Assertions.*;

public class TestEvent {
    private static final int id = 1;
    private static final String name = "b";
    private static final String creator = "creator";
    private static final String desc = "c";
    private static final long releaseDate = System.currentTimeMillis();
    private static final Event correctEvent = new Event(id, name, null, releaseDate, creator, desc);

    @Test
    public void testIDIsCorrect() {
        assertThat(correctEvent.getID()).isEqualTo(id);
    }

    @Test
    public void testNameIsCorrect() {
        assertThat(correctEvent.getName()).isEqualTo(name);
    }

    @Test
    public void testNameIsEmpty() {
        assertThatThrownBy(() -> new Event(1, "", null, 0, "c", "d")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testReleaseDateIsCorrect() {
        assertThat(correctEvent.getReleaseDate()).isEqualTo(releaseDate);
    }


    @Test
    public void testTimeTillReleaseIsCorrect() {
        assertThat(correctEvent.timeTillRelease()).isGreaterThanOrEqualTo(0);
    }

}
