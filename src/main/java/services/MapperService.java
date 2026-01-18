package services;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperService {
    private static ObjectMapper instance;

    private MapperService() {}

    public static ObjectMapper getInstance() {
        if (instance == null)
            instance = new ObjectMapper();

        return instance;
    }

    public static void reset() {
        instance = null;
    }
}
