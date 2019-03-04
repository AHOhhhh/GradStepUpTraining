package fun.hercules.order.order.platform.exports;

import com.google.common.collect.ImmutableSet;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.user.Role;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ToString
public class StatusTransitions {

    public static final StatusTransitions EMPTY = new StatusTransitions();

    @Delegate
    private Map<OrderStatus.Type, Map<OrderStatus.Type, Set<Role>>> transitions = new HashMap<>();

    public static StatusTransitionsBuilder builder() {
        return new StatusTransitionsBuilder();
    }

    public Optional<Set<Role>> getLegalRoles(OrderStatus.Type from, OrderStatus.Type to) {
        return Optional.ofNullable(transitions.get(from))
                .map(transitions -> transitions.get(to));
    }

    public static class StatusTransitionsBuilder {

        private final StatusTransitions statusTransitions = new StatusTransitions();

        public StatusTransitionsBuilder transition(
                OrderStatus.Type from, OrderStatus.Type to, Set<Role> roles) {
            Optional.ofNullable(statusTransitions.get(from))
                    .orElseGet(() -> {
                        HashMap<OrderStatus.Type, Set<Role>> target = new HashMap<>();
                        statusTransitions.put(from, target);
                        return target;
                    }).put(to, roles);
            return this;
        }

        public StatusTransitionsBuilder transition(
                OrderStatus.Type from, OrderStatus.Type to, Role... roles) {
            Optional.ofNullable(statusTransitions.get(from))
                    .orElseGet(() -> {
                        HashMap<OrderStatus.Type, Set<Role>> target = new HashMap<>();
                        statusTransitions.put(from, target);
                        return target;
                    }).put(to, ImmutableSet.copyOf(roles));
            return this;
        }

        public StatusTransitions build() {
            return statusTransitions;
        }
    }
}
