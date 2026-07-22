package dev.phoenixofforce.tea.tracker.session.tasting_note;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TastingNoteDto(@NotNull Long id, @NotNull String note) {

    public static TastingNoteDto from(TastingNote tastingNote) {
        return new TastingNoteDto(tastingNote.getId(), tastingNote.getNote());
    }

    public static List<TastingNoteDto> from(List<TastingNote> tastingNotes) {
        return tastingNotes.stream()
            .map(TastingNoteDto::from)
            .toList();
    }

}
