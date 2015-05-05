package controllers;

import models.Users;
import play.cache.Cache;
import play.mvc.Http;

/**
 * Various Controller utility functions
 * @author Rafael Veras
 */
public class Util {

    /**
     * Retrieves the current HTTP session
     * @return The current http session
     */
    public static Http.Session getCurrentSession(){
        return Http.Context.current().session();
    }

    /**
     * Inserts a value into the site-wide cache
     * @param key The first half of the lookup key
     * @param key2 The second half of the lookup key
     * @return The mapped value
     * @note The key is composable so you may only need `key` and `key2` can be null
     */
    private static void insertIntoCache(String key, String key2, Object value) {
        Cache.set(key + key2, value);
    }

    /**
     * Retrieves a value from the site-wide Cache
     * @param key The first half of the lookup key
     * @param key2 The second half of the lookup key
     * @return The mapped value
     * @note The key is composable so you may only need `key` and `key2` can be null
     */
    private static String getFromCache(String key, String key2) {
        return (String)Cache.get(key + key2);
    }

    /**
     * Inserts a value into the current HTTP session
     * @param key The key to add
     * @param value The value to add
     */
    public static void insertIntoSession(String key, String value) {
        Http.Session session = getCurrentSession();
        session.put(key, value);
    }

    /**
     * Retrieves a value from the current HTTP Session
     * @param key The key for lookup
     * @return The mapped value or null
     */
    private static String getFromSession(String key) {
        Http.Session session = getCurrentSession();
        return session.get(key);
    }

    /**
     * Creates a session and cache object for the username.
     * This does two things, creates a session object/key for UUID,
     * and creates a cache object/key for uuid+username
     * @param username The username
     * @note The cache has an expiration time
     */
    public static void createUserCache(String username)
    {
        // get the uuid from cache
        String uuid = getFromSession("uuid");
        // if it doesnt exist, create a new one and insert into session
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
            insertIntoSession("uuid", uuid);
        }
        // Insert a unique string-to-username mapping in the cache.
        // if `uuid` was "1234", and username was bob, then the cache would have 1234bob = bob
        insertIntoCache(uuid, "username", username);
    }

    /**
     * Retrieves a value from the site-wide cache
     * @param key The first half of the key
     * @param key2 The second half of the key
     * @return The mapped value or null
     * @note The key is composable so you may only need `key` and `key2` can be null
     */
    public static String getFromUserCache(String key, String key2) {
        String val = getFromSession(key);
        if (val != null)
            return getFromCache(key, key2);
        return null;
    }
    public static Http.Session setUserToSession(Users user) {
        Http.Session session = getCurrentSession();
        session.put("username", user.uname);
        session.put("fname", user.fname);
        session.put("lname", user.lname);
        session.put("email", user.email);
        session.put("mob", String.valueOf(user.mob));
        session.put("role", String.valueOf(user.role.size()));
        //session.put("picid", user.picid);
        session.put("cdate", String.valueOf(user.cdate));
        return session;
    }
}
