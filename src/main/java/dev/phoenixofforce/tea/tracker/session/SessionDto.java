package dev.phoenixofforce.tea.tracker.session;

import dev.phoenixofforce.tea.tracker.session.infusion.InfusionDto;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteDto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Data
public class SessionDto {

    private Long id;

    private Instant startTime;

    private Instant lastUpdated;

    private BigDecimal weight;

    private BigDecimal volume;

    private String location;

    private String people;

    private Integer rating;

    private String sessionSummary;

    private String nextSessionHint;

    private String hintFromLastSession;

    private List<InfusionDto> infusions = new ArrayList<>();

    private List<TastingNoteDto> tastingNotes = new ArrayList<>();

    public static SessionDto from(Session session) {
        SessionDto dto = new SessionDto();

        dto.setId(session.getId());

        if (session.getStartTime() != null) {
            dto.setStartTime(session.getStartTime());
        } else {
            dto.setStartTime(Instant.now());
        }

        dto.setLastUpdated(session.getLastUpdated());
        dto.setWeight(session.getWeight());
        dto.setVolume(session.getVolume());
        dto.setLocation(session.getLocation());
        dto.setPeople(session.getPeople());
        dto.setRating(session.getRating());
        dto.setSessionSummary(session.getSessionSummary());
        dto.setNextSessionHint(session.getNextSessionHint());

        List<InfusionDto> infusionDtos = session.getInfusions()
            .stream()
            .map(InfusionDto::from)
            .toList();
        dto.setInfusions(infusionDtos);

        List<TastingNoteDto> tastingNotes = session.getTastingNotes()
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
