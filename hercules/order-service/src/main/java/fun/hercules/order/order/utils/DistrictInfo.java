package fun.hercules.order.order.utils;

import java.util.List;

public class DistrictInfo {
    private String value;
    private String label;
    private List<DistrictInfo> children;

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public List<DistrictInfo> getChildren() {
        return children;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setChildren(List<DistrictInfo> children) {
        this.children = children;
    }
}
