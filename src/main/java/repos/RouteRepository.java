package repos;

import domain.Campaign;
import domain.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteRepository {

    private final List<Route> routes;

    RouteRepository() {
        final Campaign c1 = new Campaign(1L, "Campaign 1");
        final Campaign c2 = new Campaign(2L, "Campaign 2");
        final Campaign c3 = new Campaign(3L, "Campaign 3");

        routes = Arrays.asList(
                new Route(10L, "Route 10", c1),
                new Route(20L, "Route 20", c2),
                new Route(30L, "Route 30", c3)
        );
    }

    public List<Route> getActiveRoutes() {
        return new ArrayList<>(routes);
    }

}
