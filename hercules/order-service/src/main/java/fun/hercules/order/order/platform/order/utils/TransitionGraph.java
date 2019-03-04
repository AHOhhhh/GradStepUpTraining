package fun.hercules.order.order.platform.order.utils;

import fun.hercules.order.order.platform.exports.StatusTransitions;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.user.Role;
import guru.nidi.graphviz.attribute.Attributes;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Label;
import guru.nidi.graphviz.model.LinkSource;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.Node;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class TransitionGraph {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final StatusTransitions statusTransitions;

    private HashMap<OrderStatus.Type, Node> nodeMap = new HashMap<>();

    public TransitionGraph(StatusTransitions statusTransitions) {
        this.statusTransitions = statusTransitions;
    }

    private Graph build(StatusTransitions statusTransitions) {
        return Factory.graph("status-transition").directed()
                .with(statusTransitions.entrySet().stream()
                        .map(transition -> getNode(transition.getKey()).link(
                                transition.getValue().entrySet().stream()
                                        .map(entry -> to(getNode(entry.getKey())).with(getAttributes(entry.getValue())))
                                        .toArray(LinkTarget[]::new)
                        )).toArray(LinkSource[]::new));
    }

    private Attributes getAttributes(Set<Role> roles) {
        Label label = Label.of(String.join("\n", roles.stream()
                .map(role -> "  " + role.name()).collect(Collectors.toList())));
        switch (roles.stream().findFirst().orElse(Role.PlatformService)) {
            case PlatformService:
                return Attributes.attrs(label, Style.DASHED, Color.BLACK);
            case PlatformAdmin:
                return Attributes.attrs(label, Color.RED);
            case EnterpriseAdmin:
                return Attributes.attrs(label, Color.RED);
            case EnterpriseUser:
                return Attributes.attrs(label, Color.BLACK);
            case Internal:
                return Attributes.attrs(label, Style.DASHED, Color.GRAY50);
            default:
                return Style.SOLID;
        }
    }

    private Node getNode(OrderStatus.Type type) {
        if (!nodeMap.containsKey(type)) {
            Node node = node(type.name());
            if (type.equals(OrderStatus.Type.Submitted)) {
                node = node.with(Style.FILLED, Color.GRAY);
            } else if (type.equals(OrderStatus.Type.Closed)) {
                node = node.with(Style.FILLED, Color.DARKGREEN);
            } else if (type.equals(OrderStatus.Type.Cancelled)) {
                node = node.with(Style.FILLED, Color.ORANGERED);
            }
            nodeMap.put(type, node);
        }
        return nodeMap.get(type);
    }

    public String render() {
        try {
            return executor.submit(() ->
                    Graphviz.fromGraph(build(statusTransitions)).width(1024).render(Format.SVG_STANDALONE).toString()
            ).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
