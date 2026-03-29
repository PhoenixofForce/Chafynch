package dev.phoenixofforce.tea.tracker.tea.type;

import java.util.List;

public record TeaTypeDto(Long id, String name) {

    public static TeaTypeDto from(TeaType teaType) {
        return new TeaTypeDto(teaType.getId(), teaType.getName());
    }

    public static List<TeaTypeDto> from(List<TeaType> teaTypes) {
        return teaTypes.stream()
                .map(TeaTypeDto::from)
                .toList();
    }
}
