package dev.phoenixofforce.tea.tracker.tea.type;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeaTypeDto(@NotNull Long id, @NotNull String name) {

    public static TeaTypeDto from(TeaType teaType) {
        return new TeaTypeDto(teaType.getId(), teaType.getName());
    }

    public static List<TeaTypeDto> from(List<TeaType> teaTypes) {
        return teaTypes.stream()
                .map(TeaTypeDto::from)
                .toList();
    }
}
