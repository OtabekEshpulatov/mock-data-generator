package org.jlkesh;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FakerApplicationGenerateRequest {
    private String fileName;
    private FileType fileType;
    private int count;
    @Builder.Default
    private Set<Field> fields = new HashSet<>();
}
