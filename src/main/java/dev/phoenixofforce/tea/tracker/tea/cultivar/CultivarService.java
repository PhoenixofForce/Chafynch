package dev.phoenixofforce.tea.tracker.tea.cultivar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CultivarService {

    private final CultivarRepository cultivarRepository;

    public List<CultivarDto> searchByName(String name) {
        return CultivarDto.from(searchByNameInRepository(name));
    }

    private List<Cultivar> searchByNameInRepository(String name) {
        if (name.isBlank()) return cultivarRepository.findAll();
        return cultivarRepository.findByNameContainingIgnoreCase(name);
    }

    public Cultivar resolveOrCreate(String name) {
        return cultivarRepository.findByName(name)
                .orElseGet(() -> {
                    Cultivar c = new Cultivar();
                    c.setName(name);
                    return cultivarRepository.save(c);
                });
    }


}