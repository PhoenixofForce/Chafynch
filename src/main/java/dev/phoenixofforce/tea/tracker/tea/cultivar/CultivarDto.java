package dev.phoenixofforce.tea.tracker.tea.cultivar;

import java.util.List;

public record CultivarDto(Long id, String name) {

    public static CultivarDto from(Cultivar cultivar) {
        return new CultivarDto(cultivar.getId(), cultivar.getName());
    }

    public static List<CultivarDto> from(List<Cultivar> cultivars) {
        return cultivars.stream()
                .map(CultivarDto::from)
                .toList();
    }

}