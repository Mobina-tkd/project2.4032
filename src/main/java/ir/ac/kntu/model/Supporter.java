package ir.ac.kntu.model;

import java.util.HashMap;
import java.util.Map;

public class Supporter {
    private Map<String, Map<String, String>> admin;

    public Supporter() {
        admin = new HashMap<>();
        admin.put("admin1", Map.of(
                "firstName", "Sara",
                "lastName", "Hasani",
                "userName", "Sara_H82",
                "password", "S1382ara_"
        ));

        admin.put("admin2", Map.of(
                "firstName", "Mohammad",
                "lastName", "Lotfi",
                "userName", "mmd_L80",
                "password", "M1380md_"
        ));

    }
}
