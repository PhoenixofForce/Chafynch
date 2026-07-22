package dev.phoenixofforce.tea.tracker.tea;

import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNoteDto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TeaDTO {

    private Long id;

    @NotBlank
    private String name;

    private String cultivar; //todo: use cultivar dto

    private String teaType; // Todo: use teaType dto

    private String vendor; //Todo: use vendor dto

    // Todo: use LocationDto
    private String originCountry;

    private String originProvince;

    private String originCity;

    private Double originLatitude;

    private Double originLongitude;

    private BigDecimal price;

    private LocalDate purchaseDate;

    private BigDecimal weightGrams;

    private String descriptionMd;

    private Integer harvestYear;

    private String harvestLabel;

    private String website;

    private Integer rating;

    private List<TastingNoteDto> tastingNotes;

    public static TeaDTO from(Tea tea) {
        TeaDTO dto = new TeaDTO();
        dto.setId(tea.getId());
        dto.setName(tea.getName());
        dto.setPrice(tea.getPrice());
        dto.setPurchaseDate(tea.getPurchaseDate());
        dto.setWeightGrams(tea.getWeightGrams());

        if (tea.getCultivar() != null) dto.setCultivar(tea.getCultivar().getName());
        if (tea.getTeaType() != null) dto.setTeaType(tea.getTeaType().getName());
        if (tea.getVendor() != null) dto.setVendor(tea.getVendor().getName());
        if (tea.getOriginLocation() != null) {
            dto.setOriginCountry(tea.getOriginLocation().getCountry());
            dto.setOriginProvince(tea.getOriginLocation().getProvince());
            dto.setOriginCity(tea.getOriginLocation().getCity());
            dto.setOriginLatitude(tea.getOriginLocation().getLatitude());
            dto.setOriginLongitude(tea.getOriginLocation().getLongitude());
        }
        if (tea.getDescriptionMd() != null) dto.setDescriptionMd(tea.getDescriptionMd());
        if (tea.getRating() != null) dto.setRating(tea.getRating());
        if (tea.getWebsite() != null) dto.setWebsite(tea.getWebsite());
        if (tea.getHarvestYear() != null) dto.setHarvestYear(tea.getHarvestYear());
        if (tea.getHarvestLabel() != null) dto.setHarvestLabel(tea.getHarvestLabel());
        if (tea.getTastingNotes() != null) {
            dto.setTastingNotes(TastingNoteDto.from(tea.getTastingNotes()));
        } else dto.setTastingNotes(List.of());

        return dto;
    }

    public static List<TeaDTO> from(List<Tea> teas) {
        return teas.stream()
            .map(TeaDTO::from)
            .toList();
    }
}
