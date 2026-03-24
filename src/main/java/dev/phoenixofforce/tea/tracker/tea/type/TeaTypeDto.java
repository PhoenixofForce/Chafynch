package dev.phoenixofforce.tea.tracker.tea.type;

public record TeaTypeDto(Long id, String name) {

    public static TeaTypeDto from(TeaType teaType) {
        return new TeaTypeDto(teaType.getId(), teaType.getName());
    }
}
