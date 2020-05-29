package multiplethreadedchatapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import loginpackage.Login;

public class LoginTest {

    @Test
    public void userFound() {
        assertEquals(true, Login.authenticate("salma", "12345"));
    }

    @Test
    public void userNotFound() {
        assertEquals(false, Login.authenticate("ahmed", "203040"));
    }

    @Test
    public void userAlreadyRegistered() {
        assertEquals(false, Login.register("salma", "203040"));
    }

    @Test
    public void userRegisteredSuccessfully() {
        assertEquals(true, Login.register("deena", "12345"));
    }
}