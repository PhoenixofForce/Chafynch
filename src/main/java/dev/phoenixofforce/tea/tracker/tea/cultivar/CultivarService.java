package dev.phoenixofforce.tea.tracker.tea.cultivar;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CultivarService {

    private final CultivarRepository cultivarRepository;

    @Transactional(readOnly = true)
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


    @Transactional
    public CultivarDto update(CultivarDto cultivarDto) {
        Optional<Cultivar> optionalCultivar = cultivarRepository.findById(cultivarDto.id());
        if (optionalCultivar.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cultivar with id " + cultivarDto.id() + " not found");
        }
        Cultivar cultivar = optionalCultivar.get();
        cultivar.setName(cultivarDto.name());
        cultivar = cultivarRepository.save(cultivar);
        return CultivarDto.from(cultivar);
    }

    public void delete(Long id) {
        if(!cultivarRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cultivar not Found");
        }

        try {
            cultivarRepository.deleteById(id);
        } catch (DataIntegrityViolationException _) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cultivar in use");
        }
    }
}