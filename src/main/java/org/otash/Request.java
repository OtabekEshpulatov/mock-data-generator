package org.otash;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private String fileName;
    private FileType fileType;
    private int count;

    private Set<Field> fields;
}
