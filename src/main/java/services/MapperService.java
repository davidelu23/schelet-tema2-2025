package services;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service for providing a singleton ObjectMapper instance.
 */
public final class MapperService {
    private static ObjectMapper instance;

    private MapperService() { }

    /**
     * Returns the singleton instance of the ObjectMapper.
     * @return The singleton instance.
     */
    public static ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper();
        }
        return instance;
    }

    /**
     * Resets the singleton instance.
     */
    public static void reset() {
        instance = null;
    }
}
