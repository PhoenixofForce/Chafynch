package dev.phoenixofforce.tea.tracker.tea;

import dev.phoenixofforce.tea.tracker.location.LocationService;
import dev.phoenixofforce.tea.tracker.tea.cultivar.CultivarService;
import dev.phoenixofforce.tea.tracker.tea.type.TeaTypeRepository;
import dev.phoenixofforce.tea.tracker.vendor.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeaService {

    private final TeaRepository teaRepository;
    private final CultivarService cultivarService;
    private final TeaTypeRepository teaTypeRepository;
    private final VendorService vendorService;
    private final LocationService locationService;

    @Transactional(readOnly = true)
    public List<TeaDTO> findAll() {
        return teaRepository.findAllWithRelations().stream()
                .map(TeaDTO::from)
                .toList();
    }

    @Transactional
    public TeaDTO create(TeaDTO dto) {
        Tea tea = new Tea();
        tea.setName(dto.getName());
        tea.setPrice(dto.getPrice());
        tea.setPurchaseDate(dto.getPurchaseDate());
        tea.setWeightGrams(dto.getWeightGrams());

        if (dto.getCultivar() != null && !dto.getCultivar().isBlank()) {
            tea.setCultivar(cultivarService.resolveOrCreate(dto.getCultivar()));
        }

        if (dto.getTeaType() != null && !dto.getTeaType().isBlank()) {
            tea.setTeaType(teaTypeRepository.findByName(dto.getTeaType())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Unknown tea type: " + dto.getTeaType())));
        }

        if (dto.getVendor() != null && !dto.getVendor().isBlank()) {
            tea.setVendor(vendorService.resolveOrCreate(dto.getVendor()));
        }

        if (dto.getOriginCountry() != null && !dto.getOriginCountry().isBlank()) {
            tea.setOriginLocation(locationService.resolveOrCreate(
                    dto.getOriginCountry(), dto.getOriginProvince(), dto.getOriginCity()));
        }

        return TeaDTO.from(teaRepository.save(tea));
    }
}