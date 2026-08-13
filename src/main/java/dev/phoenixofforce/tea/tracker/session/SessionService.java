package dev.phoenixofforce.tea.tracker.session;

import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNote;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteDto;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final TastingNoteService tastingNoteService;

    private void applyDto(SessionDto dto, Session session) {
        session.setStartTime(dto.getStartTime());
        session.setLastUpdated(Instant.now()); // Todo: only if old stamp is newer then 24h
        session.setWeight(dto.getWeight());
        session.setVolume(dto.getVolume());
        session.setLocation(dto.getLocation());
        session.setPeople(dto.getPeople());
        session.setRating(dto.getRating());
        session.setSessionSummary(dto.getSessionSummary());
        session.setNextSessionHint(dto.getNextSessionHint());

        Set<String> allTastingNotes = new HashSet<>();
        allTastingNotes.addAll(dto.getTastingNotes().stream().map(TastingNoteDto::note).toList());
        allTastingNotes
            .addAll(
                dto.getInfusions()
                    .stream()
                    .flatMap(
                        infusion -> infusion.getTastingNotes()
                            .stream())
                    .map(TastingNoteDto::note)
                    .toList());

        Map<String, TastingNote> tastingNoteResolver = tastingNoteService
            .resolveOrCreateAll(allTastingNotes);

        Set<Long> infusionsToKeep = new HashSet<>();
        for (InfusionDto infusionDto : dto.getInfusions()) {
            if (infusionDto.getId() == null) {
                session.addInfusion(applyDto(infusionDto, new Infusion(), tastingNoteResolver));
                continue;
            }

            Infusion existing = session.getInfusions().stream()
                .filter(infusion -> Objects.equals(infusion.getId(), infusionDto.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Infusion not found"));
            applyDto(infusionDto, existing, tastingNoteResolver);
            infusionsToKeep.add(infusionDto.getId());
        }
        session.getInfusions()
            .removeIf(infusion -> infusion.getId() != null && !infusionsToKeep.contains(infusion.getId()));

        session.getTastingNotes().clear();
        for (TastingNoteDto noteDto : dto.getTastingNotes()) {
            TastingNote note = tastingNoteResolver.get(noteDto.note());

            SessionTastingNote tastingNote = new SessionTastingNote();
            tastingNote.setTastingNote(note);
            tastingNote.setSession(session);
            tastingNote.setCategory(noteDto.category());
            tastingNote.setSubCategory(noteDto.subCategory());

            session.getTastingNotes().add(tastingNote);
        }

    }

    private Infusion applyDto(InfusionDto dto, Infusion infusion, Map<String, TastingNote> tastingNoteResolver) {
        infusion.setStartTime(dto.getStartTime());
        infusion.setInfusionTime(dto.getInfusionTime());
        infusion.setTemperature(dto.getTemperature());
        infusion.setRating(dto.getRating());
        infusion.setRinse(dto.isRinse());

        infusion.getTastingNotes().clear();
        for (TastingNoteDto noteDto : dto.getTastingNotes()) {
            TastingNote note = tastingNoteResolver.get(noteDto.note());

            InfusionTastingNote tastingNote = new InfusionTastingNote();
            tastingNote.setTastingNote(note);
            tastingNote.setInfusion(infusion);
            tastingNote.setCategory(noteDto.category());
            tastingNote.setSubCategory(noteDto.subCategory());

            infusion.getTastingNotes().add(tastingNote);
        }

        return infusion;
    }
}
