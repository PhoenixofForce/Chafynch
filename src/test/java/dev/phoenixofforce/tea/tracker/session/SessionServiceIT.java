package dev.phoenixofforce.tea.tracker.session;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.phoenixofforce.tea.tracker.BaseIT;
import dev.phoenixofforce.tea.tracker.session.infusion.InfusionDto;
import dev.phoenixofforce.tea.tracker.session.infusion.InfusionRepository;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteDto;
import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteRepository;
import dev.phoenixofforce.tea.tracker.tea.TeaDTO;
import dev.phoenixofforce.tea.tracker.tea.TeaService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;

import java.time.Instant;
import java.util.List;

public class SessionServiceIT extends BaseIT {

    @Autowired
    private TeaService teaService;

    @Autowired
    private SessionService service;

    @Autowired
    private TastingNoteRepository tastingNoteRepository;

    @Autowired
    private InfusionRepository infusionRepository;

    @Test
    void shouldKeepOldTastingNotesWhenOrphaned() {
        TeaDTO teaDTO = new TeaDTO();
        teaDTO.setName("TestTea");
        teaDTO = teaService.create(teaDTO);

        SessionDto dto = new SessionDto();
        dto.setStartTime(Instant.now());
        dto.setBrewingMethod("Gongfu");
        dto.setRating(5);
        dto.setTastingNotes(List.of(new TastingNoteDto("wet leaf", "nose", "apple")));
        dto = service.createOrUpdateSession(teaDTO.getId(), dto);

        List<SessionDto> sessions = service.findAll(null);
        assertEquals(1, sessions.size());
        assertEquals(1, tastingNoteRepository.findNotes("", Limit.of(20)).size());

        dto.setTastingNotes(List.of(new TastingNoteDto("wet leaf", "nose", "other apple")));
        dto = service.createOrUpdateSession(teaDTO.getId(), dto);
        assertEquals(2, tastingNoteRepository.findNotes("", Limit.of(20)).size());

        dto.setTastingNotes(
            List.of(
                new TastingNoteDto("wet leaf", "nose", "other apple"),
                new TastingNoteDto("wet leaf", "nose", "apricot")));
        dto = service.createOrUpdateSession(teaDTO.getId(), dto);
        assertEquals(2, tastingNoteRepository.findNotes("", Limit.of(20)).size());
    }

    @Test
    void shouldDeleteInfusionsWhenOrphaned() {
        TeaDTO teaDTO = new TeaDTO();
        teaDTO.setName("TestTea");
        teaDTO = teaService.create(teaDTO);

        InfusionDto infusion1 = new InfusionDto();
        infusion1.setStartTime(Instant.now());
        infusion1.setRating(3);

        InfusionDto infusion2 = new InfusionDto();
        infusion2.setStartTime(Instant.now().plusSeconds(60));
        infusion2.setRating(4);

        SessionDto dto = new SessionDto();
        dto.setStartTime(Instant.now());
        dto.setBrewingMethod("Gongfu");
        dto.setRating(5);
        dto.setInfusions(List.of(infusion1));
        dto = service.createOrUpdateSession(teaDTO.getId(), dto);

        List<SessionDto> sessions = service.findAll(null);
        assertEquals(1, sessions.size());
        assertEquals(1, infusionRepository.findAll().size());

        dto.setInfusions(List.of(infusion1, infusion2));
        dto = service.createOrUpdateSession(teaDTO.getId(), dto);
        assertEquals(2, infusionRepository.findAll().size());

        dto.setInfusions(List.of(infusion2));
        dto = service.createOrUpdateSession(teaDTO.getId(), dto);
        assertEquals(1, infusionRepository.findAll().size());
    }

}
