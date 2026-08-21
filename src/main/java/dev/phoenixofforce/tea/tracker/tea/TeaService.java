package dev.phoenixofforce.tea.tracker.tea;

import dev.phoenixofforce.tea.tracker.location.LocationService;
import dev.phoenixofforce.tea.tracker.session.SessionDto;
import dev.phoenixofforce.tea.tracker.session.SessionService;
import dev.phoenixofforce.tea.tracker.tea.cultivar.CultivarService;
import dev.phoenixofforce.tea.tracker.tea.type.TeaTypeService;
import dev.phoenixofforce.tea.tracker.vendor.VendorService;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeaService {

    private final TeaRepository teaRepository;

    private final CultivarService cultivarService;

    private final TeaTypeService teaTypeService;

    private final VendorService vendorService;

    private final LocationService locationService;

    private final SessionService sessionService;

    @Transactional(readOnly = true)
    public List<TeaDTO> findAll() {
        return TeaDTO.from(teaRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TeaDTO findById(Long id) {
        Tea tea = teaRepository.findById(id)
            .orElseThrow();

        TeaDTO dto = TeaDTO.from(tea);
        dto.setTastingNotes(findTopTastingNotesForTea(tea.getId(), 10));
        return dto;
    }

    private List<String> findTopTastingNotesForTea(long teaId, long limit) {
        List<SessionDto> sessions = sessionService.findAll(teaId);
        Map<String, Integer> notesByCount = new HashMap<>();
        for (SessionDto session : sessions) {
            for (var tastingNote : session.getTastingNotes()) {
                notesByCount.put(tastingNote.note(), notesByCount.getOrDefault(tastingNote.note(), 0) + 1);
            }

            for (var infusion : session.getInfusions()) {
                for (var tastingNote : infusion.getTastingNotes()) {
                    notesByCount.put(tastingNote.note(), notesByCount.getOrDefault(tastingNote.note(), 0) + 1);
                }
            }
        }
        return notesByCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .toList();
    }

    @Transactional
    public TeaDTO create(TeaDTO dto) {
        Tea tea = new Tea();
        applyDto(dto, tea);

        return TeaDTO.from(teaRepository.save(tea));
    }

    @Transactional
    public TeaDTO update(long id, TeaDTO dto) {
        Tea tea = teaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tea Not Found"));
        applyDto(dto, tea);

        tea = teaRepository.save(tea);
        return TeaDTO.from(tea);
    }

    @Transactional
    public void delete(long id) {
        if (!teaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tea not Found");
        }

        try {
            teaRepository.deleteById(id);
        } catch (DataIntegrityViolationException _) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tea in use");
        }
    }

    //Todo: move into mapper/ dto
    private void applyDto(TeaDTO dto, Tea tea) {
        tea.setName(dto.getName());
        tea.setPrice(dto.getPrice());
        tea.setPurchaseDate(dto.getPurchaseDate());
        tea.setWeightGrams(dto.getWeightGrams());

        tea.setHarvestLabel(dto.getHarvestLabel());
        tea.setHarvestYear(dto.getHarvestYear());
        tea.setDescriptionMd(dto.getDescriptionMd());
        tea.setWebsite(dto.getWebsite());

        if (dto.getCultivar() != null && !dto.getCultivar().isBlank()) {
            tea.setCultivar(cultivarService.resolveOrCreate(dto.getCultivar()));
        } else {
            tea.setCultivar(null);
        }

        if (dto.getTeaType() != null && !dto.getTeaType().isBlank()) {
            tea.setTeaType(
                teaTypeService.findByName(dto.getTeaType())
                    .orElseThrow(
                        () -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Unknown tea type: " + dto.getTeaType())));
        } else {
            tea.setTeaType(null);
        }

        if (dto.getVendor() != null && !dto.getVendor().isBlank()) {
            tea.setVendor(vendorService.resolveOrCreate(dto.getVendor()));
        } else {
            tea.setVendor(null);
        }

        if (dto.getOriginCountry() != null && !dto.getOriginCountry().isBlank()) {
            tea.setOriginLocation(
                locationService.resolveOrCreate(
                    dto.getOriginCountry(),
                    dto.getOriginProvince(),
                    dto.getOriginCity()));
        } else {
            tea.setOriginLocation(null);
        }
    }
}
