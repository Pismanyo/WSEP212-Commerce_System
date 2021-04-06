package TestComponent.UnitTesting.UserComponentTest;


import Server.Domain.CommonClasses.Response;
import Server.Domain.UserManager.CommerceSystem;
import Server.Domain.UserManager.User;
import Server.Domain.UserManager.UserController;
import Server.Domain.UserManager.UserDAO;
import Server.Domain.UserManager.Permissions;
import Server.Service.CommerceService;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private CommerceService commerceService;
    private UserController userController;
    private UserDAO userDAO;
    private String initialUserName;

    @BeforeEach
    public void setUp(){
        commerceService = CommerceService.getInstance();
        commerceService.init();
        userController = UserController.getInstance();
        userDAO = UserDAO.getInstance();
        initialUserName = commerceService.addGuest().getResult();
    }

    @Test
    public void removeGuestTest(){
        userController.addGuest();
        assertTrue(userController.getConnectedUsers().containsKey("Guest2"));
        userController.removeGuest("Guest2");
        assertFalse(userController.getConnectedUsers().containsKey("Guest2"));
    }

    @Test
    public void addGuestTest(){
        assertFalse(userController.getConnectedUsers().containsKey("Guest2"));
        assertEquals("Guest2", userController.addGuest().getResult());
        assertTrue(userController.getConnectedUsers().containsKey("Guest2"));
    }

    @Test
    public void registerTest(){
        assertFalse(userDAO.userExists("Jacob").getResult());
        assertFalse(userController.register(initialUserName, "Jacob", "12345").isFailure());
        assertTrue(userDAO.userExists("Jacob").getResult());
        assertTrue(userController.register(initialUserName, "Jacob", "12345").isFailure());
    }

    @Test
    public void loginTest(){
        userController.register(initialUserName, "Jacob", "12345");
        assertFalse(userController.getConnectedUsers().containsKey("Jacob"));
        // login for non-existent user
        assertFalse(userDAO.userExists("Alice").getResult());
        assertTrue(userController.login(initialUserName, "Alice", "abcd").isFailure());
        // login for existing user w/ incorrect password
        assertTrue(userController.login(initialUserName, "Jacob", "incorrect").isFailure());
        assertFalse(userController.getConnectedUsers().containsKey("Jacob"));
        // login for existing user w/ correct password
        Response<String> response = userController.login(initialUserName, "Jacob", "12345");
        assertFalse(response.isFailure());
        assertEquals("Jacob", response.getResult());
        assertTrue(userController.getConnectedUsers().containsKey("Jacob"));
    }

    @Test
    public void logoutTest(){
        userController.register(initialUserName, "Jacob", "24680");
        Response<String> loginResult = userController.login(initialUserName, "Jacob", "24680");
        String newUserName = loginResult.getResult();
        assertEquals("Jacob", newUserName);

        assertTrue(userController.getConnectedUsers().containsKey("Jacob"));

        Response<String> logoutResult = userController.logout(newUserName);
        String logoutName = logoutResult.getResult();
        assertNotEquals("Jacob", logoutName);

        assertFalse(userController.getConnectedUsers().containsKey("Jacob"));
    }

    @Test
    public void appointOwnerTest(){
        String guest = commerceService.addGuest().getResult();
        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");

        Response<String> login = userController.login(initialUserName, "yoni", "pis");
        String newUserName = login.getResult();
        userController.login(guest, "tal", "kadosh");

        Response<Integer> storeRes = userController.openStore(newUserName, "eggStore");
        int storeID = storeRes.getResult();

        User tal = userController.getConnectedUsers().get("tal");
        assertFalse(tal.isOwner(storeID));

        userController.appointOwner(newUserName, "tal", storeID);

        tal = userController.getConnectedUsers().get("tal");
        assertTrue(tal.isOwner(storeID));
    }

    @Test
    public void appointManagerTest(){
        String guest = commerceService.addGuest().getResult();
        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");

        Response<String> login = userController.login(initialUserName, "yoni", "pis");
        String newUserName = login.getResult();
        userController.login(guest, "tal", "kadosh");

        Response<Integer> storeRes = userController.openStore(newUserName, "eggStore");
        int storeID = storeRes.getResult();

        User tal = userController.getConnectedUsers().get("tal");
        assertFalse(tal.isManager(storeID));

        userController.appointManager(newUserName, "tal", storeID);

        tal = userController.getConnectedUsers().get("tal");
        assertTrue(tal.isManager(storeID));
    }

    @Test
    public void removeOwnerTest(){
        String guest = commerceService.addGuest().getResult();
        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");

        Response<String> login = userController.login(initialUserName, "yoni", "pis");
        String newUserName = login.getResult();
        userController.login(guest, "tal", "kadosh");

        Response<Integer> storeRes = userController.openStore(newUserName, "eggStore");
        int storeID = storeRes.getResult();

        User tal = userController.getConnectedUsers().get("tal");
        assertFalse(tal.isOwner(storeID));

        userController.appointOwner(newUserName, "tal", storeID);

        tal = userController.getConnectedUsers().get("tal");
        assertTrue(tal.isOwner(storeID));

        userController.removeOwnerAppointment(newUserName, "tal", storeID);

        tal = userController.getConnectedUsers().get("tal");
        assertFalse(tal.isOwner(storeID));

    }

    @Test
    public void removeManagerTest(){
        String guest = commerceService.addGuest().getResult();
        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");

        Response<String> login = userController.login(initialUserName, "yoni", "pis");
        String newUserName = login.getResult();
        userController.login(guest, "tal", "kadosh");

        Response<Integer> storeRes = userController.openStore(newUserName, "eggStore");
        int storeID = storeRes.getResult();

        User tal = userController.getConnectedUsers().get("tal");
        assertFalse(tal.isManager(storeID));

                    userController.appointManager(newUserName, "tal", storeID);

        tal = userController.getConnectedUsers().get("tal");
        assertTrue(tal.isManager(storeID));

        userController.removeManagerAppointment(newUserName, "tal", storeID);

        tal = userController.getConnectedUsers().get("tal");
        assertFalse(tal.isManager(storeID));
    }

    @Test
    public void cascadingRemovalTest() {
        String guest = commerceService.addGuest().getResult();
        String guest2 = commerceService.addGuest().getResult();
        String guest3 = commerceService.addGuest().getResult();
        String guest4 = commerceService.addGuest().getResult();
        String guest5 = commerceService.addGuest().getResult();

        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");
        userController.register(initialUserName, "jacob", "lol");
        userController.register(initialUserName, "shaked6", "lol");
        userController.register(initialUserName, "aviad", "lol");
        userController.register(initialUserName, "almog", "lol");

        Response<String> yoniLogin = userController.login(initialUserName, "yoni", "pis");
        String yoniUserName = yoniLogin.getResult();
        Response<String> talLogin = userController.login(guest, "tal", "kadosh");
        String talUserName = talLogin.getResult();
        Response<String> jacobLogin = userController.login(guest2, "jacob", "lol");
        String jacobUserName = jacobLogin.getResult();
        userController.login(guest3, "shaked6", "lol");
        userController.login(guest4, "aviad", "lol");
        userController.login(guest5, "almog", "lol");

        Response<Integer> storeRes = userController.openStore(yoniUserName, "eggStore");
        int storeID = storeRes.getResult();

        userController.appointOwner(yoniUserName, "tal", storeID);
        userController.appointManager(talUserName, "jacob", storeID);
        userController.addPermission(talUserName, storeID, "jacob", Permissions.APPOINT_MANAGER);
        userController.appointOwner(talUserName, "aviad", storeID);
        userController.appointOwner(talUserName, "almog", storeID);
        userController.appointManager(jacobUserName, "shaked6", storeID);

        assertTrue(userController.getConnectedUsers().get("yoni").isOwner(storeID));
        assertTrue(userController.getConnectedUsers().get("tal").isOwner(storeID));
        assertTrue(userController.getConnectedUsers().get("jacob").isManager(storeID));
        assertTrue(userController.getConnectedUsers().get("aviad").isOwner(storeID));
        assertTrue(userController.getConnectedUsers().get("almog").isOwner(storeID));
        assertTrue(userController.getConnectedUsers().get("shaked6").isManager(storeID));

        userController.removeOwnerAppointment(yoniUserName, "tal", storeID);

        assertTrue(userController.getConnectedUsers().get("yoni").isOwner(storeID));
        assertFalse(userController.getConnectedUsers().get("tal").isOwner(storeID));
        assertFalse(userController.getConnectedUsers().get("jacob").isManager(storeID));
        assertFalse(userController.getConnectedUsers().get("aviad").isOwner(storeID));
        assertFalse(userController.getConnectedUsers().get("almog").isOwner(storeID));
        assertFalse(userController.getConnectedUsers().get("shaked6").isManager(storeID));
    }

    @Test
    public void addPermissionTest(){
        String guest = commerceService.addGuest().getResult();

        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");

        Response<String> yoniLogin = userController.login(initialUserName, "yoni", "pis");
        String yoniUserName = yoniLogin.getResult();
        userController.login(guest, "tal", "kadosh");

        Response<Integer> storeRes = userController.openStore(yoniUserName, "eggStore");
        int storeID = storeRes.getResult();

        userController.appointManager(yoniUserName, "tal", storeID);

        assertFalse(userController.getConnectedUsers().get("tal").getStoresManaged().get(storeID).contains(Permissions.ADD_PRODUCT_TO_STORE));

        userController.addPermission(yoniUserName, storeID, "tal", Permissions.ADD_PRODUCT_TO_STORE);

        assertTrue(userController.getConnectedUsers().get("tal").getStoresManaged().get(storeID).contains(Permissions.ADD_PRODUCT_TO_STORE));

    }

    @Test
    public void removePermissionTest(){
        String guest = commerceService.addGuest().getResult();

        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");

        Response<String> yoniLogin = userController.login(initialUserName, "yoni", "pis");
        String yoniUserName = yoniLogin.getResult();
        userController.login(guest, "tal", "kadosh");

        Response<Integer> storeRes = userController.openStore(yoniUserName, "eggStore");
        int storeID = storeRes.getResult();

        userController.appointManager(yoniUserName, "tal", storeID);
        userController.addPermission(yoniUserName, storeID, "tal", Permissions.ADD_PRODUCT_TO_STORE);

        assertTrue(userController.getConnectedUsers().get("tal").getStoresManaged().get(storeID).contains(Permissions.ADD_PRODUCT_TO_STORE));

        userController.removePermission(yoniUserName, storeID, "tal", Permissions.ADD_PRODUCT_TO_STORE);

        assertFalse(userController.getConnectedUsers().get("tal").getStoresManaged().get(storeID).contains(Permissions.ADD_PRODUCT_TO_STORE));
    }

    @Test
    public void getStoreWorkersDetailsTest(){
        String guest = commerceService.addGuest().getResult();
        String guest2 = commerceService.addGuest().getResult();
        String guest3 = commerceService.addGuest().getResult();
        String guest4 = commerceService.addGuest().getResult();
        String guest5 = commerceService.addGuest().getResult();

        userController.register(initialUserName, "tal", "kadosh");
        userController.register(initialUserName, "yoni", "pis");
        userController.register(initialUserName, "jacob", "lol");
        userController.register(initialUserName, "shaked6", "lol");
        userController.register(initialUserName, "aviad", "lol");
        userController.register(initialUserName, "almog", "lol");

        Response<String> yoniLogin = userController.login(initialUserName, "yoni", "pis");
        String yoniUserName = yoniLogin.getResult();
        Response<String> talLogin = userController.login(guest, "tal", "kadosh");
        String talUserName = talLogin.getResult();
        Response<String> jacobLogin = userController.login(guest2, "jacob", "lol");
        String jacobUserName = jacobLogin.getResult();
        userController.login(guest3, "shaked6", "lol");
        userController.login(guest4, "aviad", "lol");
        userController.login(guest5, "almog", "lol");

        Response<Integer> storeRes = userController.openStore(yoniUserName, "eggStore");
        int storeID = storeRes.getResult();

        userController.appointOwner(yoniUserName, "tal", storeID);
        userController.appointManager(talUserName, "jacob", storeID);
        userController.addPermission(talUserName, storeID, "jacob", Permissions.APPOINT_MANAGER);
        userController.appointOwner(talUserName, "aviad", storeID);
        userController.appointOwner(talUserName, "almog", storeID);
        userController.appointManager(jacobUserName, "shaked6", storeID);

        Response<List<User>> result = userController.getStoreWorkersDetails(yoniUserName, storeID);
        assertFalse(result.isFailure());
        List<User> actualUsers = result.getResult();

        List<String> users = new Vector<>();
        users.add("yoni");
        users.add("tal");
        users.add("jacob");
        users.add("aviad");
        users.add("almog");
        users.add("shaked6");

        assertEquals(6, users.size());
        for(User user : actualUsers){
            assertTrue(users.contains(user.getName()));
        }
    }
}
/*CommerceService commerceService = CommerceService.getInstance();
        commerceService.init();
        commerceService.addGuest();
        // Guest1
        commerceService.register("Guest1", "tal", "kadosh");
        System.out.println(commerceService.logout("Guest1").isFailure());
        System.out.println(commerceService.login("Guest1", "tal", "kadosh").getResult());
        commerceService.logout("tal");
        //Guest2
        System.out.println(commerceService.register("Guest2", "yoni", "pis").isFailure());
        System.out.println(commerceService.login("Guest2", "yoni", "pis").isFailure());

        Response<Integer> res = commerceService.openStore("yoni", "eggStore");
        commerceService.appointStoreOwner("yoni", "tal", res.getResult());
        commerceService.logout("yoni");
        //Guest3
        System.out.println(commerceService.register("Guest3", "jacob", "lol").isFailure());
        System.out.println(commerceService.register("Guest3", "jacob2", "lol").isFailure());
        System.out.println(commerceService.register("Guest3", "jacob3", "lol").isFailure());
        System.out.println(commerceService.register("Guest3", "jacob4", "lol").isFailure());
        System.out.println(commerceService.login("Guest3", "tal", "kadosh").isFailure());
        commerceService.appointStoreOwner("tal", "jacob", res.getResult());
        commerceService.appointStoreOwner("tal", "jacob2", res.getResult());
        commerceService.appointStoreManager("tal", "jacob3", res.getResult());
        commerceService.addPermission("tal", res.getResult(), "jacob3", Permissions.ADD_PRODUCT_TO_STORE);
        commerceService.removePermission("tal", res.getResult(), "jacob3", Permissions.ADD_PRODUCT_TO_STORE);
        commerceService.addPermission("tal", res.getResult(), "jacob3", Permissions.APPOINT_MANAGER);
        commerceService.removePermission("tal", res.getResult(), "jacob3", Permissions.APPOINT_MANAGER);

        commerceService.logout("tal");
        //Guest4
        System.out.println(commerceService.login("Guest4", "jacob3", "lol").isFailure());
        commerceService.appointStoreManager("jacob3", "jacob4", res.getResult());
        commerceService.logout("jacob3");
        //Guest5
        System.out.println(commerceService.login("Guest5", "yoni", "pis").isFailure());
        //commerceService.removeOwnerAppointment("yoni", "tal", res.getResult());
        for(User user : commerceService.getStoreWorkersDetails("yoni", res.getResult()).getResult()){
            System.out.println(user.getName());
        }
        commerceService.logout("yoni");
        //Guest6


        System.out.println(UserDAO.getInstance().getAppointments("yoni", res.getResult()).getResult().toString());
        System.out.println(UserDAO.getInstance().getAppointments("tal", res.getResult()).getResult().toString());
        System.out.println(UserDAO.getInstance().getAppointments("jacob3", res.getResult()).getResult().toString());

        System.out.println(UserDAO.getInstance().getUser("jacob3").getStoresManaged().get(res.getResult()));
        System.out.println(UserDAO.getInstance().getUser("jacob4").getStoresManaged().get(res.getResult()));*/