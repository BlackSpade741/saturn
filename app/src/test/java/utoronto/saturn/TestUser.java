package utoronto.saturn;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TestUser {

    /*
    * Prevents Registering with an Empty Username.
    */
    @Test
    public void TestEmptyUsername() {
        Throwable thrown = catchThrowable(() -> {
            new User("", "m@gic@user.ca", "abcabcabc");
        });
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    /*
    * Prevents Registering with an Empty Email.
    */
    @Test
    public void TestEmptyEmail() {
        Throwable thrown = catchThrowable(() -> {
            new User("magic", "", "abcabcabc");
        });
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    /*
    * Prevents Registering with an Empty Password.
    */
    @Test
    public void TestEmptyPassword() {
        Throwable thrown = catchThrowable(() -> {
            new User("magic", "a@a", "");
        });
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    /*
    * Ensures Minimum Password Length.
    */
    @Test
    public void TestPasswordTooShort() {
        Throwable thrown = catchThrowable(() -> {
            new User("magic", "a@a", "abc");
        });
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    /*
    * Prevents Entering an Email without At.
    */
    @Test
    public void TestEmailWithoutAt() {
        Throwable thrown = catchThrowable(() -> {
            new User("magic", "aa", "aefaefaef");
        });
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    /*
    * Prevents following the same user twice.
    */
    @Test(expected = IllegalStateException.class)
    public void TestDuplicateFollow() {
        User A = new User("magic", "a@a", "aefaefaef");
        A.addToFollowedCreators(new User("magic", "b@b", "abcabcabc"));
        A.addToFollowedCreators(new User("magic", "c@c", "cdecdecde"));
        A.addToFollowedCreators(new User("magic", "b@b", "abcabcabc"));
    }
}
