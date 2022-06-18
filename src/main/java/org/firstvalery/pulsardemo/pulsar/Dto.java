package org.firstvalery.pulsardemo.pulsar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dto {
    private Object payload;
    private String type;

    @Override
    public String toString() {
        return "dto: {type = " + type + "}, {payload = " + payload + "}";
    }
}
