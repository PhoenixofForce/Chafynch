package dev.phoenixofforce.tea.tracker.tea.cultivar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CultivarService {

    private final CultivarRepository cultivarRepository;

    public Cultivar resolveOrCreate(String name) {
        return cultivarRepository.findByName(name)
                .orElseGet(() -> {
                    Cultivar c = new Cultivar();
                    c.setName(name);
                    return cultivarRepository.save(c);
                });
    }
}