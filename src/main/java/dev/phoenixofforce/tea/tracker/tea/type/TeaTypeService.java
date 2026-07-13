package dev.phoenixofforce.tea.tracker.tea.type;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeaTypeService {

    private final TeaTypeRepository teaTypeRepository;

    public Optional<TeaType> findByName(String name) {
        return teaTypeRepository.findByName(name);
    }

    public List<TeaTypeDto> findAll() {
        return TeaTypeDto.from(teaTypeRepository.findAll(Sort.by("name")));
    }

    @Transactional
    public TeaType create(String name) {
        Optional<TeaType> optionalTeaType = teaTypeRepository.findByName(name);
        if (optionalTeaType.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "TeaType '" + name + "' already exists");
        }

        TeaType c = new TeaType();
        c.setName(name);
        return teaTypeRepository.save(c);
    }

    @Transactional
    public TeaTypeDto update(long id, TeaTypeDto teaTypeDto) {
        Optional<TeaType> optionalTeaType = teaTypeRepository.findById(id);
        if (optionalTeaType.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TeaType with id " + id + " not found");
        }
        TeaType teaType = optionalTeaType.get();
        teaType.setName(teaTypeDto.name());
        teaType = teaTypeRepository.save(teaType);
        return TeaTypeDto.from(teaType);
    }

    public void delete(Long id) {
        if(!teaTypeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tea type not Found");
        }

        try {
            teaTypeRepository.deleteById(id);
        } catch (DataIntegrityViolationException _) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tea type in use");
        }
    }

}