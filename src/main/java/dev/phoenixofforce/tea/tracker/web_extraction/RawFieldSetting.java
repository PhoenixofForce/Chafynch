package dev.phoenixofforce.tea.tracker.web_extraction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawFieldSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String field;

    private String selector;

    private String regex;

    @Convert(converter = StringSplitConverter.class)
    private List<String> operations;

    private boolean grabAll;

}
