package dev.phoenixofforce.tea.tracker.session;

import dev.phoenixofforce.tea.tracker.session.infusion.Infusion;
import dev.phoenixofforce.tea.tracker.session.infusion.InfusionDto;
import dev.phoenixofforce.tea.tracker.session.infusion.InfusionTastingNote;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNote;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteDto;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteService;
import dev.phoenixofforce.tea.tracker.tea.TeaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final Clock clock;

    private final TeaRepository teaRepository;

    private final TastingNoteService tastingNoteService;

    private final SessionRepository repository;

    @Transactional(readOnly = true)
    public List<SessionDto> findAll(Long teaId) {
        return repository.findByTeaId(teaId).stream()
            .map(SessionDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public SessionDto getById(Long id) {
        Session session = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        SessionDto dto = SessionDto.from(session);

        Optional<Session> lastSession = repository.findLastSessionBeforeId(id);
        dto.setHintFromLastSession(lastSession.map(Session::getNextSessionHint).orElse(null));

        return dto;
    }

    @Transactional
    public SessionDto createOrUpdateSession(Long teaId, SessionDto dto) {
        Session session = new Session();
        if (dto.getId() != null) {
            session = repository.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        } else {
            session.setTea(
                teaRepository.findById(teaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tea not found")));
        }

        applyDto(dto, session);
        session = repository.save(session);
        return SessionDto.from(session);
    }

    private void applyDto(SessionDto dto, Session session) {
        session.setStartTime(dto.getStartTime());
        session.setLastUpdated(clock.instant()); // Todo: only if old stamp is newer then 24h
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
        repository.flush();
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
        repository.flush();
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
