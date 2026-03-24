package dev.phoenixofforce.tea.tracker.tea.cultivar;

public record CultivarDto(Long id, String name) {

    public static CultivarDto from(Cultivar cultivar) {
        return new CultivarDto(cultivar.getId(), cultivar.getName());
    }
}