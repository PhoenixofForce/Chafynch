package dev.phoenixofforce.tea.tracker.tea;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TeaDTO {

    private Long id;

    @NotBlank
    private String name;

    private String cultivar;
    private String teaType;
    private String vendor;

    private String originCountry;
    private String originProvince;
    private String originCity;
    private Double originLatitude;
    private Double originLongitude;

    private BigDecimal price;
    private LocalDate purchaseDate;
    private BigDecimal weightGrams;

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

        return dto;
    }
}