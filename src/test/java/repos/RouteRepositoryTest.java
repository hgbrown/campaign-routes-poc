package repos;

import domain.Route;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static org.assertj.core.api.Assertions.assertThat;

class RouteRepositoryTest {

    private RouteRepository cut = new RouteRepository();

    @Test
    @DisplayName("A sanity check that we are getting all the routes we expect")
    void shoudHaveAllExpectedRoutes() {
        assertThat(cut.getActiveRoutes()).extracting("id").containsExactly(10L, 20L, 30L);
    }

    @Test
    @DisplayName("Given a list of routes we want to create a map from the unique campaign id to the unique route associated with that campaign")
    void shouldBeAbleToCreateMapFromCampaignIdToRoute() {
        final Map<Long, List<Route>> m = cut.getActiveRoutes().stream().collect(groupingBy(r -> r.getCampaign().getId()));
        assertThat(m).containsKeys(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Given a list of routes we want to create a map from the unique campaign id to an optional route uniquely associated with that campaign")
    void shouldBeAbleToCreateMapToOptionalWithAllOptionalValuesPresentUsingCustomCollector() {
        final Collector<Route, List<Route>, Optional<Route>> d = Collector.of(
                ArrayList::new,
                List::add,
                (routes, routes2) -> {
                    final ArrayList<Route> tmp = new ArrayList<>(routes);
                    tmp.addAll(routes2);
                    return tmp;
                },
                routes -> routes.size() > 0 ? Optional.ofNullable(routes.get(0)) : Optional.empty()
        );

        final Map<Long, Optional<Route>> map = cut.getActiveRoutes()
                .stream()
                .collect(groupingBy(r -> r.getCampaign().getId(), d));


        assertThat(map).containsKeys(1L, 2L, 3L);
        assertThat(map.values()).allMatch(Optional::isPresent);
    }

    @Test
    @DisplayName("Given a list of routes we want to create a map from the unique campaign id to the route uniquely associated with that campaign")
    void shouldBeAbleToCreateMapToSingleRouteUsingCustomCollector() {
        final Collector<Route, List<Route>, Route> d = Collector.of(
                ArrayList::new,
                List::add,
                (routes, routes2) -> {
                    final ArrayList<Route> tmp = new ArrayList<>(routes);
                    tmp.addAll(routes2);
                    return tmp;
                },
                routes -> routes.get(0)
        );

        final Map<Long, Route> map = cut.getActiveRoutes()
                .stream()
                .collect(groupingBy(r -> r.getCampaign().getId(), d));

        assertThat(map).containsKeys(1L, 2L, 3L);
        assertThat(map.values()).extracting("id").containsExactly(10L, 20L, 30L);
    }

    @Test
    @DisplayName("Using a reducing collector we want to create a map from the unique campaign id to an optional route uniquely associated with that campaign")
    void shouldBeAbleToCreateMapToOptionalWithAllOptionalValuesPresentUsingReducingCollector() {
        final Map<Long, Optional<Route>> map = cut.getActiveRoutes()
                .stream()
                .collect(groupingBy(r -> r.getCampaign().getId(), reducing((route, route2) -> {
                    throw new RuntimeException("Not Expected to here!");
                })));
        assertThat(map).containsKeys(1L, 2L, 3L);
        assertThat(map.values()).allMatch(Optional::isPresent);
    }

    @Test
    @DisplayName("Using a reducing collector we want to create a map from the unique campaign id to the route uniquely associated with that campaign")
    void shouldBeAbleToCreateMapToSingleRouteUsingReducingCollector() {
        final Map<Long, Route> map = cut.getActiveRoutes()
                .stream()
                .collect(groupingBy(r -> r.getCampaign().getId(),
                        reducing(null, identity(), (route, route2) -> route == null ? route2 : route)));

        assertThat(map).containsKeys(1L, 2L, 3L);
        assertThat(map.values()).extracting("id").containsExactly(10L, 20L, 30L);
    }
}