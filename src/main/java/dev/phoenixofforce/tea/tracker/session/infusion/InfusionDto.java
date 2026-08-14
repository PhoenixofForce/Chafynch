package dev.phoenixofforce.tea.tracker.session.infusion;

import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteDto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class InfusionDto {

    private Long id;

    private Instant startTime;

    private BigDecimal infusionTime;

    private BigDecimal temperature;

    private Integer rating;

    private boolean isRinse;

    private List<TastingNoteDto> tastingNotes = new ArrayList<>();

    public static InfusionDto from(Infusion infusion) {
        InfusionDto dto = new InfusionDto();

        dto.setId(infusion.getId());
        dto.setStartTime(infusion.getStartTime());
        dto.setInfusionTime(infusion.getInfusionTime());
        dto.setTemperature(infusion.getTemperature());
        dto.setRating(infusion.getRating());
        dto.setRinse(infusion.isRinse());

        List<TastingNoteDto> tastingNotes = infusion.getTastingNotes()
            .stream()
            .map(
                note -> new TastingNoteDto(
                    note.getCategory(),
                    note.getSubCategory(),
                    note.getTastingNote().getNote()))
            .toList();
        dto.setTastingNotes(tastingNotes);

        return dto;
    }
}
